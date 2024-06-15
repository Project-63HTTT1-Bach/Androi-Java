package com.example.quizapp.repositories;

import android.content.Context;

import java.util.ArrayList;

import com.example.quizapp.models.User;
import com.example.quizapp.sqliteOpenHelper.SqliteOpenHelper;

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

    public boolean checkExistedUser(User u) {
        for (User user : userList) {
            if (user.getFullname().equals(u.getFullname())) {
                return true;
            }
        }
        return false;
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
        if (!checkExistedUser(u)) {
            dbHelper.insertUser(u.getUsername(), u.getPassword(), u.getFullname(), u.getUserCode(), u.getEmail(), u.getProfilePicture());
            userList.add(u); // Add to the static list
            return true;
        } else {
            return false;
        }
    }

    //    public void removeUser(User u) {
//        dbHelper.deleteUser(u.getUsername());
//        userList.remove(u); // Remove from the static list
//    }
//
    public User getUser(String email) {
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
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
                user.setProfilePicture(u.getProfilePicture());
                user.setFullname(u.getFullname());
                user.setPassword(u.getPassword());
                dbHelper.updateUser(u.getUserId(), u.getUsername(), u.getPassword(), u.getFullname(), u.getUserCode(), u.getEmail(), u.getProfilePicture());
                return true;
            }
        }
        return false;
    }
}
