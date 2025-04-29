package com.example.projet.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Shopease";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase database = null;
    public DBHelper(@Nullable Context context,  @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        database = this.getWritableDatabase();
        ClientTable clientTable = ClientTable.createInstance(database);
        clientTable.createTable();
        AdressesTable adressesTable = AdressesTable.createInstance(database);
        adressesTable.createTable();
        ProduitTable produitTable = ProduitTable.createInstance(database);
        produitTable.createTable();
        BoutiqueTable boutiqueTable = BoutiqueTable.createInstance(database);
        boutiqueTable.createTable();
        ParametresTable parametresTable = ParametresTable.createInstance(database);
        parametresTable.createTable();
        PayementsTable payementsTable = PayementsTable.createInstance(database);
        payementsTable.createTable();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        database = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
