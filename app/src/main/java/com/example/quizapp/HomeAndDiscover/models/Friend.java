// Friend.java
package com.example.quizapp.HomeAndDiscover.models;

public class Friend {
    private int friendId;
    private int userId;
    private int friendUserId;

    public Friend(int friendId, int userId, int friendUserId) {
//        this.friendId = friendId;
        this.userId = userId;
        this.friendUserId = friendUserId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(int friendUserId) {
        this.friendUserId = friendUserId;
    }
}

