package com.example.quizapp.HomeAndDiscover.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.HomeAndDiscover.adapters.FindFriendAdapter;
import com.example.quizapp.Auth.models.User;
import com.example.quizapp.HomeAndDiscover.adapters.FriendAdapter;
import com.example.quizapp.HomeAndDiscover.models.Friend;
import com.example.quizapp.R;
import com.example.quizapp.HomeAndDiscover.repositories.FriendRepository;
import com.example.quizapp.Auth.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FindFriendsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etFindFriend;
    private RecyclerView rvFindFriend;
    private RecyclerView rvFriends;
    private FindFriendAdapter findFriendAdapter;
    private FriendAdapter friendAdapter;
    private List<User> allUsersList;
    private List<User> filteredList;
    private List<Friend> friendList;
    private UserRepository userRepository;
    private FriendRepository friendRepository;
    private int currentUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        btnBack = findViewById(R.id.btnBack);
        etFindFriend = findViewById(R.id.etFindFriend);
        rvFindFriend = findViewById(R.id.rvFindFriend);
        rvFriends = findViewById(R.id.rvFriends);

        btnBack.setOnClickListener(v -> finish());

        // Get currentUserId from Intent
        currentUserId = getIntent().getIntExtra("userId", -1);

        // Initialize repositories
        userRepository = new UserRepository(this);
        friendRepository = new FriendRepository(this);

        // Initialize user lists
        allUsersList = new ArrayList<>();
        filteredList = new ArrayList<>();
        friendList = new ArrayList<>();

        // Initialize adapters with empty lists
        findFriendAdapter = new FindFriendAdapter(new ArrayList<>(), this, currentUserId, friendRepository);
        friendAdapter = new FriendAdapter(this, new ArrayList<>(), userRepository);

        rvFindFriend.setLayoutManager(new LinearLayoutManager(this));
        rvFindFriend.setAdapter(findFriendAdapter);

        rvFriends.setLayoutManager(new LinearLayoutManager(this));
        rvFriends.setAdapter(friendAdapter);

        // Load all users but do not display them yet
        loadAllUsers();
        // Load current friends
        loadCurrentFriends();

        etFindFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    private void filter(String text) {
        filteredList.clear();
        for (User user : allUsersList) {
            if (user.getEmail().toLowerCase().contains(text.toLowerCase()) &&
                    user.getUserId() != currentUserId) {
                filteredList.add(user);
            }
        }
        findFriendAdapter.updateList(filteredList);
    }

    private void loadAllUsers() {
        List<Friend> friendList = friendRepository.getFriendList();
        Set<Integer> friendUserIds = new HashSet<>();

        // Get friendUserIds for the current user
        for (Friend friend : friendList) {
            if (friend.getUserId() == currentUserId) {
                friendUserIds.add(friend.getFriendUserId());
            }
        }

        // Get all users and filter out friends and the current user
        List<User> allUsers = userRepository.getAllUsers();
        List<User> nonFriendUsers = new ArrayList<>();

        for (User user : allUsers) {
            if (user.getUserId() != currentUserId && !friendUserIds.contains(user.getUserId())) {
                nonFriendUsers.add(user);
            }
        }

        allUsersList = nonFriendUsers;
    }

    private void loadCurrentFriends() {
        List<Friend> friendList = friendRepository.getFriendList();
        List<Friend> currentFriends = new ArrayList<>();

        for (Friend friend : friendList) {
            if (friend.getUserId() == currentUserId) {
                currentFriends.add(friend);
            }
        }

        this.friendList = currentFriends;
        friendAdapter.updateList(currentFriends);
    }

    public void updateFriendList() {
        loadCurrentFriends();
    }
}
