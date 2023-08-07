package edu.fpt.lab4.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.fpt.lab4.MainActivity;
import edu.fpt.lab4.R;
import edu.fpt.lab4.models.MyEmployee;
import edu.fpt.lab4.utils.ApiInterface;
import edu.fpt.lab4.utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEmployeeActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imgPicker;
    private EditText edName,edEmail,edPhone,edAddress,edRole;
    private RadioButton rdoMale,rdoFemale;
    private String base64Image;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        imgPicker = findViewById(R.id.imgSelected);

        edName=findViewById(R.id.edNameEmp);
        edEmail=findViewById(R.id.edEmailEmp);
        edPhone=findViewById(R.id.edPhoneEmp);
        edAddress=findViewById(R.id.edAddressEmp);
        edRole=findViewById(R.id.edRoleEmp);
        rdoMale=findViewById(R.id.radioButtonMale);
        rdoFemale=findViewById(R.id.radioButtonFemale);




        findViewById(R.id.imgBackFromAddEmp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.imgChooseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

            }
        });

        findViewById(R.id.btnSaveEmp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEmployee();
                finish();
            }
        });
        MyEmployee currentEmployee = (MyEmployee) getIntent().getSerializableExtra("employeeData");
        if (currentEmployee != null) {
            edName.setText(currentEmployee.getName());
            if (currentEmployee.getGender().equals("Male")) {
                rdoMale.setChecked(true);
            } else {
                rdoFemale.setChecked(true);
            }
            edEmail.setText(currentEmployee.getEmail());
            edPhone.setText(currentEmployee.getPhone());
            edAddress.setText(currentEmployee.getAddress());
            edRole.setText(currentEmployee.getRole());

            if (currentEmployee.getImage() != null && !currentEmployee.getImage().isEmpty()) {
                byte[] decodedBytes = Base64.decode(currentEmployee.getImage(), Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                imgPicker.setImageBitmap(decodedBitmap);
            }
        }
    }




    private void saveEmployee(){
        String userId=getIDBoss();
        String imgUrl=base64Image;
        String name=edName.getText().toString().trim();
        String gender = rdoMale.isChecked() ? "Male" : "Female";
        String email=edEmail.getText().toString().trim();
        String phone=edPhone.getText().toString();
        String address=edAddress.getText().toString().trim();
        String role=edRole.getText().toString().trim();

        MyEmployee currentEmployee = (MyEmployee) getIntent().getSerializableExtra("employeeData");
        if (currentEmployee != null) {
            currentEmployee.setName(name);
            currentEmployee.setGender(gender);
            currentEmployee.setEmail(email);
            currentEmployee.setPhone(phone);
            currentEmployee.setAddress(address);
            currentEmployee.setRole(role);
            currentEmployee.setImage(imgUrl);
            updateEmployeeOnServer(currentEmployee);
        } else {
            MyEmployee newEmployee = new MyEmployee(userId, email, name, gender, imgUrl, role, phone, address);
            createEmployeeOnServer(newEmployee);
        }


    }
    private void createEmployeeOnServer(MyEmployee employee) {
        ApiInterface apiService = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<Void> call = apiService.createEmployee(employee);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddEmployeeActivity.this, "Employee saved successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddEmployeeActivity.this, "Failed to save employee.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddEmployeeActivity.this, "Failed to save employee.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEmployeeOnServer(MyEmployee employee) {
        ApiInterface apiService = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        String employeeId = employee.get_id();
        Call<MyEmployee> call = apiService.updateEmployee(employeeId, employee);
        call.enqueue(new Callback<MyEmployee>() {
            @Override
            public void onResponse(Call<MyEmployee> call, Response<MyEmployee> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddEmployeeActivity.this, "Employee updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddEmployeeActivity.this, "Failed to update employee.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyEmployee> call, Throwable t) {
                Toast.makeText(AddEmployeeActivity.this, "Failed to update employee.", Toast.LENGTH_SHORT).show();
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
                imgPicker.setImageBitmap(decodedBitmap);
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


    private String getIDBoss(){
        String bossID = getIntent().getStringExtra("boss_id");
        return bossID;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}