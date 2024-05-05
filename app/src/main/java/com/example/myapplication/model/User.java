package com.example.myapplication.model;

public class User {
    private Integer Id;
    private String Firstname;
    private String Lastname;

    public User(Integer id, String firstname, String lastname) {
        Id = id;
        Firstname = firstname;
        Lastname = lastname;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }
}
