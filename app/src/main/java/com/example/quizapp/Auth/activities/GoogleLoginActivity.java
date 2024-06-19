package com.example.quizapp.Auth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quizapp.HomeAndDiscover.activities.MainActivity;
import com.example.quizapp.R;
import com.example.quizapp.Auth.models.User;
import com.example.quizapp.Auth.repositories.UserRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class GoogleLoginActivity extends LoginActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 9001;
    UserRepository userRepository;
    DatabaseReference userRef;
    DatabaseReference userIdRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userIdRef = database.getReference("currentId");

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Khởi tạo userRepository
        userRepository = new UserRepository(this);

        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);

        // Kiểm tra và thiết lập giá trị khởi đầu cho currentId nếu chưa tồn tại
        userIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    userIdRef.setValue(0); // Giá trị khởi đầu
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("GoogleLoginActivity", "Lỗi khi đọc currentId", error.toException());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();

                    if (user != null) {
                        String userId = user.getUid();
                        String userName = user.getDisplayName();
                        String userEmail = user.getEmail();
                        String userAvatar = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : "";

                        // Kiểm tra xem email đã tồn tại trong Firebase chưa
                        userRef = database.getReference("users");
                        userRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // Email đã tồn tại, đăng nhập với user tương ứng
                                    proceedToMainActivity();
                                } else {
                                    // Tăng giá trị ID và thêm người dùng mới
                                    addUserWithCurrentID(userEmail, userName, userAvatar);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("GoogleLoginActivity", "Lỗi khi đọc dữ liệu người dùng", error.toException());
                            }
                        });
                    }
                } else {
                    Toast.makeText(GoogleLoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUserWithCurrentID(String email, String fullName, String avatarUrl) {
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
                        // Giá trị mặc định cho các trường mới
                        String defaultBirthday = "01/01/2000";
                        String defaultPhone = "";

                        User newUser = new User(newId, fullName, "", fullName, email, avatarUrl, defaultBirthday, defaultPhone);

                        // Lưu người dùng mới vào Firebase
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("id", newId);
                        map.put("username", fullName);
                        map.put("password", ""); // Mật khẩu để trống
                        map.put("fullname", fullName);
                        map.put("email", email);
                        map.put("profilePicture", avatarUrl);
                        map.put("birthday", defaultBirthday);
                        map.put("phone", defaultPhone);
                        database.getReference().child("users").child(newId.toString()).setValue(map);

                        // Lưu người dùng mới vào SQLite
                        if (!userRepository.checkUser(newUser)) {
                            userRepository.addUser(newUser);
                        }

                        proceedToMainActivity();
                    }
                } else {
                    Log.e("GoogleLoginActivity", "Transaction failed.");
                }
            }
        });
    }

    private void proceedToMainActivity() {
        Intent intent = new Intent(GoogleLoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
