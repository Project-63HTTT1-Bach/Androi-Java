package com.example.quizapp.HomeAndDiscover.repositories;

import android.content.Context;

import com.example.quizapp.HomeAndDiscover.models.Friend;
import com.example.quizapp.Quiz.models.Quiz;
import com.example.quizapp.sqliteOpenHelper.SqliteOpenHelper;

import java.util.ArrayList;

public class FriendRepository {
    private SqliteOpenHelper dbHelper;
    private static ArrayList<Friend> friendList = new ArrayList<>();

    public FriendRepository(Context context) {
        dbHelper = new SqliteOpenHelper(context);
        if (friendList.isEmpty()) {
            friendList = dbHelper.getAllFriends();
        }
    }

    public FriendRepository() {

    }

    public static ArrayList<Friend> getFriendList() {
        return friendList;
    }

    public boolean checkExistedFriend(Friend f) {
        for (Friend friend : friendList) {
            if (friend.getUserId() == f.getUserId() && friend.getFriendUserId() == f.getFriendUserId()) {
                return true;
            }
        }
        return false;
    }

    public boolean addFriend(Friend f) {
        if (!checkExistedFriend(f)) {
            if (dbHelper.insertFriend(f.getUserId(), f.getFriendUserId())) {
                friendList.add(f);
                return true;
            }
        }
        return false;
    }

    public boolean removeFriend(Friend f) {
        if (dbHelper.deleteQuiz(f.getFriendId()) != 0) {
            for (int i = 0; i < friendList.size(); i++) {
                if (friendList.get(i).getFriendId() == f.getFriendId()) {
                    friendList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
}
