package edu.fpt.lab4.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import edu.fpt.lab4.R;
import edu.fpt.lab4.models.User;
import edu.fpt.lab4.utils.ApiInterface;
import edu.fpt.lab4.utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText edOldPass,edNewPass,edComfirm;
    private String oldPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        edOldPass=findViewById(R.id.edCurrentPass);
        edNewPass=findViewById(R.id.edNewPss);
        edComfirm=findViewById(R.id.edRetypeNew);



        findViewById(R.id.imgBackFromChangePass).setOnClickListener(v -> {
            finish();
        });
        findViewById(R.id.btnSavePass).setOnClickListener(v -> {
            updatePassword();
        });

        getUserInformation();
    }


    private void updatePassword(){
        String currentPass=edOldPass.getText().toString().trim();
        String newPass=edNewPass.getText().toString().trim();
        String reType=edComfirm.getText().toString().trim();


        if(currentPass.isEmpty() || newPass.isEmpty()||reType.isEmpty()){
            Toast.makeText(ChangePasswordActivity.this,"Please fill full the fields",Toast.LENGTH_SHORT).show();
        }else{
            if(!reType.matches(newPass)){
                Toast.makeText(ChangePasswordActivity.this,"Retype password isn't match",Toast.LENGTH_SHORT).show();
            }else if(!currentPass.matches(oldPass)){
                Toast.makeText(ChangePasswordActivity.this,"Current password is incorrect!",Toast.LENGTH_SHORT).show();

            }else{
                User user=new User(newPass);
                updateUserOnServer(user);
            }
        }
    }

    private void getUserInformation(){
        ApiInterface apiService = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<User> call = apiService.getUserById(getID());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    oldPass=user.getPassword();
                    edOldPass.setText(user.getPassword());
                    if (user != null) {
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
                    Toast.makeText(ChangePasswordActivity.this, "Password has been update!"+response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("Errorr",response.message());
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Failed to update password."+response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("Errorr",response.message());

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Failed to update password.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.i("Errorr",t.getMessage().toString());

            }
        });
    }


    private String getID(){
        String id = getIntent().getStringExtra("id");
        return id;
    }
}