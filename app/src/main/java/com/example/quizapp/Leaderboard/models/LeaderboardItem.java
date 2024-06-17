package com.example.quizapp.Leaderboard.models;

// LeaderboardItem.java
public class LeaderboardItem {

    private int position;
    private String name;
    private int points;
    private int avatarResourceId;
    private int medalResourceId;

    public LeaderboardItem(int position, String name, int points, int avatarResourceId, int medalResourceId) {
        this.position = position;
        this.name = name;
        this.points = points;
        this.avatarResourceId = avatarResourceId;
        this.medalResourceId = medalResourceId;
    }

    public int getPosition() {
        return position;
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

    public int getMedalResourceId() {
        return medalResourceId;
    }
}

