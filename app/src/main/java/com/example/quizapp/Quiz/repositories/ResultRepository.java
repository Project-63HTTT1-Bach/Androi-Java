package com.example.quizapp.Quiz.repositories;

import android.content.Context;

import com.example.quizapp.Quiz.models.Quiz;
import com.example.quizapp.Quiz.models.Result;
import com.example.quizapp.sqliteOpenHelper.SqliteOpenHelper;

import java.util.ArrayList;

public class ResultRepository {
    private SqliteOpenHelper dbHelper;
    private static ArrayList<Result> resultList = new ArrayList<>();

    public ResultRepository(Context context) {
        dbHelper = new SqliteOpenHelper(context);
        if (resultList.isEmpty()) {
            resultList = dbHelper.getAllResults();
        }
    }

    public static ArrayList<Result> getResultList() {
        return resultList;
    }

    public boolean addResult(Result result) {
        return dbHelper.insertResult(result.getUserId(), result.getQuizId(), result.getScore(), result.getCompletionDate(), result.getCorrectAnswers(), result.getIncorrectAnswers());
    }
}
