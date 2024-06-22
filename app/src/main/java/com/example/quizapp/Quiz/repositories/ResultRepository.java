package com.example.quizapp.Quiz.repositories;

import android.content.Context;

import com.example.quizapp.Quiz.models.Result;
import com.example.quizapp.sqliteOpenHelper.SqliteOpenHelper;

public class ResultRepository {
    private SqliteOpenHelper dbHelper;

    public ResultRepository(Context context) {
        dbHelper = new SqliteOpenHelper(context);
    }

    public boolean addResult(Result result) {
        return dbHelper.insertResult(result.getUserId(), result.getQuizId(), result.getScore(), result.getCompletionDate(), result.getCorrectAnswers(), result.getIncorrectAnswers());
    }
}
