package com.zybooks.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int VERSION = 1;

    public UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    };

    public static final class UserTable {
        public static final String TABLE = "users";
        public static final String COL_ID = "_id";
        public static final String COL_USERNAME = "username";
        public static final String COL_PASSWORD = "password";
    };

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("create table " + UserTable.TABLE + " (" +
                UserTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserTable.COL_USERNAME + " text, " +
                UserTable.COL_PASSWORD + " text)");
    };

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE);
        onCreate(db);
    };
};
