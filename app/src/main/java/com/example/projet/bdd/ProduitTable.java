package com.example.projet.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class ProduitTable{
    private static ProduitTable INSTANCE;
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "users";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ID_BOUTIQUE = "id_boutique";
    private static final String COLUMN_NOM = "nom";
    private static final String COLUMN_CATEGORIES = "categories";
    private static final String COLUMN_REDUCTION = "reduction";
    private static final String COLUMN_PRIX = "prix";
    private static final String COLUMN_DESCRIPTION = "description";

    private static SQLiteDatabase database;

    private ProduitTable(SQLiteDatabase database) {
        this.database = database;
        createTable();
    }

    public static ProduitTable createInstance(SQLiteDatabase database){
        if (INSTANCE == null){
            INSTANCE = new ProduitTable(database);
        }
        return INSTANCE;
    }

    public static ProduitTable getInstance() {
        return INSTANCE;
    }

    public void createTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID_BOUTIQUE + "INTEGER," +
                COLUMN_NOM + " TEXT, " +
                COLUMN_CATEGORIES + " TEXT, " +
                COLUMN_REDUCTION + " TEXT, " +
                COLUMN_PRIX + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT " + ")";
        database.execSQL(createTable);
    }

    public void onDelete(){
        database.execSQL("DROP TABLE IF EXISTS 'users';");
    }


    public boolean insertProduit(Integer id_boutique, String nom, String categorie, String prix, String description) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_BOUTIQUE, id_boutique);
        values.put(COLUMN_NOM, nom);
        values.put(COLUMN_CATEGORIES, categorie);
        values.put(COLUMN_REDUCTION, "");
        values.put(COLUMN_PRIX, prix);
        values.put(COLUMN_DESCRIPTION, description);

        long result = database.insert(TABLE_NAME, null, values);
        ;
        return result != -1;
    }


    public Cursor getProduitInfo(Integer id) {
        return database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }


    // SETTERD

    public boolean setNom(Integer id, String nom) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM, nom);
        int rows = database.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        ;
        return rows > 0;
    }

    public boolean setCategorie(Integer id, String categorie) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORIES, categorie);
        int rows = database.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        ;
        return rows > 0;
    }

    public boolean setReduction(Integer id, String reduction) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_REDUCTION, reduction);
        int rows = database.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        ;
        return rows > 0;
    }

    public boolean setPrix(Integer id, String prix) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRIX, prix);
        int rows = database.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        ;
        return rows > 0;
    }

    public boolean setDescription(Integer id, String description) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, description);
        int rows = database.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        ;
        return rows > 0;
    }

    public boolean setIdBoutique(Integer id, int idBoutique) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_BOUTIQUE, idBoutique);
        int rows = database.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        ;
        return rows > 0;
    }

    // GETTERS
    public String getNom(Integer id) {
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_NOM + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        String nom = null;
        if (cursor != null && cursor.moveToFirst()) {
            nom = cursor.getString(0);
            cursor.close();
        }
        return nom;
    }

    public String getCategorie(Integer id) {
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_CATEGORIES + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        String categorie = null;
        if (cursor != null && cursor.moveToFirst()) {
            categorie = cursor.getString(0);
            cursor.close();
        }
        return categorie;
    }

    public String getReduction(Integer id) {
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_REDUCTION + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        String reduction = null;
        if (cursor != null && cursor.moveToFirst()) {
            reduction = cursor.getString(0);
            cursor.close();
        }
        return reduction;
    }

    public String getPrix(Integer id) {
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_PRIX + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        String prix = null;
        if (cursor != null && cursor.moveToFirst()) {
            prix = cursor.getString(0);
            cursor.close();
        }
        return prix;
    }

    public String getDescription(Integer id) {
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_DESCRIPTION + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        String description = null;
        if (cursor != null && cursor.moveToFirst()) {
            description = cursor.getString(0);
            cursor.close();
        }
        return description;
    }

    public int getIdBoutique(Integer id) {
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_ID_BOUTIQUE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        int idBoutique = -1;
        if (cursor != null && cursor.moveToFirst()) {
            idBoutique = cursor.getInt(0);
            cursor.close();
        }
        return idBoutique;
    }


}
