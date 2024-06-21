package com.example.quizapp.Auth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {
    private TextView tvFullName2, tvBirthday2, tvPhone2, tvEmail2;
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    private LinearLayout btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btnEdit = findViewById(R.id.btnEdit);

        tvFullName2 = findViewById(R.id.tvFullname2);
        tvBirthday2 = findViewById(R.id.tvBirthday2);
        tvPhone2 = findViewById(R.id.tvPhone2);
        tvEmail2 = findViewById(R.id.tvEmail2);

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

        // Lấy email từ Intent
        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("userEmail");

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangeProfileActivity.class);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
            }
        });

        if (userEmail != null) {
            loadUserData(userEmail);
        } else {
            // Handle trường hợp không có email được truyền
        }
    }

    private void loadUserData(String email) {
        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String fullName = userSnapshot.child("fullname").getValue(String.class);
                        String birthday = userSnapshot.child("birthday").getValue(String.class);
                        String phone = userSnapshot.child("phone").getValue(String.class);
                        String email = userSnapshot.child("email").getValue(String.class);

                        tvFullName2.setText(fullName);
                        tvBirthday2.setText(birthday);
                        tvPhone2.setText(phone);
                        tvEmail2.setText(email);
                    }
                } else {
                    // Handle trường hợp không tìm thấy người dùng
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle lỗi
            }
        });
    }
}
