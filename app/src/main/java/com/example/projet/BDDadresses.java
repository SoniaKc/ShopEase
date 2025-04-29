package com.example.projet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class BDDadresses extends SQLiteOpenHelper implements Serializable {
    private static final String DATABASE_NAME = "Adresses";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "adresses";

    private static final String COLUMN_LOGIN= "login";
    private static final String COLUMN_NOM_ADRESSE = "nom_adresse";
    private static final String COLUMN_NUMERO = "numero";
    private static final String COLUMN_NOM_RUE = "nom_rue";
    private static final String COLUMN_CODE_POSTAL = "code_postal";
    private static final String COLUMN_VILLE = "ville";
    private static final String COLUMN_PAYS = "pays";

    public SQLiteDatabase db;

    public BDDadresses(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public BDDadresses(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_LOGIN + " STRING, " +
                COLUMN_NOM_ADRESSE + " TEXT, " +
                COLUMN_NUMERO + " TEXT, " +
                COLUMN_NOM_RUE + " TEXT, " +
                COLUMN_CODE_POSTAL + " TEXT, " +
                COLUMN_VILLE + " TEXT, " +
                COLUMN_PAYS + " TEXT, "+
                "PRIMARY KEY (" + COLUMN_LOGIN + ", " + COLUMN_NOM_ADRESSE + ")" +
                ")";;
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

    public boolean insertUser(String login ,String nom_adresse, String numero, String nom_rue, String code_postal, String ville, String pays) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGIN, login);
        values.put(COLUMN_NOM_ADRESSE, nom_adresse);
        values.put(COLUMN_NUMERO, numero);
        values.put(COLUMN_NOM_RUE, nom_rue);
        values.put(COLUMN_CODE_POSTAL, code_postal);
        values.put(COLUMN_VILLE, ville);
        values.put(COLUMN_PAYS, pays);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }


    // SETTERS

    public boolean setNumero(String login, String nom_adresse, String newNumero) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMERO, newNumero);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_ADRESSE + " = ?",
                new String[]{login, nom_adresse});
        db.close();
        return rows > 0;
    }

    public boolean setNomRue(String login, String nom_adresse, String newNomRue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM_RUE, newNomRue);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_ADRESSE + " = ?",
                new String[]{login, nom_adresse});
        db.close();
        return rows > 0;
    }

    public boolean setCodePostal(String login, String nom_adresse, String newCodePostal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE_POSTAL, newCodePostal);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_ADRESSE + " = ?",
                new String[]{login, nom_adresse});
        db.close();
        return rows > 0;
    }

    public boolean setVille(String login, String nom_adresse, String newVille) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VILLE, newVille);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_ADRESSE + " = ?",
                new String[]{login, nom_adresse});
        db.close();
        return rows > 0;
    }

    public boolean setPays(String login, String nom_adresse, String newPays) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYS, newPays);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_ADRESSE + " = ?",
                new String[]{login, nom_adresse});
        db.close();
        return rows > 0;
    }

    public boolean setNomAdresse(String login, String oldNomAdresse, String newNomAdresse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM_ADRESSE, newNomAdresse);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_ADRESSE + " = ?",
                new String[]{login, oldNomAdresse});
        db.close();
        return rows > 0;
    }




    // GETTERS

    public String getNumero(String login, String nom_adresse) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NUMERO + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_ADRESSE + " = ?",
                new String[]{login, nom_adresse});
        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(0);
            cursor.close();
        }
        return result;
    }

    public String getNomRue(String login, String nom_adresse) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NOM_RUE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_ADRESSE + " = ?",
                new String[]{login, nom_adresse});
        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(0);
            cursor.close();
        }
        return result;
    }

    public String getCodePostal(String login, String nom_adresse) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CODE_POSTAL + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_ADRESSE + " = ?",
                new String[]{login, nom_adresse});
        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(0);
            cursor.close();
        }
        return result;
    }

    public String getVille(String login, String nom_adresse) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_VILLE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_ADRESSE + " = ?",
                new String[]{login, nom_adresse});
        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(0);
            cursor.close();
        }
        return result;
    }

    public String getPays(String login, String nom_adresse) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PAYS + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_ADRESSE + " = ?",
                new String[]{login, nom_adresse});
        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(0);
            cursor.close();
        }
        return result;
    }




    public String getListeNomsAdresses(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_NOM_ADRESSE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?",
                new String[]{login}
        );

        StringBuilder nomsAdresses = new StringBuilder();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                nomsAdresses.append(cursor.getString(0)).append(", ");
            }
            cursor.close();
        }

        if (nomsAdresses.length() > 2) {
            nomsAdresses.setLength(nomsAdresses.length() - 2); // remove last comma & space
        }

        return nomsAdresses.toString();
    }


}
