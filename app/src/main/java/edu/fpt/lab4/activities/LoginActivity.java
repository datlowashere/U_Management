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

import edu.fpt.lab4.MainActivity;
import edu.fpt.lab4.R;
import edu.fpt.lab4.models.User;
import edu.fpt.lab4.utils.ApiInterface;
import edu.fpt.lab4.utils.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText edEmail,edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        edEmail=findViewById(R.id.edUser);
        edPassword=findViewById(R.id.edPass);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();

            }
        });
        findViewById(R.id.tvRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iToRegister=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(iToRegister);
            }
        });

        findViewById(R.id.tvForgotPass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iforgot=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(iforgot);
            }
        });
    }

    private void loginUser() {
        if(Validate()>0) {
            ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

            User user = new User(); // Tạo đối tượng User với thông tin người dùng
            String email=edEmail.getText().toString();
            String pass=edPassword.getText().toString();

            user.setEmail(email);
            user.setPassword(pass);


            Call<ResponseBody> call = apiInterface.loginUser(user);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            Toast.makeText(LoginActivity.this, "Logging Successful", Toast.LENGTH_SHORT).show();
                            String email = edEmail.getText().toString();
                            String name = "";
                            String password="";
                            String address="";
                            String phone="";

                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.has("email")) {
                                email = jsonObject.getString("email");
                            }
                            if (jsonObject.has("name")) {
                                name = jsonObject.getString("name");
                            }
                            if (jsonObject.has("password")) {
                                password = jsonObject.getString("password");
                            }
                            if (jsonObject.has("address")) {
                                address = jsonObject.getString("address");
                            }
                            if (jsonObject.has("phone")) {
                                phone = jsonObject.getString("phone");
                            }

                            Log.i("NAME USER", name);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("name", name);
                            intent.putExtra("password", password);
                            intent.putExtra("address", address);
                            intent.putExtra("phone", phone);

                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(LoginActivity.this, "Logging Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                    Log.i("ERORR",""+t.getMessage());
                }
            });
        }
    }

    private int Validate(){
        int check=1;
        String email=edEmail.getText().toString();
        String pass=edPassword.getText().toString();

        if( email.isEmpty() ||pass.isEmpty()){
            Toast.makeText(LoginActivity.this,"Please fill in all the required information!",Toast.LENGTH_SHORT).show();
            check=-1;
        }
        return check;
    }

}