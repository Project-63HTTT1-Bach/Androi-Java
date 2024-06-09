package com.example.quizapp.models;

public class Question {
    private  String Question,opA,opB,opC,opD,correctAnswer;

    public Question() {

    }
    public Question(String question, String opA, String opB, String opC, String opD, String correctAnswer) {
        Question = question;
        this.opA = opA;
        this.opB = opB;
        this.opC = opC;
        this.opD = opD;
        this.correctAnswer = correctAnswer;
    }


    public void setOpA(String opA) {
        this.opA = opA;
    }

    public void setOpB(String opB) {
        this.opB = opB;
    }

    public void setOpC(String opC) {
        this.opC = opC;
    }

    public void setOpD(String opD) {
        this.opD = opD;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getQuestion() {
        return Question;
    }

    public String getOpA() {
        return opA;
    }

    public String getOpB() {
        return opB;
    }

    public String getOpC() {
        return opC;
    }

    public String getOpD() {
        return opD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
