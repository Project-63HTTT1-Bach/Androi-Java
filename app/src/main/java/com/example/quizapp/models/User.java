package com.example.quizapp.models;

public class User {
    private int userId;
    private String username;
    private String password;
    private String fullname;
    private String userCode;
    private String email;
    private String profilePicture;

    public User(int userId, String username, String password, String fullname, String userCode, String email, String profilePicture) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.userCode = userCode;
        this.email = email;
        this.profilePicture = profilePicture;
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

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
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
}
