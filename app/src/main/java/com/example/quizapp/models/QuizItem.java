// QuizItem.java
package com.example.quizapp.models;

public class QuizItem {

    private String title;
    private int iconResourceId;

    public QuizItem(String title, int iconResourceId) {
        this.title = title;
        this.iconResourceId = iconResourceId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }
}
