package edu.fpt.lab4.utils;
import edu.fpt.lab4.models.ResetPasswordRequest;
import edu.fpt.lab4.models.ResetPasswordResponse;
import edu.fpt.lab4.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST("register")
    Call<ResponseBody> registerUser(@Body User user);

    @POST("login")
    Call<ResponseBody> loginUser(@Body User user);

    @PUT("user/email/{userEmail}")
    Call<User> updateUserByEmail(@Path("userEmail") String userEmail, @Body User user);

    // Gửi yêu cầu reset mật khẩu
    @POST("resetpassword/request")
    Call<ResetPasswordResponse> requestResetPassword(@Body ResetPasswordRequest request);

    // Xác nhận mã và đặt lại mật khẩu mới
    @POST("resetpassword/confirm")
    Call<ResetPasswordResponse> confirmResetPassword(@Body ResetPasswordRequest request);
}
