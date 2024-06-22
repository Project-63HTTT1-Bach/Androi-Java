package com.example.quizapp.Auth.repositories;

import android.content.Context;

import com.example.quizapp.Auth.models.User;
import com.example.quizapp.sqliteOpenHelper.SqliteOpenHelper;

import java.util.ArrayList;

public class UserRepository {
    private static ArrayList<User> userList = new ArrayList<>();
    private final SqliteOpenHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new SqliteOpenHelper(context);
        if (userList.isEmpty()) {
            userList = dbHelper.getAllUsers(); // Load from DB if the list is empty
        }
    }

    public static ArrayList<User> getUserList() {
        return userList;
    }

    public boolean checkUser(User u) {
        for (User user : userList) {
            if (user.getEmail().equals(u.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public boolean addUser(User u) {
        if (!checkUser(u)) {
            dbHelper.insertUser(u.getUsername(), u.getPassword(), u.getFullname(), u.getEmail(), u.getProfilePicture(), u.getBirthday(), u.getPhone());
            userList.add(u); // Add to the static list
            return true;
        } else {
            return false;
        }
    }

    public void removeUser(User u) {
        dbHelper.deleteUser(u.getUserId());
        userList.remove(u); // Remove from the static list
    }

    public User getUser(int userId) {
        for (User user : userList) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getAllUsers() {
        return userList;
    }

    public int getUserCount() {
        return userList.size();
    }

    public boolean updateUser(User u) {
        for (User user : userList) {
            if (user.getEmail().equals(u.getEmail())) {
                user.setPhone(u.getPhone());
                user.setBirthday(u.getBirthday());
                user.setProfilePicture(u.getProfilePicture());
                user.setFullname(u.getFullname());
                user.setPassword(u.getPassword());
                dbHelper.updateUser(u.getUserId(), u.getUsername(), u.getPassword(), u.getFullname(), u.getEmail(), u.getProfilePicture(), u.getPhone(), u.getBirthday());
                return true;
            }
        }
        return false;
    }
}
