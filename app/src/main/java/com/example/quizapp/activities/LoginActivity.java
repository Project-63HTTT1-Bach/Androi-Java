package com.example.quizapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.R;
import com.example.quizapp.models.User;
import com.example.quizapp.repositories.UserRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText tietEmail, tietPassword;
    private UserRepository userRepository;
    private Button btnOnLogin;
    private LinearLayout btnOnLoginGoogle, btnOnLoginGithub;
    private TextView btnRegiter;
    private TextView btnForgotPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tietEmail = findViewById(R.id.txtEmail);
        tietPassword = findViewById(R.id.txtPassword);
        btnOnLogin = findViewById(R.id.btnOnLogin);
        btnRegiter = (TextView) findViewById(R.id.btnRegister);
        btnOnLoginGoogle = findViewById(R.id.btnOnLoginGoogle);
        btnOnLoginGithub = findViewById(R.id.btnOnLoginGithub);

        userRepository = new UserRepository(this);
        initData();

        ArrayList<User> userList = userRepository.getAllUsers();
        for (User user : userList) {
            Log.d("UserRepository", "User: " + user.getFullname() + ", Email: " + user.getEmail() + ", Password: " + user.getPassword() + ", Phone: " + user.getPhone() + ", Birthday: " + user.getBirthday());
        }
        btnOnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = tietEmail.getText().toString().trim();
                String password = tietPassword.getText().toString().trim();
                if(!email.matches(emailPattern)){
                    Toast.makeText(LoginActivity.this, "Enter a proper email", Toast.LENGTH_SHORT).show();
                }
                else{
                    loginUser(email, password);
                }
            }
        });

        btnRegiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnForgotPassword = (TextView) findViewById(R.id.btnForgotPassword);
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


        btnOnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GoogleLoginActivity.class);
                startActivity(intent);

            }
        });
        btnOnLoginGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GithubLoginActivity.class);
                startActivity(intent);
            }
        });
    }



    private void loginUser(String email, String password) {
        User user = userRepository.getUser(email);
        if (user != null && user.getPassword().equals(password)) {
            Toast.makeText(LoginActivity.this, "OnLogin success!!!!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "OnLogin failed!!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            String name = "name" + (i + 1);
            String username = "email" + (i + 1);
            String password = "password" + (i + 1);
            String phone = "012345678" + i;
            String birthday = "2003-01-0" + (i + 1);

            User user = new User(name, username, password, phone, birthday);
            userRepository.addUser(user);
        }
    }

}