package com.politecnico.masterchef_pmdm_albajonathan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.politecnico.masterchef_pmdm_albajonathan.Contract.SQL_CREATE_ENTRIES;
import static com.politecnico.masterchef_pmdm_albajonathan.Contract.SQL_DELETE_ENTRIES;

public class VotacionesDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Proyecto.db";

    public VotacionesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}