package edu.fpt.lab4.activities;

import static edu.fpt.lab4.utils.RetrofitClient.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.fpt.lab4.R;
import edu.fpt.lab4.models.ResetPasswordRequest;
import edu.fpt.lab4.models.ResetPasswordResponse;
import edu.fpt.lab4.utils.ApiInterface;
import edu.fpt.lab4.utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputEditText edCode, edNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Intent intent = getIntent();
        String iemail = intent.getStringExtra("email");

        edCode = findViewById(R.id.edCode);
        edNewPassword = findViewById(R.id.edNewPassword);

        Button btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = iemail;
                Log.i("EMAIL USERR:",""+email);
                int resetToken = Integer.parseInt(edCode.getText().toString());
                String newPassword = edNewPassword.getText().toString();
                Log.i("RESET TOKEN",""+resetToken);

                ResetPasswordRequest request = new ResetPasswordRequest(email, resetToken, newPassword);
                ApiInterface apiInterface = getRetrofitInstance().create(ApiInterface.class);
                Call<ResetPasswordResponse> call = apiInterface.confirmResetPassword(request);
                call.enqueue(new Callback<ResetPasswordResponse>() {
                    @Override
                    public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "Password has been reset", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                            finish();
                        } else {
                            String errorBody = null;
                            try {
                                errorBody = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(errorBody);
                                String errorMessage = jsonObject.getString("message");
                                Toast.makeText(ResetPasswordActivity.this, "Password reset failed"+errorMessage, Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                        Toast.makeText(ResetPasswordActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}