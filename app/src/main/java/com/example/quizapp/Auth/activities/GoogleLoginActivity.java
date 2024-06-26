package com.example.quizapp.Auth.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Base64;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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
                if (!snapshot.exists() || snapshot.getValue(Integer.class) == null) {
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
                        String userEmail = user.getEmail();
                        String userFullName = user.getDisplayName();
                        String userPhotoUrl = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : "";

                        if (!userPhotoUrl.isEmpty()) {
                            // Tải và mã hóa ảnh
                            Picasso.get().load(userPhotoUrl).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    // Bo tròn ảnh
                                    Bitmap roundedBitmap = getCircularBitmap(bitmap);
                                    String userAvatarBase64 = encodeToBase64(roundedBitmap);

                                    // Tiếp tục xử lý đăng nhập
                                    processLogin(userEmail, userFullName, userAvatarBase64);
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                    // Xử lý khi tải ảnh thất bại
                                    Toast.makeText(GoogleLoginActivity.this, "Failed to load profile picture", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    // Xử lý khi chuẩn bị tải ảnh
                                }
                            });
                        } else {
                            // Nếu không có URL ảnh, tiếp tục xử lý đăng nhập với avatar trống
                            processLogin(userEmail, userFullName, "");
                        }
                    }
                } else {
                    Toast.makeText(GoogleLoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void processLogin(String email, String fullName, String avatarBase64) {
        // Kiểm tra xem email đã tồn tại trong Firebase chưa
        userRef = database.getReference("users");
        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Email đã tồn tại, đăng nhập với user tương ứng
                    proceedToMainActivity(email);
                } else {
                    // Tăng giá trị ID và thêm người dùng mới
                    addUserWithCurrentID(email, fullName, avatarBase64);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("GoogleLoginActivity", "Lỗi khi đọc dữ liệu người dùng", error.toException());
            }
        });
    }

    private void addUserWithCurrentID(String email, String fullName, String avatarBase64) {
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
                        String defaultBirthday = "";
                        String defaultPhone = "";

                        String createAt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        // Tạo chuỗi ngẫu nhiên cho password
                        String randomPassword = generateRandomString();

                        User newUser = new User(newId, email, randomPassword, fullName, email, avatarBase64, defaultBirthday, defaultPhone, createAt);

                        // Lưu người dùng mới vào Firebase
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("id", newId);
                        map.put("username", email);
                        map.put("password", randomPassword); // Mật khẩu ngẫu nhiên
                        map.put("fullname", fullName);
                        map.put("email", email);
                        map.put("profilePicture", avatarBase64);
                        map.put("birthday", defaultBirthday);
                        map.put("phone", defaultPhone);
                        map.put("createAt", createAt);
                        database.getReference().child("users").child(newId.toString()).setValue(map);

                        // Lưu người dùng mới vào SQLite
                        if (!userRepository.checkUser(newUser)) {
                            userRepository.addUser(newUser);
                        }

                        proceedToMainActivity(email);
                    }
                } else {
                    Log.e("GoogleLoginActivity", "Transaction failed.");
                }
            }
        });
    }

    private void proceedToMainActivity(String email) {
        Intent intent = new Intent(GoogleLoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userEmail", email);
        startActivity(intent);
        finish();
    }

    // Phương thức để tạo chuỗi ngẫu nhiên
    private String generateRandomString() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int PASSWORD_LENGTH = 20;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private String encodeToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
