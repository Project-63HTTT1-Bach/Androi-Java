package com.example.quizapp.Auth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.R;
import com.example.quizapp.repositories.UserRepository;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText tietEmail, tietPassword, tietConfirmPassword;
    private TextView btnLogin;
    private Button btnOnRegister;
    private UserRepository userRepository;

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

        tietEmail = (TextInputEditText) findViewById(R.id.txtEmail);
        tietPassword = (TextInputEditText) findViewById(R.id.txtPassword);
        tietConfirmPassword = (TextInputEditText) findViewById(R.id.txtConfirmPassword);
        btnOnRegister = (Button) findViewById(R.id.btnOnRegister);

        userRepository = new UserRepository(this);


        btnOnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tietEmail.getText().toString().trim();
                String password = tietPassword.getText().toString().trim();
                String confirmPassword = tietConfirmPassword.getText().toString().trim();

                Intent intent = new Intent(RegisterActivity.this, OTPVerification.class);

                intent.putExtra("email", email);

                startActivity(intent);


//
//                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
//                    Toast.makeText(RegisterActivity.this, "OnRegiter fail!!!!", Toast.LENGTH_LONG).show();
//                } else if (password.equals(confirmPassword)) {
//                    User user = new User(email, password);
//                    if (userRepository.addUser(user)) {
//                        Toast.makeText(RegisterActivity.this, "OnRegiter success!!!!", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                    } else {
//                        Toast.makeText(RegisterActivity.this, "OnRegiter fail!!!!", Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    Toast.makeText(RegisterActivity.this, "OnRegiter fail!!!!", Toast.LENGTH_LONG).show();
//                }
            }
        });

        btnLogin = (TextView) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}