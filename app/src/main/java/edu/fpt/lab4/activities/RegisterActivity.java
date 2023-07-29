package edu.fpt.lab4.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.fpt.lab4.R;
import edu.fpt.lab4.models.User;
import edu.fpt.lab4.utils.ApiInterface;
import edu.fpt.lab4.utils.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText edName,edEmail,edPhone,edAddress,edPassword,edComfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edName=findViewById(R.id.edNameRegis);
        edEmail=findViewById(R.id.edEmailRegis);
        edPhone=findViewById(R.id.edPhoneRegis);
        edAddress=findViewById(R.id.edAddressRegis);
        edPassword=findViewById(R.id.edPasswordRegis);
        edComfirm=findViewById(R.id.edRePasswordRegis);

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

            }
        });
        findViewById(R.id.tvBackToLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToLogin();
            }
        });
    }

    private void registerUser() {
        if(Validate()>0) {
            ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
            String name=edName.getText().toString();
            String email=edEmail.getText().toString();
            String phone=edPhone.getText().toString();
            String address=edAddress.getText().toString();
            String pass=edPassword.getText().toString();

            User user = new User(email, pass, name, phone, address);

            Call<ResponseBody> call = apiInterface.registerUser(user);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Xử lý thành công
                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        clearText();
                        backToLogin();

                    } else {
                        // Xử lý lỗi
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String errorMessage = jsonObject.getString("message");
                            Toast.makeText(RegisterActivity.this, "Registration fail: " + errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Xử lý lỗi kết nối
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", t.getMessage());
                }
            });
        }
    }

    private int Validate(){
        int check=1;
        String name=edName.getText().toString();
        String email=edEmail.getText().toString();
        String phone=edPhone.getText().toString();
        String address=edAddress.getText().toString();
        String pass=edPassword.getText().toString();
        String confirm=edComfirm.getText().toString();

        if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || pass.isEmpty() || confirm.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            check=-1;
        }else {
            if (pass.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters long!", Toast.LENGTH_SHORT).show();
                check = -1;
            }
            if (!confirm.equals(pass)) {
                Toast.makeText(RegisterActivity.this, "Confirm password does not match!", Toast.LENGTH_SHORT).show();
                check = -1;
            }
            if (!isValidEmail(email)) {
                Toast.makeText(RegisterActivity.this, "Invalid email format!", Toast.LENGTH_SHORT).show();
                check = -1;
            }
            if (!isValidPhone(phone)) {
                Toast.makeText(RegisterActivity.this, "Invalid phone number format!", Toast.LENGTH_SHORT).show();
                check = -1;
            }
        }
        return check;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhone(String phone) {
        String phoneRegex = "^\\d{10}$";
        return phone.matches(phoneRegex);
    }
    private void clearText(){
        edName.setText("");
        edEmail.setText("");
        edPhone.setText("");
        edAddress.setText("");
        edPassword.setText("");
        edComfirm.setText("");
    }
    private void backToLogin(){
        Intent iToLogin=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(iToLogin);
    }
}