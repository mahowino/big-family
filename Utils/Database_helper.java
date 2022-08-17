package com.example.bigfamilyv20.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database_helper extends SQLiteOpenHelper {
    private static  String TABLE_NAME="basket";
    private static  String DATABASE_NAME="basket.db";
    private static  String COL1="PRODUCTNO";
    private static  String COL2="NAME";
    private static  String COL3="DESCRIPTION";
    private static  String COL4="PRICE";
    private static  String COL5="AMOUNT";
    private static String newtab;

    public Database_helper(Context context) {
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
    /*
    public boolean adddata(String name,String product_id,String amount,String price){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
       // values.put(COL1,id);
        values.put(COL2,name);
        values.put(COL3,product_id);
        values.put(COL4,price);
       // values.put(COL5,price);
        long result=db.insert(TABLE_NAME,null,values);
        if(result==-1){
            return false;
        }
        else {
            return true;

        }
    }
    */
    public boolean adddata(Long id,String name,String description,String price,int amount){
        Log.i("dbissuer", "adddata: "+newtab);
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
         values.put(COL1,id);
        values.put(COL2,name);
        values.put(COL3,description);
        values.put(COL4,price);
        values.put(COL5,amount);
        long result=db.insert(TABLE_NAME,null,values);
        if(result==-1){
            return false;
        }
        else {
            return true;

        }
    }
    public  Cursor getdata(SQLiteDatabase db){
        String query="SELECT * FROM "+TABLE_NAME;
        Cursor DATA=db.rawQuery(query,null);
        return DATA;
    }
    public  Cursor getdata(SQLiteDatabase db,String query){
       // String query="SELECT * FROM "+TABLE_NAME;
        Cursor DATA=db.rawQuery(query,null);
        return DATA;
    }
    public boolean clearTable()
    {
        SQLiteDatabase db=getWritableDatabase();
        String query="DELETE FROM "+TABLE_NAME;
        db.execSQL(query);
       // long result=db.delete(query,null,null);
        return true;
    }
    public void deleteRecord( Long id)
    {
        SQLiteDatabase db=getWritableDatabase();
        String query="DELETE FROM "+TABLE_NAME +" WHERE PRODUCTNO = "+id;
        db.execSQL(query);
        // long result=db.delete(query,null,null);

    }
    public void EditRecord(String COLUM,int Data,Long ID)
    {
        SQLiteDatabase db=getWritableDatabase();
        String query="UPDATE "+TABLE_NAME+"  SET "+COLUM+" = "+Data+" WHERE "+COL1+" = "+ID ;
        db.execSQL(query);

    }
}
