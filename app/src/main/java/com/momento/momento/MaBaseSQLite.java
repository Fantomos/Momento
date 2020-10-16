package com.momento.momento;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MaBaseSQLite extends SQLiteOpenHelper {
    private static final String TABLE_POUTRES = "table_poutres";
    private static final String COL_ID = "ID";
    private static final String COL_NOM = "Nom";
    private static final String COL_TYPE = "Type";
    private static final String COL_LONGUEUR = "Longueur";
    private static final String COL_FORCE = "Force";
    private static final String COL_YOUNG = "Young";
    private static final String COL_INERTIE = "Inertie";
    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_POUTRES + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOM + " TEXT NOT NULL, "
            + COL_TYPE + " TEXT NOT NULL, " + COL_LONGUEUR + " DOUBLE NOT NULL, " + COL_FORCE + " DOUBLE NOT NULL, " + COL_YOUNG + " DOUBLE NOT NULL, " + COL_INERTIE + " DOUBLE NOT NULL)";

    public MaBaseSQLite(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_POUTRES + ";");
        onCreate(db);
    }
}