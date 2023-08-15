package edu.fpt.lab4.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Cache;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.fpt.lab4.MainActivity;
import edu.fpt.lab4.R;
import edu.fpt.lab4.activities.AddEmployeeActivity;
import edu.fpt.lab4.adapters.MyEmployeeAdapter;
import edu.fpt.lab4.models.MyEmployee;
import edu.fpt.lab4.utils.ApiInterface;
import edu.fpt.lab4.utils.RetrofitClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EmployeeFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;

    private ListView lv;
    private TextView tvMsg;
    private MyEmployeeAdapter employeeAdapter;
    private List<MyEmployee> employeeList;
    private float initialTouchY;
    private boolean swipeStarted;
    private final int SWIPE_LIMIT = 30;
    public EmployeeFragment() {
        // Required empty public constructor
    }

    public static EmployeeFragment newInstance(String param1, String param2) {
        EmployeeFragment fragment = new EmployeeFragment();
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
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_employee, container, false);
        lv=view.findViewById(R.id.lvEmp);
        tvMsg=view.findViewById(R.id.tvNoEmployee);



         employeeList = new ArrayList<>();
         employeeAdapter = new MyEmployeeAdapter(getContext(), employeeList);
         lv.setAdapter(employeeAdapter);


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MyEmployee selectedEmployee = employeeList.get(position);
                String employeeId = selectedEmployee.get_id();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Options");
                builder.setItems(new CharSequence[]{"Update", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent iUpdateEmp =new Intent(getContext(),AddEmployeeActivity.class);
                                iUpdateEmp.putExtra("employeeData",  selectedEmployee);
                                startActivity(iUpdateEmp);
                                break;
                            case 1:
                                deleteEmployeeById(employeeId);
                                break;
                        }
                    }
                });
                builder.show();
                return true;
            }
        });





        view.findViewById(R.id.fabEmp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iFabEmp=new Intent(getContext(),AddEmployeeActivity.class);
                iFabEmp.putExtra("boss_id", getBossID());
                startActivity(iFabEmp);
            }
        });
        getEmployeesByUserId(getBossID());

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEmployeesByUserId(getBossID());
            }
        });

        swipeRefreshLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialTouchY = event.getY();
                        swipeStarted = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (swipeStarted) {
                            float distance = event.getY() - initialTouchY;
                            if (distance > SWIPE_LIMIT) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        swipeStarted = false;
                        break;
                }
                return false;
            }
        });

        return view;
    }




    private void getEmployeesByUserId(String userId) {
        ApiInterface apiService = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<List<MyEmployee>> call = apiService.getAllEmployees(userId);
        call.enqueue(new Callback<List<MyEmployee>>() {
            @Override
            public void onResponse(Call<List<MyEmployee>> call, Response<List<MyEmployee>> response) {
                if (response.isSuccessful()) {
                    employeeList.clear();
                    employeeList.addAll(response.body());
                    employeeAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    Toast.makeText(getContext(), "Failed to get employees.", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onFailure(Call<List<MyEmployee>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get employees.", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void deleteEmployeeById(String employeeId){
        ApiInterface apiService = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<Void> call = apiService.deleteEmployee(employeeId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    getEmployeesByUserId(getBossID());
                    Toast.makeText(getContext(), "Employee has been deleted.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Failed to delete employee.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to delete employee.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private String getBossID() {
        MainActivity activity = (MainActivity) getActivity();
        String idBoss = activity.getBossID();
        return idBoss;
    }

}