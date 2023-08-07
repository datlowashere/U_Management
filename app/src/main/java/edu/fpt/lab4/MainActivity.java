package edu.fpt.lab4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;



import edu.fpt.lab4.activities.LoginActivity;
import edu.fpt.lab4.fragments.EmployeeFragment;
import edu.fpt.lab4.fragments.HomeFragment;
import edu.fpt.lab4.fragments.SettingFragment;
import edu.fpt.lab4.models.User;
import edu.fpt.lab4.utils.ApiInterface;
import edu.fpt.lab4.utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FrameLayout frameLayout;
    private NavigationView navigationView;
    private View mHeaderView;
    private TextView tvUser;
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
        String id=intent.getStringExtra("id");
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolBar);
        frameLayout=findViewById(R.id.frameLayout);
        navigationView=findViewById(R.id.navigationView);
        drawerLayout=findViewById(R.id.drawer_layoutT);

        mHeaderView=navigationView.getHeaderView(0);
        tvUser=mHeaderView.findViewById(R.id.tvUserNav);


        tvUser.setText(getName());


        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.menu2);
        ColorDrawable colorDrawable = new ColorDrawable(getColor(R.color.colorPrimary));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);


        replaceFragment(new HomeFragment());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager manager=getSupportFragmentManager();

                if(item.getItemId()==R.id.navHome){
                    replaceFragment(new HomeFragment());

                }
                if(item.getItemId()==R.id.navEmp){
                    replaceFragment(new EmployeeFragment());

                }
                if(item.getItemId()==R.id.navSetting){
                    replaceFragment(new SettingFragment());

                }
                if(item.getItemId()==R.id.navLogout){
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                toolbar.setTitle(item.getTitle());
                return true;
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
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


    public  String getBossID(){
        Intent intent = getIntent();
        String idBoss = intent.getStringExtra("id");
        return idBoss;
    }
    private String getName(){
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        return name;
    }
    private String getEmail(){
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        return email;
    }
    private String getPhone(){
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        return phone;
    }
    private String getPassword(){
        Intent intent = getIntent();

        String password = intent.getStringExtra("password");
        return password;
    }
    private String getAddress(){
        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        return address;

    }
}