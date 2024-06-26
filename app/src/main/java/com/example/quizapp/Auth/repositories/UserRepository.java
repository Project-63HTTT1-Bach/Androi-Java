package com.example.quizapp.Auth.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.quizapp.Auth.models.User;
import com.example.quizapp.sqliteOpenHelper.SqliteOpenHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserRepository {
    private static ArrayList<User> userList = new ArrayList<>();
    private final SqliteOpenHelper dbHelper;
    private DatabaseReference userRef;

    public UserRepository(Context context) {
        dbHelper = new SqliteOpenHelper(context);
        userRef = FirebaseDatabase.getInstance().getReference("users");
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
            dbHelper.insertUser(u.getUsername(), u.getPassword(), u.getFullname(), u.getEmail(), u.getProfilePicture(), u.getBirthday(), u.getPhone(), u.getCreateAt());
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

    public int getUserId(String email) {
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                return user.getUserId();
            }
        }
        return -1;
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
                dbHelper.updateUser(u.getUserId(), u.getUsername(), u.getPassword(), u.getFullname(), u.getEmail(), u.getProfilePicture(), u.getBirthday(), u.getPhone(), u.getCreateAt());
                return true;
            }
        }
        return false;
    }

    public void importUsersFromFirebase() {
        dbHelper.deleteAllUsers();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setUserId(userSnapshot.child("id").getValue(Integer.class));
                        try {
                            addUser(user); // Sử dụng phương thức addUser thay vì chèn trực tiếp vào cơ sở dữ liệu
                        } catch (Exception e) {
                            Log.e("UserRepository", "Error inserting user: " + user.getEmail(), e);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("UserRepository", "Error reading users from Firebase", error.toException());
            }
        });
    }
}
