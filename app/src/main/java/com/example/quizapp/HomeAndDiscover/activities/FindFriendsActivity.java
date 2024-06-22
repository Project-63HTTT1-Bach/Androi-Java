package com.example.quizapp.HomeAndDiscover.activities;

import static java.util.Locale.filter;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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
    private RecyclerView rvFindFriend;
    private RecyclerView rvFriends;
    private FriendRepository friendRepository;
    private FriendAdapter friendAdapter;
    private Toolbar toolbar;
    private SearchView searchView;

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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                friendAdapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                friendAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return true;
//    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}