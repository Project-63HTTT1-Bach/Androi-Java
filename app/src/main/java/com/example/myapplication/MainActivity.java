package com.example.myapplication;

import android.os.Bundle;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import android.widget.CompoundButton;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Account;
import com.example.myapplication.model.User;

public class MainActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private CheckBox checkBox;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        checkBox = findViewById(R.id.checkBox);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                User user = new User(1, "John", "Doe");
                Account account = new Account(username, password, user);

                if (isValidLogin(account)) {
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private boolean isValidLogin(Account account) {
        return account.getUsername().equals("admin") && account.getPassword().equals("password123");
    }
}