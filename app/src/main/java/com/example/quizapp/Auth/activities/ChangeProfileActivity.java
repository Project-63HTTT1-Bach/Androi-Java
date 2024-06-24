package com.example.quizapp.Auth.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.Auth.models.User;
import com.example.quizapp.Auth.repositories.UserRepository;
import com.example.quizapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

public class ChangeProfileActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_CAMERA2_CAPTURE = 101;

    private ImageView btnBack, iv1, iv2, iv3, iv4, iv5, iv6;
    private TextView tvEmail;
    private EditText etFullname, etBirthday, etPhone;
    private LinearLayout btnSave;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private String userEmail;
    private String selectedAvatarBase64;
    private ImageView selectedAvatarView;
    private UserRepository userRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        etFullname = findViewById(R.id.etFullname2);
        etBirthday = findViewById(R.id.etBirthday2);
        etPhone = findViewById(R.id.etPhone2);
        tvEmail = findViewById(R.id.tvEmail2);

        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        iv4 = findViewById(R.id.iv4);
        iv5 = findViewById(R.id.iv5);
        iv6 = findViewById(R.id.iv6);

        iv1.setOnClickListener(v -> selectAvatar(iv1, R.drawable.user_avatar));
        iv2.setOnClickListener(v -> selectAvatar(iv2, R.drawable.user_avatar2));
        iv3.setOnClickListener(v -> selectAvatar(iv3, R.drawable.user_avatar3));
        iv4.setOnClickListener(v -> selectAvatar(iv4, R.drawable.user_avatar4));
        iv5.setOnClickListener(v -> selectAvatar(iv5, R.drawable.user_avatar5));
        iv6.setOnClickListener(v -> openCamera2Activity());

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> showConfirmDialog());

        database = FirebaseDatabase.getInstance();
        userEmail = getIntent().getStringExtra("userEmail");
        userRepository = new UserRepository(this);

        loadUserData(userEmail);
    }

    private void selectAvatar(ImageView imageView, int drawableId) {
        clearAvatarSelection();
        imageView.setImageResource(R.drawable.ic_avatarselected);
        selectedAvatarView = imageView;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        selectedAvatarBase64 = encodeToBase64(bitmap);
    }

    private void clearAvatarSelection() {
        iv1.setImageResource(R.drawable.user_avatar);
        iv2.setImageResource(R.drawable.user_avatar2);
        iv3.setImageResource(R.drawable.user_avatar3);
        iv4.setImageResource(R.drawable.user_avatar4);
        iv5.setImageResource(R.drawable.user_avatar5);
        iv6.setImageResource(R.drawable.photo_camera);
    }

    private void openCamera2Activity() {
        Intent intent = new Intent(this, Camera2Activity.class);
        startActivityForResult(intent, REQUEST_CAMERA2_CAPTURE);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA2_CAPTURE && resultCode == RESULT_OK) {
            String imageBase64 = data.getStringExtra("imageBase64");
            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            clearAvatarSelection();  // Clear previous selection
            iv6.setImageBitmap(decodedByte);
            selectedAvatarBase64 = imageBase64;
            iv6.setImageResource(R.drawable.ic_avatarselected);
        }
    }

    private String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void loadUserData(String email) {
        userRef = database.getReference("users");
        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String fullName = userSnapshot.child("fullname").getValue(String.class);
                        String birthday = userSnapshot.child("birthday").getValue(String.class);
                        String phone = userSnapshot.child("phone").getValue(String.class);
                        String email = userSnapshot.child("email").getValue(String.class);
                        String profilePicture = userSnapshot.child("profilePicture").getValue(String.class);

                        etFullname.setText(fullName);
                        etBirthday.setText(birthday);
                        etPhone.setText(phone);
                        tvEmail.setText(email);
                        if (profilePicture != null) {
                            byte[] decodedString = Base64.decode(profilePicture, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            selectedAvatarBase64 = profilePicture;
                        }
                    }
                } else {
                    Log.e("ChangeProfileActivity", "User not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChangeProfileActivity", "Database error: ", error.toException());
            }
        });
    }


    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_confirm_changes, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button buttonNo = dialogView.findViewById(R.id.btnNo);
        Button buttonYes = dialogView.findViewById(R.id.btnYes);

        buttonNo.setOnClickListener(v -> dialog.dismiss());

        buttonYes.setOnClickListener(v -> {
            updateUserData();
            dialog.dismiss();
        });

        dialog.show();

    }



    private void updateUserData() {
        String newFullName = etFullname.getText().toString().trim();
        String newBirthday = etBirthday.getText().toString().trim();
        String newPhone = etPhone.getText().toString().trim();
        String email = tvEmail.getText().toString().trim();

        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        if (userId != null) {
                            userRef.child(userId).child("fullname").setValue(newFullName);
                            userRef.child(userId).child("birthday").setValue(newBirthday);
                            userRef.child(userId).child("phone").setValue(newPhone);
                            userRef.child(userId).child("profilePicture").setValue(selectedAvatarBase64);

                            User user = new User();
                            user.setUserId(Integer.parseInt(userId)); // Chuyển đổi userId từ String sang int
                            user.setUsername(email);
                            user.setFullname(newFullName);
                            user.setBirthday(newBirthday);
                            user.setPhone(newPhone);
                            user.setProfilePicture(selectedAvatarBase64);
                            userRepository.updateUser(user);

                            Toast.makeText(ChangeProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ChangeProfileActivity.this, SettingActivity.class);
                            intent.putExtra("userEmail", userEmail);
                            startActivity(intent);
                            finish();
                        }
                    }
                } else {
                    Log.e("ChangeProfileActivity", "User not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChangeProfileActivity", "Database error: ", error.toException());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
