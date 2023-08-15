package edu.fpt.lab4.models;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordResponse {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
//
}