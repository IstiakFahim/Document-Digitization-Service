package com.example.myapplication;

public class UserHelperClass {

    String username, email, pass, cpass, phone, uid;

    public UserHelperClass() {
    }

    public UserHelperClass(String username, String email, String pass, String cpass, String phone, String uid) {
        this.username = username;
        this.email = email;
        this.pass = pass;
        this.cpass = cpass;
        this.phone = phone;
        this.uid = uid;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCpass() {
        return cpass;
    }

    public void setCpass(String cpass) {
        this.cpass = cpass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
