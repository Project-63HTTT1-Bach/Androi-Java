package com.example.quizapp.livequiz.model;
public class LiveQuiz {
    private String title;
    private int imageResId;

    public LiveQuiz(String title, int imageResId) {
        this.title = title;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResId() {
        return imageResId;
    }
}

