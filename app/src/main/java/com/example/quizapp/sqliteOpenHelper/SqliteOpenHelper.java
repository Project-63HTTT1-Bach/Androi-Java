package com.example.quizapp.sqliteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quizapp.db";
    private static final int DATABASE_VERSION = 1;

    public SqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create User table
        String CREATE_USER_TABLE = "CREATE TABLE user ("
                + "userId INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "email TEXT, "
                + "profilePicture TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        // Create Quiz table
        String CREATE_QUIZ_TABLE = "CREATE TABLE quiz ("
                + "quizId INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "quizName TEXT NOT NULL, "
                + "creatorId INTEGER NOT NULL, "
                + "createDate TEXT, "
                + "isPublic INTEGER, "
                + "FOREIGN KEY (creatorId) REFERENCES user(userId))";
        db.execSQL(CREATE_QUIZ_TABLE);

        // Create Question table
        String CREATE_QUESTION_TABLE = "CREATE TABLE question ("
                + "questionId INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "quizId INTEGER NOT NULL, "
                + "questionText TEXT NOT NULL, "
                + "questionType TEXT, "
                + "FOREIGN KEY (quizId) REFERENCES quiz(quizId))";
        db.execSQL(CREATE_QUESTION_TABLE);

        // Create Answer table
        String CREATE_ANSWER_TABLE = "CREATE TABLE answer ("
                + "answerId INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "questionId INTEGER NOT NULL, "
                + "answerText TEXT NOT NULL, "
                + "isCorrect INTEGER, "
                + "FOREIGN KEY (questionId) REFERENCES question(questionId))";
        db.execSQL(CREATE_ANSWER_TABLE);

        // Create Result table
        String CREATE_RESULT_TABLE = "CREATE TABLE result ("
                + "resultId INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "userId INTEGER NOT NULL, "
                + "quizId INTEGER NOT NULL, "
                + "score INTEGER, "
                + "completionDate TEXT, "
                + "FOREIGN KEY (userId) REFERENCES user(userId), "
                + "FOREIGN KEY (quizId) REFERENCES quiz(quizId))";
        db.execSQL(CREATE_RESULT_TABLE);

        // Create Friend table
        String CREATE_FRIEND_TABLE = "CREATE TABLE friend ("
                + "friendId INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "userId INTEGER NOT NULL, "
                + "friendUserId INTEGER NOT NULL, "
                + "FOREIGN KEY (userId) REFERENCES user(userId), "
                + "FOREIGN KEY (friendUserId) REFERENCES user(userId))";
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
}
