// QuizItem.java
package com.example.quizapp.Quiz.models;

public class Quiz {
    private int quizId;
    private String quizName;
    private int creatorId;
    private String startTime;
    private String endTime;
    private String description;
    private int isPublic;
    private int timeLimit;
    private String iconImage;
    private String quizCode;

    public Quiz(int quizId, String quizName, int creatorId, String startTime, String endTime, String description, int isPublic, int timeLimit, String iconImage, String quizCode) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.creatorId = creatorId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.isPublic = isPublic;
        this.timeLimit = timeLimit;
        this.iconImage = iconImage;
        this.quizCode = quizCode;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public String getQuizCode() {
        return quizCode;
    }

    public void setQuizCode(String quizCode) {
        this.quizCode = quizCode;
    }
}
