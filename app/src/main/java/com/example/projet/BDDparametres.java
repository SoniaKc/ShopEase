package com.example.projet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class BDDparametres extends SQLiteOpenHelper implements Serializable {
    private static final String DATABASE_NAME = "Params";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "params";

    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_LANGUE = "langue";
    private static final String COLUMN_COOKIES = "cookies";
    private static final String COLUMN_NOTIFICATIONS= "notifications";

    public SQLiteDatabase db;

    public BDDparametres(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public BDDparametres(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_LOGIN + " TEXT , " +
                COLUMN_TYPE + " TEXT , " +
                COLUMN_LANGUE + " TEXT, " +
                COLUMN_COOKIES + " TEXT, " +
                COLUMN_NOTIFICATIONS + " TEXT, " +
                "PRIMARY KEY (" + COLUMN_LOGIN + ", " + COLUMN_TYPE + ")" +
                ")";
        this.db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void onDelete(){
        db.execSQL("DROP TABLE IF EXISTS 'users';");
    }

    public SQLiteDatabase open(){
        return this.getWritableDatabase();
    }

    public boolean insertUser(String login, String type) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGIN, login);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_LANGUE, "français");
        values.put(COLUMN_COOKIES, "acceptés");
        values.put(COLUMN_NOTIFICATIONS, "");

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }


    // SETTERS

    public boolean setLangue(String login, String type, String newLangue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LANGUE, newLangue);

        int rows = db.update(TABLE_NAME, values,
                COLUMN_LOGIN + " = ? AND " + COLUMN_TYPE + " = ?",
                new String[]{login, type});
        db.close();
        return rows > 0;
    }

    public boolean setNotifications(String login, String type, String newNotifications) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFICATIONS, newNotifications);

        int rows = db.update(TABLE_NAME, values,
                COLUMN_LOGIN + " = ? AND " + COLUMN_TYPE + " = ?",
                new String[]{login, type});
        db.close();
        return rows > 0;
    }

    public boolean setCookies(String login, String type, String newCookies) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COOKIES, newCookies);

        int rows = db.update(TABLE_NAME, values,
                COLUMN_LOGIN + " = ? AND " + COLUMN_TYPE + " = ?",
                new String[]{login, type});
        db.close();
        return rows > 0;
    }




    // GETTERS

    public String getLangue(String login, String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_LANGUE + " FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_LOGIN + " = ? AND " + COLUMN_TYPE + " = ?",
                new String[]{login, type});

        String langue = null;
        if (cursor != null && cursor.moveToFirst()) {
            langue = cursor.getString(0);
            cursor.close();
        }
        return langue;
    }

    public String getNotifications(String login, String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_NOTIFICATIONS + " FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_LOGIN + " = ? AND " + COLUMN_TYPE + " = ?",
                new String[]{login, type});

        String notifications = null;
        if (cursor != null && cursor.moveToFirst()) {
            notifications = cursor.getString(0);
            cursor.close();
        }
        return notifications;
    }

    public String getCookies(String login, String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_COOKIES + " FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_LOGIN + " = ? AND " + COLUMN_TYPE + " = ?",
                new String[]{login, type});

        String cookies = null;
        if (cursor != null && cursor.moveToFirst()) {
            cookies = cursor.getString(0);
            cursor.close();
        }
        return cookies;
    }

}
