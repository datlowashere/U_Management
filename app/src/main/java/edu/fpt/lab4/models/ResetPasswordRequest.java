package edu.fpt.lab4.models;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("resetToken")
    private int resetToken;

    @SerializedName("newPassword")
    private String newPassword;

    public ResetPasswordRequest(String email) {
        this.email = email;
    }

    public ResetPasswordRequest(String email, int resetToken, String newPassword) {
        this.email = email;
        this.resetToken = resetToken;
        this.newPassword = newPassword;
    }
//
}