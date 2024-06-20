package com.example.quizapp.Auth.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText tietEmail, tietPassword, tietConfirmPassword;
    private TextView btnLogin;
    private Button btnOnRegister;
    private UserRepository userRepository;
    private FirebaseDatabase database;
    private DatabaseReference idRef;

    // Định dạng email
    private static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    // Định dạng mật khẩu
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         // ít nhất một chữ số
            "(?=.*[a-z])" +         // ít nhất một chữ cái viết thường
            "(?=.*[A-Z])" +         // ít nhất một chữ cái viết hoa
            "(?=\\S+$)" +           // không khoảng trắng
            ".{8,}" +               // ít nhất 8 ký tự
            "$");

    private ImageView ivRequirement1, ivRequirement2, ivRequirement3, ivRequirement4;

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
        ivRequirement1 = findViewById(R.id.ivRequirement1);
        ivRequirement2 = findViewById(R.id.ivRequirement2);
        ivRequirement3 = findViewById(R.id.ivRequirement3);
        ivRequirement4 = findViewById(R.id.ivRequirement4);

        userRepository = new UserRepository(this);
        database = FirebaseDatabase.getInstance();
        idRef = database.getReference("currentId");

        tietPassword.addTextChangedListener(passwordWatcher);

        btnOnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tietEmail.getText().toString().trim();
                String password = tietPassword.getText().toString().trim();
                String confirmPassword = tietConfirmPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all the fields!", Toast.LENGTH_LONG).show();
                } else if (!Pattern.matches(emailPattern, email)) {
                    Toast.makeText(RegisterActivity.this, "Email không hợp lệ", Toast.LENGTH_LONG).show();
                } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không đủ điều kiện", Toast.LENGTH_LONG).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
                } else {
                    // Kiểm tra xem email đã tồn tại trong Firebase chưa
                    DatabaseReference userRef = database.getReference("users");
                    userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // Email đã tồn tại, hiển thị thông báo lỗi
                                Toast.makeText(RegisterActivity.this, "Email đã tồn tại, vui lòng kiểm tra lại!", Toast.LENGTH_LONG).show();
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

    private TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String password = s.toString();

            // Kiểm tra độ dài mật khẩu
            if (password.length() >= 8) {
                ivRequirement1.setVisibility(View.VISIBLE);
            } else {
                ivRequirement1.setVisibility(View.INVISIBLE);
            }

            // Kiểm tra có ít nhất một chữ cái viết thường
            if (password.matches(".*[a-z].*")) {
                ivRequirement2.setVisibility(View.VISIBLE);
            } else {
                ivRequirement2.setVisibility(View.INVISIBLE);
            }

            // Kiểm tra có ít nhất một chữ cái viết hoa
            if (password.matches(".*[A-Z].*")) {
                ivRequirement3.setVisibility(View.VISIBLE);
            } else {
                ivRequirement3.setVisibility(View.INVISIBLE);
            }

            // Kiểm tra có ít nhất một chữ số
            if (password.matches(".*[0-9].*")) {
                ivRequirement4.setVisibility(View.VISIBLE);
            } else {
                ivRequirement4.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Không có hành động nào cần thực hiện sau khi thay đổi
        }
    };
}
