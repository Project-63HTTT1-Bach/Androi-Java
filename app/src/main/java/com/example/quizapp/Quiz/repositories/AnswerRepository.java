package com.example.quizapp.Quiz.repositories;

import android.content.Context;

import com.example.quizapp.Quiz.models.Answer;
import com.example.quizapp.sqliteOpenHelper.SqliteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AnswerRepository {
    private SqliteOpenHelper dbHelper;
    private static ArrayList<Answer> answerList = new ArrayList<>();

    public AnswerRepository(Context context) {
        dbHelper = new SqliteOpenHelper(context);
        if (answerList.isEmpty()) {
            answerList = dbHelper.getAllAnswers();
        }
    }

    public static ArrayList<Answer> getAnswerList() {
        return answerList;
    }

    public boolean addAnswer(Answer answer) {
        if (dbHelper.insertAnswer(answer.getQuestionId(), answer.getAnswerText(), answer.getIsCorrect())) {
            answerList.add(answer);
            return true;
        }
        return false;
    }

    public boolean updateAnswer(Answer answer) {
        if (dbHelper.updateAnswer(answer.getAnswerId(), answer.getQuestionId(), answer.getAnswerText(), answer.getIsCorrect())) {
            for (Answer a : answerList) {
                if (a.getAnswerId() == answer.getAnswerId()) {
                    a.setAnswerText(answer.getAnswerText());
                    a.setQuestionId(answer.getQuestionId());
                    a.setIsCorrect(answer.getIsCorrect());
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public boolean deleteAnswer(Answer answer) {
        if (dbHelper.deleteAnswer(answer.getAnswerId()) != 0) {
            answerList.remove(answer);
            return true;
        }
        return false;
    }

    public List<Answer> getAnswersByQuestionId(int questionId) {
        List<Answer> filteredAnswers = new ArrayList<>();
        for (Answer answer : answerList) {
            if (answer.getQuestionId() == questionId) {
                filteredAnswers.add(answer);
            }
        }
        return filteredAnswers;
    }
}
