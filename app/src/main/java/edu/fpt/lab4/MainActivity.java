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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
    private ImageView imgUser;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolBar);
        frameLayout=findViewById(R.id.frameLayout);
        navigationView=findViewById(R.id.navigationView);
        drawerLayout=findViewById(R.id.drawer_layoutT);

        mHeaderView=navigationView.getHeaderView(0);

        imgUser=mHeaderView.findViewById(R.id.imgUserNav);
        tvUser=mHeaderView.findViewById(R.id.tvUserNav);




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
        getUserInformation();


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


    private void getUserInformation(){
        ApiInterface apiService = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<User> call = apiService.getUserById(getBossID());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        byte[] decodedString = Base64.decode(user.getImg(), Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imgUser.setImageBitmap(decodedBitmap);

                        tvUser.setText(user.getName());

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

    public  String getBossID(){
        Intent intent = getIntent();
        String idBoss = intent.getStringExtra("id");
        return idBoss;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInformation();
    }
}