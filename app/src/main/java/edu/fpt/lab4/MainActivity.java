package edu.fpt.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;



import edu.fpt.lab4.activities.LoginActivity;
import edu.fpt.lab4.models.User;
import edu.fpt.lab4.utils.ApiInterface;
import edu.fpt.lab4.utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String name = intent.getStringExtra("name");
        String password = intent.getStringExtra("password");
        String address = intent.getStringExtra("address");
        String phone = intent.getStringExtra("phone");
        setContentView(R.layout.activity_main);

    }

//    private void openDialog(){
//        dialog=new Dialog(MainActivity.this);
//        dialog.setContentView(R.layout.layout_change_information);
//
//
//        edName=dialog.findViewById(R.id.edChangeName);
//        edAddress=dialog.findViewById(R.id.edChangeAddress);
//        edPhone=dialog.findViewById(R.id.edChangePhone);
//        edPass=dialog.findViewById(R.id.edChangePass);
//        edComfirm=dialog.findViewById(R.id.edChangePasComfirm);
//
//        edName.setText(getName());
//        edAddress.setText(getAddress());
//        edPhone.setText(getPhone());
//
//
//        dialog.findViewById(R.id.btnSaveChange).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(edComfirm.getText().toString().isEmpty()){
//                    Toast.makeText(MainActivity.this,"Please fill all the field",Toast.LENGTH_SHORT).show();
//
//                }else if(!edComfirm.getText().toString().equals(edPass.getText().toString())){
//                    Toast.makeText(MainActivity.this,"Comfirm pass is incorrect!",Toast.LENGTH_SHORT).show();
//
//                }else{
//                    User updatedUser = new User();
//                    updatedUser.setEmail(getEmail());
//                    updatedUser.setPassword(edPass.getText().toString());
//                    updatedUser.setName(edName.getText().toString());
//                    updatedUser.setPhone(edPhone.getText().toString());
//                    updatedUser.setAddress(edAddress.getText().toString());
//                    updateUserByEmail(getEmail(),updatedUser);
//                }
//            }
//        });
//        dialog.findViewById(R.id.btnCancelChange).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//
//
//    }

//    private void updateUserByEmail(String email, User updatedUser) {
//        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
//
//        Call<User> call = apiInterface.updateUserByEmail(email, updatedUser);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()) {
//                    User updatedUser = response.body();
//                    Toast.makeText(MainActivity.this, "Thông tin người dùng đã được cập nhật", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Cập nhật thông tin người dùng thất bại", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                // Xử lý lỗi kết nối
//                Toast.makeText(MainActivity.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
//                Log.e("ERROR", t.getMessage());
//            }
//        });
//    }
//
//    private String getName(){
//        Intent intent = getIntent();
//        String name = intent.getStringExtra("name");
//        return name;
//    }
//    private String getEmail(){
//        Intent intent = getIntent();
//        String email = intent.getStringExtra("email");
//        return email;
//    }
//    private String getPhone(){
//        Intent intent = getIntent();
//        String phone = intent.getStringExtra("phone");
//        return phone;
//    }
//    private String getPassword(){
//        Intent intent = getIntent();
//
//        String password = intent.getStringExtra("password");
//        return password;
//    }
//    private String getAddress(){
//        Intent intent = getIntent();
//        String address = intent.getStringExtra("address");
//        return address;
//
//    }
}