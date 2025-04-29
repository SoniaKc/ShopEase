package com.example.projet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class BDDboutique extends SQLiteOpenHelper implements Serializable {
    private static final String DATABASE_NAME = "boutique";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "boutique";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NOM = "nom";
    private static final String COLUMN_TEL = "tel";
    private static final String COLUMN_SIRET = "siret";
    private static final String COLUMN_FORME_JURIDIQUE = "forme_juridique";
    private static final String COLUMN_SIEGE_SOCIAL = "siege_social";
    private static final String COLUMN_PAYS_ENREGISTREMENT = "pays_enregistrement";
    private static final String COLUMN_IBAN = "iban";

    public SQLiteDatabase db;

    public BDDboutique(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public BDDboutique(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_LOGIN + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_NOM + " TEXT, " +
                COLUMN_TEL + "TEXT,"+
                COLUMN_SIRET + " TEXT, " +
                COLUMN_FORME_JURIDIQUE + " TEXT, " +
                COLUMN_SIEGE_SOCIAL + " TEXT, " +
                COLUMN_PAYS_ENREGISTREMENT + " TEXT, " +
                COLUMN_IBAN + " TEXT " + ")";
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
        this.db = this.getWritableDatabase();
        return this.db;
    }

    public boolean insertUser(String login, String password, String nom, String siret, String forme_juridique) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGIN, login);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_NOM, nom);
        values.put(COLUMN_TEL,"");
        values.put(COLUMN_SIRET, siret);
        values.put(COLUMN_FORME_JURIDIQUE, forme_juridique);
        values.put(COLUMN_SIEGE_SOCIAL, "");
        values.put(COLUMN_PAYS_ENREGISTREMENT, "");
        values.put(COLUMN_IBAN, "");

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }


    public Cursor getBoutiqueInfo(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
    }


    public String getBoutiqueExistence(String login, String mdp) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{login, mdp});

        if (c.moveToFirst()) {
            String foundLogin = c.getString(c.getColumnIndexOrThrow(COLUMN_LOGIN));
            c.close();
            return foundLogin;
        } else {
            c.close();
            return "Error!";
        }
    }

// SETTERS
    public boolean setLogin(String login, String newlogin){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGIN, newlogin);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        db.close();
        return rows > 0;
    }

    public boolean setTel(String login, String tel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEL, tel);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        db.close();
        return rows > 0;
    }


    public boolean setPassword(String login, String newPassword){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});  // Correction ici
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

    public boolean setSiret(String login, String newSiret){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SIRET, newSiret);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        db.close();
        return rows > 0;
    }

    public boolean setFormeJuridique(String login, String newFormeJuridique){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FORME_JURIDIQUE, newFormeJuridique);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        db.close();
        return rows > 0;
    }

    public boolean setSiegeSocial(String login, String newSiegeSocial){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SIEGE_SOCIAL, newSiegeSocial);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        db.close();
        return rows > 0;
    }

    public boolean setPaysEnregistrement(String login, String newPays){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYS_ENREGISTREMENT, newPays);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        db.close();
        return rows > 0;
    }

    public boolean setIban(String login, String newIban){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IBAN, newIban);

        int rows = db.update(TABLE_NAME, values, COLUMN_LOGIN + " = ?", new String[]{login});
        db.close();
        return rows > 0;
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

    // GETTERS
    public String getBoutiqueID(String login, String mdp) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NOM + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String id = null;
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getString(0);
            cursor.close();
        }
        return id;
    }

    public String getNom(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NOM + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?",new String[]{login});
        String nom = null;
        if (cursor != null && cursor.moveToFirst()) {
            nom = cursor.getString(0);
            cursor.close();
        }
        return nom;
    }

    public String getSiret(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_SIRET + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String siret = null;
        if (cursor != null && cursor.moveToFirst()) {
            siret = cursor.getString(0);
            cursor.close();
        }
        return siret;
    }

    public String getFormeJuridique(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_FORME_JURIDIQUE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String forme = null;
        if (cursor != null && cursor.moveToFirst()) {
            forme = cursor.getString(0);
            cursor.close();
        }
        return forme;
    }

    public String getSiegeSocial(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_SIEGE_SOCIAL + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String siege = null;
        if (cursor != null && cursor.moveToFirst()) {
            siege = cursor.getString(0);
            cursor.close();
        }
        return siege;
    }

    public String getPaysEnregistrement(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PAYS_ENREGISTREMENT + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String pays = null;
        if (cursor != null && cursor.moveToFirst()) {
            pays = cursor.getString(0);
            cursor.close();
        }
        return pays;
    }

    public String getIban(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_IBAN + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String iban = null;
        if (cursor != null && cursor.moveToFirst()) {
            iban = cursor.getString(0);
            cursor.close();
        }
        return iban;
    }

    public String getTel(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_TEL + " FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGIN + " = ?", new String[]{login});
        String tel = null;
        if (cursor != null && cursor.moveToFirst()) {
            tel = cursor.getString(0);
            cursor.close();
        }
        return tel;
    }

    public boolean getShopExistence(String login, String mdp) {
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

}
