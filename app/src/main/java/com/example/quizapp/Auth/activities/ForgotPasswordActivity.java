package com.example.quizapp.Auth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.R;
import com.example.quizapp.Auth.Utils.GmailSender;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Button btnContinue;
    private LinearLayout btnBack;
    private TextInputEditText txtEmail;
    private FirebaseAuth mAuth;
    private String generatedOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        btnContinue = findViewById(R.id.btnContinue);
        txtEmail = findViewById(R.id.txtEmail);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    sendVerificationCode(email);
                }
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendVerificationCode(String email) {
        generatedOTP = generateOTP();
        String subject = "Your OTP Code";
        String body = "Your OTP code is: " + generatedOTP;

        GmailSender.sendEmail(email, subject, body);
        Intent intent = new Intent(ForgotPasswordActivity.this, OTPVerification.class);
        intent.putExtra("email", email);
        intent.putExtra("otp", generatedOTP);
        startActivity(intent);
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
