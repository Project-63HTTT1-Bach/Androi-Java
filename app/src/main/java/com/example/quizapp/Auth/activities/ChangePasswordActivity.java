package com.example.quizapp.Auth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePasswordActivity extends AppCompatActivity {
    private Button btnContinue;
    private LinearLayout btnBack;
    private TextInputEditText txtPassword, txtConfirmPassword;
    private ImageView ivRequirement1, ivRequirement2, ivRequirement3, ivRequirement4;

    private boolean isPasswordValid = false;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnContinue = findViewById(R.id.btnContinue);
        btnBack = findViewById(R.id.btnBack);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);

        ivRequirement1 = findViewById(R.id.ivRequirement1);
        ivRequirement2 = findViewById(R.id.ivRequirement2);
        ivRequirement3 = findViewById(R.id.ivRequirement3);
        ivRequirement4 = findViewById(R.id.ivRequirement4);

        email = getIntent().getStringExtra("email");

        txtPassword.addTextChangedListener(passwordTextWatcher);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = txtPassword.getText().toString().trim();
                String confirmPassword = txtConfirmPassword.getText().toString().trim();

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
                } else if (!isPasswordValid) {
                    Toast.makeText(ChangePasswordActivity.this, "Password does not meet requirements!", Toast.LENGTH_LONG).show();
                } else {
                    updatePasswordInDatabase(email, password);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String password = s.toString();
            isPasswordValid = validatePassword(password);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private boolean validatePassword(String password) {
        boolean isAtLeast8 = password.length() >= 8;
        boolean hasLowerCase = password.matches(".*[a-z].*");
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*\\d.*");

        ivRequirement1.setVisibility(isAtLeast8 ? View.VISIBLE : View.INVISIBLE);
        ivRequirement2.setVisibility(hasLowerCase ? View.VISIBLE : View.INVISIBLE);
        ivRequirement3.setVisibility(hasNumber ? View.VISIBLE : View.INVISIBLE);
        ivRequirement4.setVisibility(hasUpperCase ? View.VISIBLE : View.INVISIBLE);

        return isAtLeast8 && hasLowerCase && hasUpperCase && hasNumber;
    }

    private void updatePasswordInDatabase(String email, String password) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        userSnapshot.getRef().child("password").setValue(password);
                    }
                    Toast.makeText(ChangePasswordActivity.this, "Password updated successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "User not found!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChangePasswordActivity.this, "Failed to update password!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
