package com.example.projet.bdd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ClientTable {

    private static ClientTable INSTANCE;
    private static final String TABLE_NAME = "client";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NOM = "nom";
    private static final String COLUMN_PRENOM = "prenom";
    private static final String COLUMN_DATE_NAISSANCE = "date_naissance";
    private static final String COLUMN_TELEPHONE = "telephone";
    private static final String COLUMN_EMAIL = "email";

    private static SQLiteDatabase database;

    private ClientTable(SQLiteDatabase database) {
        this.database = database;
        createTable();
    }
    
    public static ClientTable createInstance(SQLiteDatabase database){
        if (INSTANCE == null){
            INSTANCE = new ClientTable(database);
        }
        return INSTANCE;
    }

    public static ClientTable getInstance() {
        return INSTANCE;
    }

    public void createTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_LOGIN + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_NOM + " TEXT, " +
                COLUMN_PRENOM + " TEXT, " +
                COLUMN_DATE_NAISSANCE + " TEXT, " +
                COLUMN_TELEPHONE + " TEXT, " +
                COLUMN_EMAIL + " TEXT " + ")";
        database.execSQL(createTable);
    }


    public void onDelete(){
        database.execSQL("DROP TABLE IF EXISTS 'users';");
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

        long result = database.insert(TABLE_NAME, null, values);
        ;
        return result != -1;
    }


    // SETTERS

    public boolean setLogin(String oldLogin, String newLogin){
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGIN, newLogin);

        int rows = database.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{oldLogin});
        ;
        return rows > 0;
    }


    public boolean setPassword(String login, String newPassword){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int rows = database.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        ;
        return rows > 0;
    }


    public boolean setNom(String login, String newNom){
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM, newNom);

        int rows = database.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        ;
        return rows > 0;
    }

    public boolean setPrenom(String login, String newPrenom){
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRENOM, newPrenom);

        int rows = database.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        ;
        return rows > 0;
    }

    public boolean setDateDeNaissance(String login, String newDate){
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE_NAISSANCE, newDate);

        int rows = database.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        ;
        return rows > 0;
    }

    public boolean setTel(String login, String newTel){
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_TELEPHONE, newTel);

        int rows = database.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        ;
        return rows > 0;
    }

    public boolean setEmail(String login, String newEmail){
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, newEmail);

        int rows = database.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        ;
        return rows > 0;
    }
    // GETTERS

    public String getPassword(String login) {
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(0);
            cursor.close();
        }
        return password;
    }

    public String getNom(String login) {
        
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_NOM + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(0);
            cursor.close();
        }
        return password;
    }

    public String getPrenom(String login) {
        
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_PRENOM + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(0);
            cursor.close();
        }
        return password;
    }

    public String getDateNaissance(String login) {
        
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_DATE_NAISSANCE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(0);
            cursor.close();
        }
        return password;
    }

    public String getTel(String login) {
        
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_TELEPHONE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(0);
            cursor.close();
        }
        return password;
    }

    public String getEmail(String login) {
        
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_EMAIL + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = cursor.getString(0);
            cursor.close();
        }
        return password;
    }



    // Vérifier connexion
    public boolean getClientExistence(String login, String mdp) {
        String request = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor c = database.rawQuery(request, new String[]{login, mdp});

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
        
        return database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
    }

    public boolean getUserClientLogin(String login) {
        
        Cursor c = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ? ", new String[]{login});

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
