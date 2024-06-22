package com.example.quizapp.Quiz.repositories;

import android.content.Context;

import com.example.quizapp.Quiz.models.Question;
import com.example.quizapp.sqliteOpenHelper.SqliteOpenHelper;

public class QuestionRepository {
    private SqliteOpenHelper dbHelper;

    public QuestionRepository(Context context) {
        dbHelper = new SqliteOpenHelper(context);
    }

    public boolean addQuestion(Question question) {
        return dbHelper.insertQuestion(question.getQuizId(), question.getQuestionText(), question.getQuestionType());
    }
}
