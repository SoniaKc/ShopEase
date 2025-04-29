package com.example.projet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class BDDpayments extends SQLiteOpenHelper implements Serializable {
    private static final String DATABASE_NAME = "Payements";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "payements";

    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_NOM_CARTE = "nom_carte";
    private static final String COLUMN_NOM_PERSONNE_CARTE = "personne_carte";
    private static final String COLUMN_CVC = "cvc";
    private static final String COLUMN_DATE_EXPIRATION = "date_expiration";

    public SQLiteDatabase db;

    public BDDpayments(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public BDDpayments(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_LOGIN + " TEXT, " +
                COLUMN_NOM_CARTE + " TEXT, " +
                COLUMN_NOM_PERSONNE_CARTE + " TEXT, " +
                COLUMN_CVC + " TEXT, " +
                COLUMN_DATE_EXPIRATION + " TEXT, " +
                "PRIMARY KEY (" + COLUMN_LOGIN + ", " + COLUMN_NOM_CARTE + ")" +
                ")";
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
        return this.getWritableDatabase();
    }

    public boolean insertCard(String login, String nom_carte, String personne_carte, String cvc, String date_expiration) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGIN, login);
        values.put(COLUMN_NOM_CARTE, nom_carte);
        values.put(COLUMN_NOM_PERSONNE_CARTE, personne_carte);
        values.put(COLUMN_CVC, cvc);
        values.put(COLUMN_DATE_EXPIRATION, date_expiration);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }


    // SETTERS

    public boolean setNomCarte(String login, String oldNomCarte, String newNomCarte) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM_CARTE, newNomCarte);

        int rows = db.update( TABLE_NAME, values, COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_CARTE + " = ?",
                new String[]{login, oldNomCarte});
        db.close();
        return rows > 0;
    }



    public boolean setNomPersonneCarte(String login, String NomCarte, String newNomPersonneCarte) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM_CARTE, newNomPersonneCarte);

        int rows = db.update( TABLE_NAME, values, COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_CARTE + " = ?",
                new String[]{login, NomCarte});
        db.close();
        return rows > 0;
    }


    public boolean setCvc(String login, String NomCarte, String newCvc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM_CARTE, newCvc);

        int rows = db.update( TABLE_NAME, values, COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_CARTE + " = ?",
                new String[]{login, NomCarte});
        db.close();
        return rows > 0;
    }

    public boolean setDateExpiration(String login, String NomCarte, String newDateExpiration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM_CARTE, newDateExpiration);

        int rows = db.update( TABLE_NAME, values, COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_CARTE + " = ?",
                new String[]{login, NomCarte});
        db.close();
        return rows > 0;
    }





    // GETTERS

    public String getNomPersonneCarte(String login, String nomCarte) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NOM_PERSONNE_CARTE + " FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_CARTE + " = ?",
                new String[]{login, nomCarte});

        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(0);
            cursor.close();
        }
        return result;
    }

    public String getCvc(String login, String nomCarte) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CVC + " FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_CARTE + " = ?",
                new String[]{login, nomCarte});

        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(0);
            cursor.close();
        }
        return result;
    }

    public String getDateExpiration(String login, String nomCarte) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_DATE_EXPIRATION + " FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_LOGIN + " = ? AND " + COLUMN_NOM_CARTE + " = ?",
                new String[]{login, nomCarte});

        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(0);
            cursor.close();
        }
        return result;
    }


    public String getListeNomsCartes(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_NOM_CARTE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?",
                new String[]{login}
        );

        StringBuilder nomsCartes = new StringBuilder();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nomCarte = cursor.getString(0);
                nomsCartes.append(nomCarte).append(", ");
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (nomsCartes.length() > 2) {
            nomsCartes.setLength(nomsCartes.length() - 2);
        }

        return nomsCartes.toString();
    }

}
