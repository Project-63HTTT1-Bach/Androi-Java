package com.example.quizapp.sqlliteopenhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import com.example.quizapp.models.User;

public class SqliteOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "QuizApp.db";
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_COLUMN_ID = "id";
    public static final String USERS_COLUMN_FULL_NAME = "fullname";
    //    public static final String USERS_COLUMN_USERNAME = "username";
    public static final String USERS_COLUMN_PASSWORD = "password";
    public static final String USERS_COLUMN_EMAIL = "email";
    public static final String USERS_COLUMN_BIRTHDAY = "birthday";
    public static final String USERS_COLUMN_PHONE = "phone";


    public SqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USERS_TABLE_NAME + " (" + USERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERS_COLUMN_FULL_NAME + " TEXT, " + USERS_COLUMN_PASSWORD + " TEXT, " + USERS_COLUMN_EMAIL + " TEXT, " + USERS_COLUMN_BIRTHDAY + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUser(String fullname, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_FULL_NAME, fullname);
//        contentValues.put(USERS_COLUMN_USERNAME, username);
        contentValues.put(USERS_COLUMN_PASSWORD, password);
        contentValues.put(USERS_COLUMN_EMAIL, email);
        db.insert(USERS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateUser(String fullname, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(USERS_COLUMN_USERNAME, username);
        contentValues.put(USERS_COLUMN_FULL_NAME, fullname);
        contentValues.put(USERS_COLUMN_PASSWORD, password);
        contentValues.put(USERS_COLUMN_EMAIL, email);
        db.update(USERS_TABLE_NAME, contentValues, USERS_COLUMN_FULL_NAME + " = ? ", new String[]{fullname});
        return true;
    }

    public Integer deleteUser(String fullname) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USERS_TABLE_NAME, USERS_COLUMN_FULL_NAME + " = ? ", new String[]{fullname});
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + USERS_TABLE_NAME, null);

        if (res != null && res.moveToFirst()) {
            int fullnameIndex = res.getColumnIndex(USERS_COLUMN_FULL_NAME);
//            int usernameIndex = res.getColumnIndex(USERS_COLUMN_USERNAME);
            int passwordIndex = res.getColumnIndex(USERS_COLUMN_PASSWORD);
            int emailIndex = res.getColumnIndex(USERS_COLUMN_EMAIL);
            int birthdayIndex = res.getColumnIndex(USERS_COLUMN_BIRTHDAY);
            int phoneIndex = res.getColumnIndex(USERS_COLUMN_PHONE);

            if (fullnameIndex != -1 && passwordIndex != -1 && emailIndex != -1 && birthdayIndex != -1 && phoneIndex != 1) {
                do {
                    String fullname = res.getString(fullnameIndex);
//                    String username = res.getString(usernameIndex);
                    String password = res.getString(passwordIndex);
                    String email = res.getString(emailIndex);
                    String birthday = res.getString(birthdayIndex);
                    String phone = res.getString(phoneIndex);
                    User user = new User(fullname, password, email, birthday, phone);
                    array_list.add(user);
                } while (res.moveToNext());
            }

            res.close();
        }

        return array_list;
    }
}