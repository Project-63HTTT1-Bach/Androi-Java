package com.example.quizapp.models;

public class User {
    private String fullname;
    //    private String username;
    private String password;
    private String email;
    private String birthday;
    private String phone;

    public User(String email, String password, String birthday, String fullname, String phone) {
//        this.username = username;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.fullname = fullname;
        this.phone = phone;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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
}
