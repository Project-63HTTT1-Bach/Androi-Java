package com.example.quizapp.Auth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.HomeAndDiscover.activities.MainActivity;
import com.example.quizapp.R;
import com.example.quizapp.Auth.models.User;
import com.example.quizapp.Auth.repositories.UserRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class GithubLoginActivity extends AppCompatActivity {
    TextInputEditText txtEmail;
    Button btnLogin;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    UserRepository userRepository;
    DatabaseReference userRef;
    DatabaseReference userIdRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_login);

        txtEmail = findViewById(R.id.txtEmail);
        btnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userRepository = new UserRepository(this);

    }


}
