package com.example.quizapp.Auth.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.R;
import com.example.quizapp.Auth.models.User;
import com.example.quizapp.Auth.repositories.UserRepository;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText tietEmail, tietPassword, tietConfirmPassword;
    private TextView btnLogin;
    private Button btnOnRegister;
    private UserRepository userRepository;
    private FirebaseDatabase database;
    private DatabaseReference idRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tietEmail = findViewById(R.id.txtEmail);
        tietPassword = findViewById(R.id.txtPassword);
        tietConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnOnRegister = findViewById(R.id.btnOnRegister);

        userRepository = new UserRepository(this);
        database = FirebaseDatabase.getInstance();
        idRef = database.getReference("currentId");

        btnOnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tietEmail.getText().toString().trim();
                String password = tietPassword.getText().toString().trim();
                String confirmPassword = tietConfirmPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all the fields!", Toast.LENGTH_LONG).show();
                } else if (password.equals(confirmPassword)) {
                    // Kiểm tra xem email đã tồn tại trong Firebase chưa
                    DatabaseReference userRef = database.getReference("users");
                    userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // Email đã tồn tại, cập nhật mật khẩu
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    String userId = userSnapshot.getKey();
                                    userRef.child(userId).child("password").setValue(password);
                                }
                                Toast.makeText(RegisterActivity.this, "Password updated successfully!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            } else {
                                // Tăng giá trị ID và thêm người dùng mới
                                idRef.runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                        Integer currentId = currentData.getValue(Integer.class);
                                        if (currentId == null) {
                                            currentData.setValue(1);
                                        } else {
                                            currentData.setValue(currentId + 1);
                                        }
                                        return Transaction.success(currentData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot currentData) {
                                        if (committed) {
                                            Integer newId = currentData.getValue(Integer.class);
                                            if (newId != null) {
                                                // Giá trị mặc định cho các trường mới
                                                String defaultBirthday = "01/01/2000";
                                                String defaultPhone = "123456789";

                                                // Lấy hình ảnh từ drawable và chuyển đổi thành chuỗi Base64
                                                String profilePictureBase64 = getImageBase64();

                                                User user = new User(newId, email, password, email, email, profilePictureBase64, defaultBirthday, defaultPhone);

                                                // Lưu người dùng mới vào Firebase
                                                HashMap<String, Object> map = new HashMap<>();
                                                map.put("id", newId);
                                                map.put("username", email);
                                                map.put("password", password); // Mật khẩu từ người dùng nhập
                                                map.put("fullname", email);
                                                map.put("email", email);
                                                map.put("profilePicture", profilePictureBase64);
                                                map.put("birthday", defaultBirthday);
                                                map.put("phone", defaultPhone);
                                                database.getReference().child("users").child(newId.toString()).setValue(map);

                                                // Lưu người dùng mới vào SQLite
                                                if (!userRepository.checkUser(user)) {
                                                    userRepository.addUser(user);
                                                }

                                                Toast.makeText(RegisterActivity.this, "Registration success!", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            }
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("RegisterActivity", "Lỗi khi đọc dữ liệu người dùng", error.toException());
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getImageBase64() {
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.user_avatar)).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
