package com.example.projet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class BDDclient extends SQLiteOpenHelper implements Serializable {
    private static final String DATABASE_NAME = "client";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "client";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NOM = "nom";
    private static final String COLUMN_PRENOM = "prenom";
    private static final String COLUMN_DATE_NAISSANCE = "date_naissance";
    private static final String COLUMN_TELEPHONE = "telephone";
    private static final String COLUMN_EMAIL = "email";

    public SQLiteDatabase db;

    public BDDclient(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public BDDclient(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_LOGIN + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_NOM + " TEXT, " +
                COLUMN_PRENOM + " TEXT, " +
                COLUMN_DATE_NAISSANCE + " TEXT, " +
                COLUMN_TELEPHONE + " TEXT, " +
                COLUMN_EMAIL + " TEXT " + ")";
        db.execSQL(createTable);
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
        this.db = this.getWritableDatabase();
        return this.db;
    }


    public boolean insertUser(String login, String password, String nom, String prenom, String date_naissance, String email) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGIN, login);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_NOM, nom);
        values.put(COLUMN_PRENOM, prenom);
        values.put(COLUMN_DATE_NAISSANCE, date_naissance);
        values.put(COLUMN_TELEPHONE, "");
        values.put(COLUMN_EMAIL, email);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }


    // SETTERS

    public boolean setLogin(String oldLogin, String newLogin){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGIN, newLogin);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{oldLogin});
        db.close();
        return rows > 0;
    }


    public boolean setPassword(String login, String newPassword){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        db.close();
        return rows > 0;
    }


    public boolean setNom(String login, String newNom){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM, newNom);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        db.close();
        return rows > 0;
    }

    public boolean setPrenom(String login, String newPrenom){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRENOM, newPrenom);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        db.close();
        return rows > 0;
    }

    public boolean setDateDeNaissance(String login, String newDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE_NAISSANCE, newDate);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        db.close();
        return rows > 0;
    }

    public boolean setTel(String login, String newTel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TELEPHONE, newTel);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        db.close();
        return rows > 0;
    }

    public boolean setEmail(String login, String newEmail){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, newEmail);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        db.close();
        return rows > 0;
    }



    // GETTERS

    public String getPassword(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(0);
            cursor.close();
        }
        return password;
    }

    public String getNom(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NOM + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(0);
            cursor.close();
        }
        return password;
    }

    public String getPrenom(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PRENOM + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(0);
            cursor.close();
        }
        return password;
    }

    public String getDateNaissance(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_DATE_NAISSANCE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(0);
            cursor.close();
        }
        return password;
    }

    public String getTel(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_TELEPHONE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(0);
            cursor.close();
        }
        return password;
    }

    public String getEmail(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_EMAIL + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(0);
            cursor.close();
        }
        return password;
    }



    // Vérifier connexion
    public boolean getClientExistence(String login, String mdp) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{login, mdp});

        if (c.moveToFirst()) {
            String foundLogin = c.getString(c.getColumnIndexOrThrow(COLUMN_LOGIN));
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }





    // AUTRES

    public Cursor getUserInfo(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
    }



    public boolean getUserClientLogin(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ? ", new String[]{login});

        if (c.moveToFirst()) {
            String foundLogin = c.getString(c.getColumnIndexOrThrow(COLUMN_LOGIN));
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

}
