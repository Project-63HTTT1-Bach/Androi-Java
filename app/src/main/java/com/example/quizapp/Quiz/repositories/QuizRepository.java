package com.example.quizapp.Quiz.repositories;

import android.content.Context;

import com.example.quizapp.Quiz.models.Quiz;
import com.example.quizapp.sqliteOpenHelper.SqliteOpenHelper;

import java.util.ArrayList;

public class QuizRepository {
    private SqliteOpenHelper dbHelper;
    private static ArrayList<Quiz> quizList = new ArrayList<>();

    public QuizRepository(Context context) {
        dbHelper = new SqliteOpenHelper(context);
        if (quizList.isEmpty()) {
            quizList = dbHelper.getAllQuizzes();
        }
    }

    public QuizRepository() {
        // Default constructor
    }

    public static ArrayList<Quiz> getQuizList() {
        return quizList;
    }

    public void filterQuizzesByUserId(int userId) {
        ArrayList<Quiz> filteredQuizzes = new ArrayList<>();
        for (Quiz quiz : quizList) {
            if (quiz.getCreatorId() == userId) {
                filteredQuizzes.add(quiz);
            }
        }
        quizList = filteredQuizzes;
    }

    public boolean checkExistedQuiz(Quiz q) {
        for (Quiz quiz : quizList) {
            if (quiz.getQuizName().equals(q.getQuizName()) && quiz.getCreatorId() == q.getCreatorId()) {
                return true;
            }
        }
        return false;
    }

    public boolean addQuiz(Quiz q) {
        if (!checkExistedQuiz(q)) {
            if (dbHelper.insertQuiz(q.getQuizName(), q.getCreatorId(), q.getStartTime(), q.getEndTime(), q.getDescription(), q.getIsPublic(), q.getTimeLimit(), q.getIconImage(), q.getQuizCode())) {
                quizList.add(q);
                return true;
            }
        }
        return false;
    }

    public boolean updateQuiz(Quiz q) {
        if (dbHelper.updateQuiz(q.getQuizId(), q.getQuizName(), q.getCreatorId(), q.getStartTime(), q.getEndTime(), q.getDescription(), q.getIsPublic(), q.getTimeLimit(), q.getIconImage(), q.getQuizCode())) {
            for (Quiz quiz : quizList) {
                if (quiz.getQuizId() == q.getQuizId()) {
                    quiz.setQuizName(q.getQuizName());
                    quiz.setCreatorId(q.getCreatorId());
                    quiz.setStartTime(q.getStartTime());
                    quiz.setEndTime(q.getEndTime());
                    quiz.setDescription(q.getDescription());
                    quiz.setIsPublic(q.getIsPublic());
                    quiz.setTimeLimit(q.getTimeLimit());
                    quiz.setIconImage(q.getIconImage());
                    quiz.setQuizCode(q.getQuizCode());
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public boolean removeQuiz(Quiz q) {
        if (dbHelper.deleteQuiz(q.getQuizId()) != 0) {
            for (int i = 0; i < quizList.size(); i++) {
                if (quizList.get(i).getQuizId() == q.getQuizId()) {
                    quizList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
}
