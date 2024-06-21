package com.example.quizapp.Auth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeProfileActivity extends AppCompatActivity {
    private ImageView btnBack;
    private TextView tvEmail;
    private EditText etFullname, etBirthday, etPhone;
    private LinearLayout btnSave;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave = findViewById(R.id.btnSave);
        etFullname = findViewById(R.id.etFullname2);
        etBirthday = findViewById(R.id.etBirthday2);
        etPhone = findViewById(R.id.etPhone2);
        tvEmail = findViewById(R.id.tvEmail2);

        database = FirebaseDatabase.getInstance();

        // Nhận email từ Intent
        userEmail = getIntent().getStringExtra("userEmail");

        loadUserData(userEmail);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });
    }

    private void loadUserData(String email) {
        userRef = database.getReference("users");
        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String fullName = userSnapshot.child("fullname").getValue(String.class);
                        String birthday = userSnapshot.child("birthday").getValue(String.class);
                        String phone = userSnapshot.child("phone").getValue(String.class);
                        String email = userSnapshot.child("email").getValue(String.class);

                        etFullname.setText(fullName);
                        etBirthday.setText(birthday);
                        etPhone.setText(phone);
                        tvEmail.setText(email);
                    }
                } else {
                    // Handle trường hợp không tìm thấy người dùng
                    Log.e("ChangeProfileActivity", "User not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle lỗi
                Log.e("ChangeProfileActivity", "Database error: ", error.toException());
            }
        });
    }

    private void updateUserData() {
        String newFullName = etFullname.getText().toString().trim();
        String newBirthday = etBirthday.getText().toString().trim();
        String newPhone = etPhone.getText().toString().trim();
        String email = tvEmail.getText().toString().trim();

        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        if (userId != null) {
                            userRef.child(userId).child("fullname").setValue(newFullName);
                            userRef.child(userId).child("birthday").setValue(newBirthday);
                            userRef.child(userId).child("phone").setValue(newPhone);
                            Toast.makeText(ChangeProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                            // Quay lại SettingActivity với dữ liệu đã cập nhật
                            Intent intent = new Intent(ChangeProfileActivity.this, SettingActivity.class);
                            intent.putExtra("userEmail", userEmail);
                            startActivity(intent);
                            finish();
                        }
                    }
                } else {
                    // Handle trường hợp không tìm thấy người dùng
                    Log.e("ChangeProfileActivity", "User not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle lỗi
                Log.e("ChangeProfileActivity", "Database error: ", error.toException());
            }
        });
    }
}
