package edu.fpt.lab4.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.fpt.lab4.R;
import edu.fpt.lab4.models.MyEmployee;
import edu.fpt.lab4.models.User;
import edu.fpt.lab4.utils.ApiInterface;
import edu.fpt.lab4.utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeInformationActivity extends AppCompatActivity {
    private EditText edName,edEmail,edPhone,edAddress;
    private ImageView imgSelected,imgPic;

    String name,email,phone,address,password,base64Image;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);

        edName=findViewById(R.id.edNameUserCIP);
        edEmail=findViewById(R.id.edEmailCIP);
        edPhone=findViewById(R.id.edPhoneUserCIP);
        edAddress=findViewById(R.id.edAddressUserCIP);
        imgSelected=findViewById(R.id.imgUserSelectedCIP);
        imgPic=findViewById(R.id.imgChooseCIP);

        edEmail.setEnabled(false);

        findViewById(R.id.imgBackFromChangeInfor).setOnClickListener(v -> {
            finish();
        });

        imgPic.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);

        });
        findViewById(R.id.btnSaveUserCIP).setOnClickListener(v -> {
            saveInformation();


        });
        findViewById(R.id.btnCancelSaveUseCIP).setOnClickListener(v -> {
            Toast.makeText(ChangeInformationActivity.this, "Pass."+password, Toast.LENGTH_SHORT).show();


        });
        getUserInformation();


    }
    private void saveInformation(){
        String name=edName.getText().toString().trim();
        String email=edEmail.getText().toString().trim();
        String phone=edPhone.getText().toString().trim();
        String address=edAddress.getText().toString().trim();

        User user=new User(email,password,name,phone,address,base64Image);
        updateUserOnServer(user);

    }

    private void getUserInformation(){
        ApiInterface apiService = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<User> call = apiService.getUserById(getID());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        password=user.getPassword();

                        base64Image=user.getImg();
                        edName.setText(user.getName());
                        edEmail.setText(user.getEmail());
                        edPhone.setText(user.getPhone());
                        edAddress.setText(user.getAddress());
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imgSelected.setImageBitmap(decodedBitmap);

                    }
                } else {
                    Log.i("Error","Can't get the user information"+response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("Error",""+t.getMessage().toString());
                t.printStackTrace();
            }
        });
    }
    private void updateUserOnServer(User user) {
        ApiInterface apiService = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<User> call = apiService.updateUserById(getID(),user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChangeInformationActivity.this, "User updated successfully!"+response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("Errorr",response.message());
                } else {
                    Toast.makeText(ChangeInformationActivity.this, "Failed to update user."+response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("Errorr",response.message());

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ChangeInformationActivity.this, "Failed to update user.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.i("Errorr",t.getMessage().toString());

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            base64Image = resizeImageAndConvertToBase64(selectedImageUri);
            if (base64Image != null) {
                byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                imgSelected.setImageBitmap(decodedBitmap);
            }
        }
    }

    private String resizeImageAndConvertToBase64(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            String base64Image = resizeImageAndConvertToBase64(inputStream);
            inputStream.close();
            return base64Image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String resizeImageAndConvertToBase64(InputStream inputStream) throws IOException {
        int maxWidth = 512;
        int maxHeight = 512;

        Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);

        float scale = Math.min((float) maxWidth / originalBitmap.getWidth(), (float) maxHeight / originalBitmap.getHeight());

        int newWidth = Math.round(originalBitmap.getWidth() * scale);
        int newHeight = Math.round(originalBitmap.getHeight() * scale);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, baos);
        byte[] imageBytes = baos.toByteArray();
        String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        originalBitmap.recycle();
        resizedBitmap.recycle();

        return base64Image;
    }

    private String getID(){
        String id = getIntent().getStringExtra("id");
        return id;
    }
}