package com.example.quizapp.sqliteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.quizapp.HomeAndDiscover.fragments.HomeFragment;

import com.example.quizapp.Quiz.models.Answer;
import com.example.quizapp.HomeAndDiscover.models.Friend;
import com.example.quizapp.Quiz.models.Question;
import com.example.quizapp.Quiz.models.Quiz;
import com.example.quizapp.Quiz.models.Result;
import com.example.quizapp.Auth.models.User;

import java.util.ArrayList;

public class SqliteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quizapp.db";
    private static final int DATABASE_VERSION = 12;

    public SqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE user (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " + "password TEXT NOT NULL, " +
                "fullname TEXT NOT NULL, " + "email TEXT NOT NULL, " +
                "profilePicture TEXT, " + "birthDay TEXT," + "phone TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_QUIZ_TABLE = "CREATE TABLE quiz (" +
                "quizId INTEGER PRIMARY KEY, " +
                "quizName TEXT NOT NULL, " +
                "creatorId INTEGER NOT NULL, " +
                "startTime TEXT, " +
                "endTime TEXT, " +
                "description TEXT, " +
                "isPublic INTEGER, " +
                "timeLimit INTEGER, " +
                "iconImage TEXT, " +
                "quizCode TEXT, " +
                "FOREIGN KEY (creatorId) REFERENCES user(userId))";
        db.execSQL(CREATE_QUIZ_TABLE);

        String CREATE_QUESTION_TABLE = "CREATE TABLE question (" +
                "questionId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "quizId INTEGER NOT NULL, " + "questionText TEXT NOT NULL, " +
                "questionType TEXT, " + "FOREIGN KEY (quizId) REFERENCES quiz(quizId))";
        db.execSQL(CREATE_QUESTION_TABLE);

        String CREATE_ANSWER_TABLE = "CREATE TABLE answer (" +
                "answerId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "questionId INTEGER NOT NULL, " + "answerText TEXT NOT NULL, " +
                "isCorrect INTEGER, " +
                "FOREIGN KEY (questionId) REFERENCES question(questionId))";
        db.execSQL(CREATE_ANSWER_TABLE);

        String CREATE_RESULT_TABLE = "CREATE TABLE result (" +
                "resultId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER NOT NULL, " + "quizId INTEGER NOT NULL, " +
                "score INTEGER, " +
                "completionDate TEXT, " +
                "correctAnswers INTEGER, " +
                "incorrectAnswers INTEGER, " +
                "FOREIGN KEY (userId) REFERENCES user(userId), " +
                "FOREIGN KEY (quizId) REFERENCES quiz(quizId))";
        db.execSQL(CREATE_RESULT_TABLE);

        String CREATE_FRIEND_TABLE = "CREATE TABLE friend (" +
                "friendId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER NOT NULL, " +
                "friendUserId INTEGER NOT NULL, " +
                "FOREIGN KEY (userId) REFERENCES user(userId), " +
                "FOREIGN KEY (friendUserId) REFERENCES user(userId))";
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

    public boolean insertUser(String username, String password, String fullname, String email, String profilePicture, String birthDay, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("fullname", fullname);
        contentValues.put("phone", phone);
        contentValues.put("birthday", birthDay);
        contentValues.put("email", email);
        contentValues.put("profilePicture", profilePicture);
        db.insert("user", null, contentValues);
        return true;
    }

    public boolean updateUser(int userId, String username, String password, String fullname, String email, String profilePicture, String birthDay, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("fullname", fullname);
        contentValues.put("phone", phone);
        contentValues.put("birthday", birthDay);
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
            int phoneIndex = res.getColumnIndex("phone");
            int birthdayIndex = res.getColumnIndex("birthday");
            int emailIndex = res.getColumnIndex("email");
            int profilePictureIndex = res.getColumnIndex("profilePicture");

            if (userIdIndex != -1 && birthdayIndex != -1 && usernameIndex != -1 && passwordIndex != -1 && fullnameIndex != -1 && phoneIndex != -1 && emailIndex != -1 && profilePictureIndex != -1) {
                do {
                    int userId = res.getInt(userIdIndex);
                    String username = res.getString(usernameIndex);
                    String password = res.getString(passwordIndex);
                    String fullname = res.getString(fullnameIndex);
                    String phone = res.getString(phoneIndex);
                    String email = res.getString(emailIndex);
                    String birthday = res.getString(birthdayIndex);
                    String profilePicture = res.getString(profilePictureIndex);
                    User user = new User(userId, username, password, fullname, email, profilePicture, birthday, phone);
                    array_list.add(user);
                } while (res.moveToNext());
            }
            res.close();
        }
        return array_list;
    }

    public boolean insertQuiz(String quizName, int creatorId, String startTime, String endTime, String description, int isPublic, int timeLimit, String iconImage, String quizCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quizName", quizName);
        contentValues.put("creatorId", creatorId);
        contentValues.put("startTime", startTime);
        contentValues.put("endTime", endTime);
        contentValues.put("description", description);
        contentValues.put("isPublic", isPublic);
        contentValues.put("timeLimit", timeLimit);
        contentValues.put("iconImage", iconImage);
        contentValues.put("quizCode", quizCode);
        db.insert("quiz", null, contentValues);
        return true;
    }

    public boolean updateQuiz(int quizId, String quizName, int creatorId, String startTime, String endTime, String description, int isPublic, int timeLimit, String iconImage, String quizCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quizName", quizName);
        contentValues.put("creatorId", creatorId);
        contentValues.put("startTime", startTime);
        contentValues.put("endTime", endTime);
        contentValues.put("description", description);
        contentValues.put("isPublic", isPublic);
        contentValues.put("timeLimit", timeLimit);
        contentValues.put("iconImage", iconImage);
        contentValues.put("quizCode", quizCode);
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
            int startTimeIndex = res.getColumnIndex("startTime");
            int endTimeIndex = res.getColumnIndex("endTime");
            int descriptionIndex = res.getColumnIndex("description");
            int isPublicIndex = res.getColumnIndex("isPublic");
            int timeLimitIndex = res.getColumnIndex("timeLimit");
            int iconImageIndex = res.getColumnIndex("iconImage");
            int quizCodeIndex = res.getColumnIndex("quizCode");

            if (quizIdIndex != -1 && quizNameIndex != -1 && creatorIdIndex != -1 && startTimeIndex != -1 && endTimeIndex != -1 && descriptionIndex != -1 && isPublicIndex != -1 && timeLimitIndex != -1 && iconImageIndex != -1 && quizCodeIndex != -1) {
                do {
                    int quizId = res.getInt(quizIdIndex);
                    String quizName = res.getString(quizNameIndex);
                    int creatorId = res.getInt(creatorIdIndex);
                    String startTime = res.getString(startTimeIndex);
                    String endTime = res.getString(endTimeIndex);
                    String description = res.getString(descriptionIndex);
                    int isPublic = res.getInt(isPublicIndex);
                    int timeLimit = res.getInt(timeLimitIndex);
                    String iconImage = res.getString(iconImageIndex);
                    String quizCode = res.getString(quizCodeIndex);
                    Quiz quiz = new Quiz(quizId, quizName, creatorId, startTime, endTime, description, isPublic, timeLimit, iconImage, quizCode);
                    array_list.add(quiz);
                } while (res.moveToNext());
            }
            res.close();
        }
        return array_list;
    }

    public boolean insertQuestion(int quizId, String questionText, String questionType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quizId", quizId);
        contentValues.put("questionText", questionText);
        contentValues.put("questionType", questionType);
        db.insert("question", null, contentValues);
        return true;
    }

    public boolean updateQuestion(int questionId, int quizId, String questionText, String questionType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quizId", quizId);
        contentValues.put("questionText", questionText);
        contentValues.put("questionType", questionType);
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

            if (questionIdIndex != -1 && quizIdIndex != -1 && questionTextIndex != -1 && questionTypeIndex != -1) {
                do {
                    int questionId = res.getInt(questionIdIndex);
                    int quizId = res.getInt(quizIdIndex);
                    String questionText = res.getString(questionTextIndex);
                    String questionType = res.getString(questionTypeIndex);
                    Question question = new Question(questionId, quizId, questionText, questionType);
                    array_list.add(question);
                } while (res.moveToNext());
            }
            res.close();
        }
        return array_list;
    }

    public boolean insertAnswer(int questionId, String answerText, int isCorrect) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("questionId", questionId);
        contentValues.put("answerText", answerText);
        contentValues.put("isCorrect", isCorrect);
        db.insert("answer", null, contentValues);
        return true;
    }

    public boolean updateAnswer(int answerId, int questionId, String answerText, int isCorrect) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("questionId", questionId);
        contentValues.put("answerText", answerText);
        contentValues.put("isCorrect", isCorrect);
        db.update("answer", contentValues, "answerId = ? ", new String[]{Integer.toString(answerId)});
        return true;
    }

    public Integer deleteAnswer(int answerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("answer", "answerId = ? ", new String[]{Integer.toString(answerId)});
    }

    public ArrayList<Answer> getAllAnswers() {
        ArrayList<Answer> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM answer", null);

        if (res != null && res.moveToFirst()) {
            int answerIdIndex = res.getColumnIndex("answerId");
            int questionIdIndex = res.getColumnIndex("questionId");
            int answerTextIndex = res.getColumnIndex("answerText");
            int isCorrectIndex = res.getColumnIndex("isCorrect");

            if (answerIdIndex != -1 && questionIdIndex != -1 && answerTextIndex != -1 && isCorrectIndex != -1) {
                do {
                    int answerId = res.getInt(answerIdIndex);
                    int questionId = res.getInt(questionIdIndex);
                    String answerText = res.getString(answerTextIndex);
                    int isCorrect = res.getInt(isCorrectIndex);
                    Answer answer = new Answer(answerId, questionId, answerText, isCorrect);
                    array_list.add(answer);
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

