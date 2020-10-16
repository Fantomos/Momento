package com.momento.momento;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class PoutresDBB {
    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "poutres.db";
    private static final String TABLE_POUTRES = "table_poutres";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NOM = "Nom";
    private static final int NUM_COL_NOM = 1;
    private static final String COL_TYPE = "Type";
    private static final int NUM_COL_TYPE = 2;
    private static final String COL_LONGUEUR = "Longueur";
    private static final int NUM_COL_LONGUEUR = 3;
    private static final String COL_FORCE = "Force";
    private static final int NUM_COL_FORCE = 4;
    private static final String COL_YOUNG = "Young";
    private static final int NUM_COL_YOUNG = 5;
    private static final String COL_INERTIE = "Inertie";
    private static final int NUM_COL_INERTIE = 6;
    private SQLiteDatabase bdd;
    private MaBaseSQLite maBaseSQLite;

    public PoutresDBB(Context context){
        //On crée la BDD et sa table
        maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertPoutre(Poutre poutre){
        ContentValues values = new ContentValues();
        values.put(COL_NOM,  poutre.getNom());
        values.put(COL_TYPE, poutre.getType());
        values.put(COL_LONGUEUR, poutre.getLongueur());
        values.put(COL_FORCE, poutre.getForce());
        values.put(COL_YOUNG, poutre.getYoung());
        values.put(COL_INERTIE, poutre.getInertie());
        return bdd.insert(TABLE_POUTRES, null, values);
    }

    public int updatePoutre(Poutre poutre){
        ContentValues values = new ContentValues();
        values.put(COL_NOM,  poutre.getNom());
        values.put(COL_TYPE, poutre.getType());
        values.put(COL_LONGUEUR, poutre.getLongueur());
        values.put(COL_FORCE, poutre.getForce());
        values.put(COL_YOUNG, poutre.getYoung());
        values.put(COL_INERTIE, poutre.getInertie());
        return bdd.update(TABLE_POUTRES, values, COL_ID + " = " +poutre.getId(), null);
    }

    public int removePoutreAvecID(int id){
        return bdd.delete(TABLE_POUTRES, COL_ID + " = " +id, null);
    }

    public Poutre getPoutresAvecID(int id){
        Cursor c = bdd.query(TABLE_POUTRES, new String[] {COL_ID, COL_NOM, COL_TYPE, COL_LONGUEUR, COL_FORCE, COL_YOUNG, COL_INERTIE}, COL_ID + " = " +id, null, null, null, null);
        c.moveToFirst();
        Poutre result = cursorToPoutre(c);
        c.close();
        return result;
    }

    public ArrayList<Poutre> getAllPoutres(){
        Cursor c = bdd.query(TABLE_POUTRES, new String[] {COL_ID, COL_NOM, COL_TYPE, COL_LONGUEUR, COL_FORCE, COL_YOUNG, COL_INERTIE}, null, null, null, null, null);
        ArrayList<Poutre> listPoutre = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                listPoutre.add(cursorToPoutre(c));
            }
        }
        finally{
                c.close();
        }
        return listPoutre;
    }

    private Poutre cursorToPoutre(Cursor c){
        if (c.getCount() == 0)
            return null;
        Poutre poutre = new Poutre(c.getInt(NUM_COL_ID),c.getString(NUM_COL_NOM),c.getInt(NUM_COL_TYPE),c.getDouble(NUM_COL_LONGUEUR),c.getDouble(NUM_COL_FORCE),c.getDouble(NUM_COL_YOUNG),c.getDouble(NUM_COL_INERTIE));
        return poutre;
    }
}