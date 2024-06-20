package com.example.quizapp.HomeAndDiscover.activities;

import static java.util.Locale.filter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.HomeAndDiscover.adapters.FriendAdapter;
import com.example.quizapp.HomeAndDiscover.models.Friend;
import com.example.quizapp.HomeAndDiscover.repositories.FriendRepository;
import com.example.quizapp.R;

import java.util.List;

public class FindFriendsActivity extends AppCompatActivity {
    private ImageView btnBack;
    private RecyclerView rvFriends;
    private RecyclerView rvFindFriend;
    private FriendRepository friendRepository;
    private FriendAdapter friendAdapter;
    private EditText searchEditText;

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

        rvFriends = (RecyclerView) findViewById(R.id.rvFriends);
        rvFriends.setLayoutManager(new LinearLayoutManager(this));
        rvFindFriend = (RecyclerView) findViewById(R.id.rvFindFriend);
        rvFindFriend.setLayoutManager(new LinearLayoutManager(this));

        searchEditText = (EditText) findViewById(R.id.searchEditText);

        friendRepository = new FriendRepository(this);

        List<Friend> friendList = FriendRepository.getFriendList();
        friendAdapter = new FriendAdapter(this, friendList);
        rvFriends.setAdapter(friendAdapter);
        rvFindFriend.setAdapter(friendAdapter);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}