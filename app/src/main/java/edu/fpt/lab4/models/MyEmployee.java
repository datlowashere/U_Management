package edu.fpt.lab4.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyEmployee implements Serializable {

private String userId,email,name,gender,image,role,phone,address,_id;

    public MyEmployee() {
    }

    public MyEmployee(String userId, String email, String name, String gender, String image, String role, String phone, String address) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.image = image;
        this.role = role;
        this.phone = phone;
        this.address = address;
    }

    public MyEmployee(String userId, String email, String name, String gender, String image, String role, String phone, String address, String _id) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.image = image;
        this.role = role;
        this.phone = phone;
        this.address = address;
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
