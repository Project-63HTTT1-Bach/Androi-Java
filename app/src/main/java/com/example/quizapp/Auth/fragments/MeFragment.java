package com.example.quizapp.Auth.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizapp.HomeAndDiscover.repositories.FriendRepository;
import com.example.quizapp.R;
import com.example.quizapp.Auth.activities.SettingActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MeFragment extends Fragment {

    private String userEmail;
    private ImageView ivUserAvatar;
    private DatabaseReference userRef;
    private TextView tvUsername, tvDays, tvQuizFinished, tvFriends;
    private int userId;
    private FriendRepository friendRepository;

    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userEmail = getArguments().getString("userEmail");
        }

        // Initialize Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

        friendRepository = new FriendRepository(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView btnSetting = view.findViewById(R.id.btnSetting);
        ivUserAvatar = view.findViewById(R.id.ivUseravatar);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvDays = view.findViewById(R.id.tvDays);
        tvQuizFinished = view.findViewById(R.id.tvQuizFinished);
        tvFriends = view.findViewById(R.id.tvFriends);

        btnSetting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
        });

        if (userEmail != null) {
            loadUserData(userEmail);
        } else {
            // Handle trường hợp không có email được truyền
        }
    }

    private void loadUserData(String email) {
        userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String profilePicture = userSnapshot.child("profilePicture").getValue(String.class);
                        String fullName = userSnapshot.child("fullname").getValue(String.class);
                        String createAt = userSnapshot.child("createAt").getValue(String.class);
                        userId = userSnapshot.child("id").getValue(Integer.class);

                        tvUsername.setText(fullName);
                        // Chuyển đổi chuỗi Base64 thành Bitmap và gán vào ImageView
                        if (profilePicture != null && !profilePicture.isEmpty()) {
                            Bitmap bitmap = decodeBase64(profilePicture);
                            ivUserAvatar.setImageBitmap(bitmap);
                        }

                        if (createAt != null) {
                            long days = calculateDays(createAt);
                            tvDays.setText(String.valueOf(days));
                        }
                        int friendCount = friendRepository.countFriendsByUserId(userId);
                        tvFriends.setText(String.valueOf(friendCount));
                    }
                } else {
                    // Handle trường hợp không tìm thấy người dùng
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle lỗi
            }
        });
    }

    private Bitmap decodeBase64(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private long calculateDays(String createAt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date createDate = sdf.parse(createAt);
            Date currentDate = new Date();
            long diffInMillies = Math.abs(currentDate.getTime() - createDate.getTime());
            return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
