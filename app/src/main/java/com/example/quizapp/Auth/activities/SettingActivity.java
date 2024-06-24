package com.example.quizapp.Auth.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {
    private TextView tvFullName2, tvBirthday2, tvPhone2, tvEmail2;
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    private LinearLayout btnEdit, btnLogout;
    private ImageView ivUserAvatar, btnBack;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        btnEdit = findViewById(R.id.btnEdit);
        btnBack = findViewById(R.id.btnBack);

        ivUserAvatar = findViewById(R.id.ivUseravatar);
        tvFullName2 = findViewById(R.id.tvFullname2);
        tvBirthday2 = findViewById(R.id.tvBirthday2);
        tvPhone2 = findViewById(R.id.tvPhone2);
        tvEmail2 = findViewById(R.id.tvEmail2);
        btnLogout = findViewById(R.id.btnLogout);

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

        // Lấy email từ Intent
        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("userEmail");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Quay lại activity trước đó
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangeProfileActivity.class);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
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
                        String profilePicture = userSnapshot.child("profilePicture").getValue(String.class);

                        tvFullName2.setText(fullName);
                        tvBirthday2.setText(birthday);
                        tvPhone2.setText(phone);
                        tvEmail2.setText(email);

                        // Chuyển đổi chuỗi Base64 thành Bitmap và gán vào ImageView
                        if (profilePicture != null && !profilePicture.isEmpty()) {
                            Bitmap bitmap = decodeBase64(profilePicture);
                            ivUserAvatar.setImageBitmap(bitmap);
                        }
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

    private Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_confirm_logout, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button buttonNo = dialogView.findViewById(R.id.btnNo);
        Button buttonYes = dialogView.findViewById(R.id.btnYes);

        buttonNo.setOnClickListener(v -> dialog.dismiss());

        buttonYes.setOnClickListener(v -> {
            logoutUser();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void logoutUser() {
        mAuth.signOut();
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
