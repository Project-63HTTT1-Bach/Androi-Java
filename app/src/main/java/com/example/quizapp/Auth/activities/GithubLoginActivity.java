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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleGithubLogin();
            }
        });
    }

    private void handleGithubLogin() {
        String email = txtEmail.getText().toString().trim();

        if (email.matches(emailPattern)) {
            signInWithGithub(email);
        } else {
            Toast.makeText(GithubLoginActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithGithub(String email) {
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
        provider.addCustomParameter("login", email);

        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            pendingResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    handleSignInResult(authResult);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GithubLoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mAuth.startActivityForSignInWithProvider(this, provider.build()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    handleSignInResult(authResult);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GithubLoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void handleSignInResult(AuthResult authResult) {
        FirebaseUser user = authResult.getUser();
        if (user != null) {
            linkProviderWithExistingUser(user, OAuthProvider.newCredentialBuilder("github.com").build());
        }
    }

    private void linkProviderWithExistingUser(FirebaseUser user, AuthCredential credential) {
        String userEmail = user.getEmail();
        if (userEmail != null) {
            userRef = database.getReference("users");
            userRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Email exists in the database, link the provider
                        user.linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                proceedToMainActivity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(GithubLoginActivity.this, "Failed to link account", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Email does not exist in the database, create a new user
                        createUserWithCurrentID(user);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("DatabaseError", "onCancelled: ", error.toException());
                }
            });
        }
    }

    private void createUserWithCurrentID(FirebaseUser user) {
        userIdRef = database.getReference("currentID");
        userIdRef.runTransaction(new Transaction.Handler() {
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
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (committed) {
                    Integer newId = currentData.getValue(Integer.class);
                    if (newId != null) {
                        String email = user.getEmail();
                        String fullName = user.getDisplayName();
                        String avatarUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";

                        // Create new user object
                        User newUser = new User(newId, "", "", fullName, email, avatarUrl, "01/01/2003", "");

                        // Add user to SQLite
                        userRepository.addUser(newUser);

                        // Add user data to Firebase Realtime Database
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("id", newId);
                        userMap.put("email", email);
                        userMap.put("fullname", fullName);
                        userMap.put("profilePicture", avatarUrl);
                        userMap.put("birthday", "01/01/2003");
                        userMap.put("username", "");
                        userMap.put("password", "");
                        userMap.put("phone", "");

                        // Save user to Firebase
                        database.getReference().child("users").child(newId.toString()).setValue(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(GithubLoginActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                                proceedToMainActivity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(GithubLoginActivity.this, "Failed to add user", Toast.LENGTH_SHORT).show();
                                Log.e("DatabaseError", "onFailure: ", e);
                            }
                        });
                    }
                } else {
                    Log.e("GithubLoginActivity", "Transaction failed.");
                }
            }
        });
    }

    private void proceedToMainActivity() {
        Intent intent = new Intent(GithubLoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
