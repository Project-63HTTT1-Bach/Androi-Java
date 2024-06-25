package com.example.quizapp.Auth.models;

public class User {
    private int userId;
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String profilePicture;
    private String phone;
    private String birthday;
    private String createAt;
    public User(int userId, String username, String password, String fullname, String email, String profilePicture, String phone, String birthday, String createAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.phone = phone;
        this.birthday = birthday;
        this.email = email;
        this.profilePicture = profilePicture;
        this.createAt = createAt;
    }

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
