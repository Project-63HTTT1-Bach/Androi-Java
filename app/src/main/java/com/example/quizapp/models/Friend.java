// Friend.java
package com.example.quizapp.models;

public class Friend {
    private String name;
    private int points;
    private int avatarResourceId;

    public Friend(String name, int points, int avatarResourceId) {
        this.name = name;
        this.points = points;
        this.avatarResourceId = avatarResourceId;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getAvatarResourceId() {
        return avatarResourceId;
    }
}

