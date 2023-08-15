package edu.fpt.lab4.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.fpt.lab4.MainActivity;
import edu.fpt.lab4.R;
import edu.fpt.lab4.activities.ChangeInformationActivity;
import edu.fpt.lab4.activities.ChangePasswordActivity;
import edu.fpt.lab4.models.User;
import edu.fpt.lab4.utils.ApiInterface;
import edu.fpt.lab4.utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {
    private CircleImageView img;
    private TextView tvName,tvEMail;


    public SettingFragment() {
    }


    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_setting, container, false);
        img=view.findViewById(R.id.imgUserSetting);
        tvName=view.findViewById(R.id.tvNameSetting);
        tvEMail=view.findViewById(R.id.tvEmailSetting);




        view.findViewById(R.id.btnChangeInfor).setOnClickListener(v -> {
            Intent iChangeInfor=new Intent(getContext(), ChangeInformationActivity.class);
            iChangeInfor.putExtra("id",getID());
            startActivity(iChangeInfor);
        });
        view.findViewById(R.id.btnChangePass).setOnClickListener(v->{
            Intent iChangePass=new Intent(getContext(), ChangePasswordActivity.class);
            iChangePass.putExtra("id",getID());
            startActivity(iChangePass);
        });
        getUserInformation();

        return view;
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

                        byte[] decodedString = Base64.decode(user.getImg(), Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        img.setImageBitmap(decodedBitmap);

                        tvName.setText(user.getName());
                        tvEMail.setText(user.getEmail());

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

    private String getID(){
        MainActivity activity=(MainActivity) getActivity();
        String id=activity.getBossID();
        return id;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInformation();
    }
}