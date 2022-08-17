package com.example.bigfamilyv20.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class bulk_database_holder extends SQLiteOpenHelper {
    private static  String TABLE_NAME="bulk_database_table";
    private static  String DATABASE_NAME=" bulk.db";
    private static  String COL1="PRODUCTNO";
    private static  String COL2="NAME";
    private static  String COL3="DESCRIPTION";
    private static  String COL4="PRICE";
    private static  String COL5="AMOUNT";
    private static String newtab;

    public bulk_database_holder(Context context) {
        super(context,DATABASE_NAME, null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createtable="CREATE TABLE "+TABLE_NAME+"" +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                +COL1+" BIGINT ,"
                +COL2+" TEXT , "
                +COL3+" TEXT ,"
                +COL4+" TEXT ,"
                +COL5+" INTEGER)";
        db.execSQL(createtable);
        newtab="created";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sq="DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(sq);
        onCreate(db);
    }
}
