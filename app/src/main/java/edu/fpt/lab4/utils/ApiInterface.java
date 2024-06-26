package edu.fpt.lab4.utils;
import java.util.List;

import edu.fpt.lab4.models.MyEmployee;
import edu.fpt.lab4.models.ResetPasswordRequest;
import edu.fpt.lab4.models.ResetPasswordResponse;
import edu.fpt.lab4.models.User;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
//    Register
    @POST("register")
    Call<ResponseBody> registerUser(@Body User user);

//    Login
    @POST("login")
    Call<ResponseBody> loginUser(@Body User user);


// Get user
    @GET("/users/{id}")
    Call<User> getUserById(@Path("id") String userId);
//   Update user
    @PUT("users/{id}")
    Call<User> updateUserById(@Path("id") String userId, @Body User user);


//    Request reset password
    @POST("resetpassword/request")
    Call<ResetPasswordResponse> requestResetPassword(@Body ResetPasswordRequest request);


//    Comfirm reset password
    @POST("resetpassword/confirm")
    Call<ResetPasswordResponse> confirmResetPassword(@Body ResetPasswordRequest request);

    // Create
    @POST("/employees")
    Call<Void> createEmployee(@Body MyEmployee employee);
    // Read all employees of a boss (user)
    @GET("/employees/{userId}")
    Call<List<MyEmployee>> getAllEmployees(@Path("userId") String userId);


    // Update an employee by ID
    @PUT("/employees/{employeeId}")
    Call<MyEmployee> updateEmployee(@Path("employeeId") String employeeId, @Body MyEmployee employee);

    // Delete an employee by ID
    @DELETE("/employees/{employeeId}")
    Call<Void> deleteEmployee(@Path("employeeId") String employeeId);
}
