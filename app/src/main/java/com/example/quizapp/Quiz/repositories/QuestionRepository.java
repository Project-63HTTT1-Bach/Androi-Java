package com.example.quizapp.Quiz.repositories;

import android.content.Context;

import com.example.quizapp.Quiz.models.Question;
import com.example.quizapp.sqliteOpenHelper.SqliteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {
    private SqliteOpenHelper dbHelper;
    private static ArrayList<Question> questionList = new ArrayList<>();

    public QuestionRepository(Context context) {
        dbHelper = new SqliteOpenHelper(context);
        if (questionList.isEmpty()) {
            questionList = dbHelper.getAllQuestions();
        }
    }

    public static ArrayList<Question> getQuestionList() {
        return questionList;
    }

    public int addQuestion(Question question) {
        if (dbHelper.insertQuestion(question.getQuizId(), question.getQuestionText(), question.getQuestionType())) {
            questionList.add(question);
            return question.getQuestionId();
        }
        return -1;
    }

    public boolean updateQuestion(Question question) {
        if (dbHelper.updateQuestion(question.getQuestionId(), question.getQuizId(), question.getQuestionText(), question.getQuestionType())) {
            for (Question q : questionList) {
                if (q.getQuestionId() == question.getQuestionId()) {
                    q.setQuestionText(question.getQuestionText());
                    q.setQuizId(question.getQuizId());
                    q.setQuestionType(question.getQuestionType());
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public boolean deleteQuestion(Question question) {
        if (dbHelper.deleteQuestion(question.getQuestionId()) != 0) {
            questionList.remove(question);
            return true;
        }
        return false;
    }

    public List<Question> getQuestionsByQuizId(int quizId) {
        List<Question> filteredQuestions = new ArrayList<>();
        for (Question question : questionList) {
            if (question.getQuizId() == quizId) {
                filteredQuestions.add(question);
            }
        }
        return filteredQuestions;
    }

    public Question getQuestionById(int questionId) {
        for (Question question : questionList) {
            if (question.getQuestionId() == questionId) {
                return question;
            }
        }
        return null;
    }
}
