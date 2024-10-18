package com.example.kingpho.dto;

public class UserDTO {
    private String username;
    private String email;
    private String fullname;
    private String phone;
    private String address;
    private String avatar;

    public UserDTO(String username, String email, String fullname, String phone, String address, String avatar) {
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
