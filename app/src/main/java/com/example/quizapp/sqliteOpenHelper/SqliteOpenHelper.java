package com.example.quizapp.sqliteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quizapp.models.Friend;
import com.example.quizapp.models.Question;
import com.example.quizapp.models.Quiz;
import com.example.quizapp.models.Result;
import com.example.quizapp.models.User;

import java.util.ArrayList;

public class SqliteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quizapp.db";
    private static final int DATABASE_VERSION = 1;

    public SqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE user (" + "userId INTEGER PRIMARY KEY AUTOINCREMENT, " + "username TEXT NOT NULL, " + "password TEXT NOT NULL, " + "fullname TEXT NOT NULL, " + "userCode TEXT NOT NULL, " + "email TEXT, " + "profilePicture TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_QUIZ_TABLE = "CREATE TABLE quiz (" + "quizId INTEGER PRIMARY KEY AUTOINCREMENT, " + "quizName TEXT NOT NULL, " + "creatorId INTEGER NOT NULL, " + "createDate TEXT, " + "isPublic INTEGER, " + "timeLimit INTEGER, " + "FOREIGN KEY (creatorId) REFERENCES user(userId))";
        db.execSQL(CREATE_QUIZ_TABLE);

        String CREATE_QUESTION_TABLE = "CREATE TABLE question (" + "questionId INTEGER PRIMARY KEY AUTOINCREMENT, " + "quizId INTEGER NOT NULL, " + "questionText TEXT NOT NULL, " + "questionType TEXT, " + "isCorrect INTEGER, " + "FOREIGN KEY (quizId) REFERENCES quiz(quizId))";
        db.execSQL(CREATE_QUESTION_TABLE);

        String CREATE_RESULT_TABLE = "CREATE TABLE result (" + "resultId INTEGER PRIMARY KEY AUTOINCREMENT, " + "userId INTEGER NOT NULL, " + "quizId INTEGER NOT NULL, " + "score INTEGER, " + "completionDate TEXT, " + "correctAnswers INTEGER, " + "incorrectAnswers INTEGER, " + "FOREIGN KEY (userId) REFERENCES user(userId), " + "FOREIGN KEY (quizId) REFERENCES quiz(quizId))";
        db.execSQL(CREATE_RESULT_TABLE);

        String CREATE_FRIEND_TABLE = "CREATE TABLE friend (" + "friendId INTEGER PRIMARY KEY AUTOINCREMENT, " + "userId INTEGER NOT NULL, " + "friendUserId INTEGER NOT NULL, " + "FOREIGN KEY (userId) REFERENCES user(userId), " + "FOREIGN KEY (friendUserId) REFERENCES user(userId))";
        db.execSQL(CREATE_FRIEND_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS quiz");
        db.execSQL("DROP TABLE IF EXISTS question");
        db.execSQL("DROP TABLE IF EXISTS answer");
        db.execSQL("DROP TABLE IF EXISTS result");
        db.execSQL("DROP TABLE IF EXISTS friend");
        onCreate(db);
    }

    public boolean insertUser(String username, String password, String fullname, String userCode, String email, String profilePicture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("fullname", fullname);
        contentValues.put("userCode", userCode);
        contentValues.put("email", email);
        contentValues.put("profilePicture", profilePicture);
        db.insert("user", null, contentValues);
        return true;
    }

    public boolean updateUser(int userId, String username, String password, String fullname, String userCode, String email, String profilePicture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("fullname", fullname);
        contentValues.put("userCode", userCode);
        contentValues.put("email", email);
        contentValues.put("profilePicture", profilePicture);
        db.update("user", contentValues, "userId = ? ", new String[]{Integer.toString(userId)});
        return true;
    }

    public Integer deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("user", "userId = ? ", new String[]{Integer.toString(userId)});
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM user", null);

        if (res != null && res.moveToFirst()) {
            int userIdIndex = res.getColumnIndex("userId");
            int usernameIndex = res.getColumnIndex("username");
            int passwordIndex = res.getColumnIndex("password");
            int fullnameIndex = res.getColumnIndex("fullname");
            int userCodeIndex = res.getColumnIndex("userCode");
            int emailIndex = res.getColumnIndex("email");
            int profilePictureIndex = res.getColumnIndex("profilePicture");

            if (userIdIndex != -1 && usernameIndex != -1 && passwordIndex != -1 && fullnameIndex != -1 && userCodeIndex != -1 && emailIndex != -1 && profilePictureIndex != -1) {
                do {
                    int userId = res.getInt(userIdIndex);
                    String username = res.getString(usernameIndex);
                    String password = res.getString(passwordIndex);
                    String fullname = res.getString(fullnameIndex);
                    String userCode = res.getString(userCodeIndex);
                    String email = res.getString(emailIndex);
                    String profilePicture = res.getString(profilePictureIndex);
                    User user = new User(userId, username, password, fullname, userCode, email, profilePicture);
                    array_list.add(user);
                } while (res.moveToNext());
            }
            res.close();
        }
        return array_list;
    }

    public boolean insertQuiz(String quizName, int creatorId, String createDate, int isPublic, int timeLimit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quizName", quizName);
        contentValues.put("creatorId", creatorId);
        contentValues.put("createDate", createDate);
        contentValues.put("isPublic", isPublic);
        contentValues.put("timeLimit", timeLimit);
        db.insert("quiz", null, contentValues);
        return true;
    }

    public boolean updateQuiz(int quizId, String quizName, int creatorId, String createDate, int isPublic, int timeLimit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quizName", quizName);
        contentValues.put("creatorId", creatorId);
        contentValues.put("createDate", createDate);
        contentValues.put("isPublic", isPublic);
        contentValues.put("timeLimit", timeLimit);
        db.update("quiz", contentValues, "quizId = ? ", new String[]{Integer.toString(quizId)});
        return true;
    }

    public Integer deleteQuiz(int quizId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("quiz", "quizId = ? ", new String[]{Integer.toString(quizId)});
    }

    public ArrayList<Quiz> getAllQuizzes() {
        ArrayList<Quiz> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM quiz", null);

        if (res != null && res.moveToFirst()) {
            int quizIdIndex = res.getColumnIndex("quizId");
            int quizNameIndex = res.getColumnIndex("quizName");
            int creatorIdIndex = res.getColumnIndex("creatorId");
            int createDateIndex = res.getColumnIndex("createDate");
            int isPublicIndex = res.getColumnIndex("isPublic");
            int timeLimitIndex = res.getColumnIndex("timeLimit");

            if (quizIdIndex != -1 && quizNameIndex != -1 && creatorIdIndex != -1 && createDateIndex != -1 && isPublicIndex != -1 && timeLimitIndex != -1) {
                do {
                    int quizId = res.getInt(quizIdIndex);
                    String quizName = res.getString(quizNameIndex);
                    int creatorId = res.getInt(creatorIdIndex);
                    String createDate = res.getString(createDateIndex);
                    int isPublic = res.getInt(isPublicIndex);
                    int timeLimit = res.getInt(timeLimitIndex);
                    Quiz quiz = new Quiz(quizId, quizName, creatorId, createDate, isPublic, timeLimit);
                    array_list.add(quiz);
                } while (res.moveToNext());
            }
            res.close();
        }
        return array_list;
    }

    public boolean insertQuestion(int quizId, String questionText, String questionType, int isCorrect) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quizId", quizId);
        contentValues.put("questionText", questionText);
        contentValues.put("questionType", questionType);
        contentValues.put("isCorrect", isCorrect);
        db.insert("question", null, contentValues);
        return true;
    }

    public boolean updateQuestion(int questionId, int quizId, String questionText, String questionType, int isCorrect) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quizId", quizId);
        contentValues.put("questionText", questionText);
        contentValues.put("questionType", questionType);
        contentValues.put("isCorrect", isCorrect);
        db.update("question", contentValues, "questionId = ? ", new String[]{Integer.toString(questionId)});
        return true;
    }

    public Integer deleteQuestion(int questionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("question", "questionId = ? ", new String[]{Integer.toString(questionId)});
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM question", null);

        if (res != null && res.moveToFirst()) {
            int questionIdIndex = res.getColumnIndex("questionId");
            int quizIdIndex = res.getColumnIndex("quizId");
            int questionTextIndex = res.getColumnIndex("questionText");
            int questionTypeIndex = res.getColumnIndex("questionType");
            int isCorrectIndex = res.getColumnIndex("isCorrect");

            if (questionIdIndex != -1 && quizIdIndex != -1 && questionTextIndex != -1 && questionTypeIndex != -1 && isCorrectIndex != -1) {
                do {
                    int questionId = res.getInt(questionIdIndex);
                    int quizId = res.getInt(quizIdIndex);
                    String questionText = res.getString(questionTextIndex);
                    String questionType = res.getString(questionTypeIndex);
                    int isCorrect = res.getInt(isCorrectIndex);
                    Question question = new Question(questionId, quizId, questionText, questionType, isCorrect);
                    array_list.add(question);
                } while (res.moveToNext());
            }
            res.close();
        }
        return array_list;
    }

    public boolean insertResult(int userId, int quizId, int score, String completionDate, int correctAnswers, int incorrectAnswers) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("quizId", quizId);
        contentValues.put("score", score);
        contentValues.put("completionDate", completionDate);
        contentValues.put("correctAnswers", correctAnswers);
        contentValues.put("incorrectAnswers", incorrectAnswers);
        db.insert("result", null, contentValues);
        return true;
    }

    public boolean updateResult(int resultId, int userId, int quizId, int score, String completionDate, int correctAnswers, int incorrectAnswers) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("quizId", quizId);
        contentValues.put("score", score);
        contentValues.put("completionDate", completionDate);
        contentValues.put("correctAnswers", correctAnswers);
        contentValues.put("incorrectAnswers", incorrectAnswers);
        db.update("result", contentValues, "resultId = ? ", new String[]{Integer.toString(resultId)});
        return true;
    }

    public Integer deleteResult(int resultId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("result", "resultId = ? ", new String[]{Integer.toString(resultId)});
    }

    public ArrayList<Result> getAllResults() {
        ArrayList<Result> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM result", null);

        if (res != null && res.moveToFirst()) {
            int resultIdIndex = res.getColumnIndex("resultId");
            int userIdIndex = res.getColumnIndex("userId");
            int quizIdIndex = res.getColumnIndex("quizId");
            int scoreIndex = res.getColumnIndex("score");
            int completionDateIndex = res.getColumnIndex("completionDate");
            int correctAnswersIndex = res.getColumnIndex("correctAnswers");
            int incorrectAnswersIndex = res.getColumnIndex("incorrectAnswers");

            if (resultIdIndex != -1 && userIdIndex != -1 && quizIdIndex != -1 && scoreIndex != -1 && completionDateIndex != -1 && correctAnswersIndex != -1 && incorrectAnswersIndex != -1) {
                do {
                    int resultId = res.getInt(resultIdIndex);
                    int userId = res.getInt(userIdIndex);
                    int quizId = res.getInt(quizIdIndex);
                    int score = res.getInt(scoreIndex);
                    String completionDate = res.getString(completionDateIndex);
                    int correctAnswers = res.getInt(correctAnswersIndex);
                    int incorrectAnswers = res.getInt(incorrectAnswersIndex);
                    Result result = new Result(resultId, userId, quizId, score, completionDate, correctAnswers, incorrectAnswers);
                    array_list.add(result);
                } while (res.moveToNext());
            }
            res.close();
        }
        return array_list;
    }

    public boolean insertFriend(int userId, int friendUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("friendUserId", friendUserId);
        db.insert("friend", null, contentValues);
        return true;
    }

    public boolean updateFriend(int friendId, int userId, int friendUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("friendUserId", friendUserId);
        db.update("friend", contentValues, "friendId = ? ", new String[]{Integer.toString(friendId)});
        return true;
    }

    public Integer deleteFriend(int friendId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("friend", "friendId = ? ", new String[]{Integer.toString(friendId)});
    }

    public ArrayList<Friend> getAllFriends() {
        ArrayList<Friend> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM friend", null);

        if (res != null && res.moveToFirst()) {
            int friendIdIndex = res.getColumnIndex("friendId");
            int userIdIndex = res.getColumnIndex("userId");
            int friendUserIdIndex = res.getColumnIndex("friendUserId");

            if (friendIdIndex != -1 && userIdIndex != -1 && friendUserIdIndex != -1) {
                do {
                    int friendId = res.getInt(friendIdIndex);
                    int userId = res.getInt(userIdIndex);
                    int friendUserId = res.getInt(friendUserIdIndex);
                    Friend friend = new Friend(friendId, userId, friendUserId);
                    array_list.add(friend);
                } while (res.moveToNext());
            }
            res.close();
        }
        return array_list;
    }
}
