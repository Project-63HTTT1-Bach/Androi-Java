package com.example.quizapp.Quiz.repositories;

import android.content.Context;

import com.example.quizapp.Quiz.models.Answer;
import com.example.quizapp.sqliteOpenHelper.SqliteOpenHelper;

public class AnswerRepository {
    private SqliteOpenHelper dbHelper;

    public AnswerRepository(Context context) {
        dbHelper = new SqliteOpenHelper(context);
    }

    public boolean addAnswer(Answer answer) {
        return dbHelper.insertAnswer(answer.getQuestionId(), answer.getAnswerText(), answer.getIsCorrect());
    }
}
