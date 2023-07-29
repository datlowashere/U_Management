package edu.fpt.lab4.models;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("resetToken")
    private String resetToken;

    @SerializedName("newPassword")
    private String newPassword;

    // Constructor cho yêu cầu reset mật khẩu
    public ResetPasswordRequest(String email) {
        this.email = email;
    }

    // Constructor cho xác nhận mã và đặt lại mật khẩu mới
    public ResetPasswordRequest(String email, String resetToken, String newPassword) {
        this.email = email;
        this.resetToken = resetToken;
        this.newPassword = newPassword;
    }
}