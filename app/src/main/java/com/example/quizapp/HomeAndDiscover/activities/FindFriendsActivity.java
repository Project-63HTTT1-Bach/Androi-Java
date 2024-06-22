// FindFriendsActivity.java
package com.example.quizapp.HomeAndDiscover.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.HomeAndDiscover.adapters.FriendAdapter;
import com.example.quizapp.HomeAndDiscover.models.Friend;
import com.example.quizapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindFriendsActivity extends AppCompatActivity {
    private ImageView btnBack;
    private RecyclerView rvFindFriend;
    private RecyclerView rvFriends;
    private FriendAdapter friendAdapter;
    private EditText etFindFriend;
    private DatabaseReference usersRef;
    private List<Friend> friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_friends);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        rvFriends = findViewById(R.id.rvFriends);
        rvFindFriend = findViewById(R.id.rvFindFriend);
        etFindFriend = findViewById(R.id.etFindFriend);

        rvFriends.setLayoutManager(new LinearLayoutManager(this));
        rvFindFriend.setLayoutManager(new LinearLayoutManager(this));

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        friendList = new ArrayList<>();
        friendAdapter = new FriendAdapter(this, friendList);
        rvFindFriend.setAdapter(friendAdapter);

        btnBack.setOnClickListener(v -> finish());

        etFindFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchFriendsByEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });
    }

    private void searchFriendsByEmail(String email) {
        usersRef.orderByChild("email").startAt(email).endAt(email + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        friendList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            int userId = Integer.parseInt(snapshot.getKey());
                            String userEmail = snapshot.child("email").getValue(String.class);

                            if (userEmail != null && userEmail.contains(email)) {
                                // Tạo đối tượng Friend với đầy đủ thông tin
                                Friend friend = new Friend(0, 0, userId);
                                friendList.add(friend);
                            }
                        }
                        friendAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors here
                    }
                });
    }
}
