// QuizItem.java
package com.example.quizapp.models;

public class Quiz {
    private int quizId;
    private String quizName;
    private int creatorId;
    private String createDate;
    private int isPublic;
    private int timeLimit;

    public Quiz(int quizId, String quizName, int creatorId, String createDate, int isPublic, int timeLimit) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.creatorId = creatorId;
        this.createDate = createDate;
        this.isPublic = isPublic;
        this.timeLimit = timeLimit;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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
}
