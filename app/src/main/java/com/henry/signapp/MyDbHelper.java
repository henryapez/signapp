package com.henry.signapp;

/**
 * Created by Henry on 10/23/2016.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by white on 10/9/2015.
 */
public class MyDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 2;

    //GIF TABLE

    public static final String TABLE_NAME_G = "gif";
    public static final String GCOL_CAT = "category";      //Gif Category Foreign key
    public static final String GCOL_NAME = "name";         //Url name of gif ex: abouta
    public static final String GCOL_TITLE = "title";      //Title of gif in UI ex: About
    public static final String GCOL_INDECK = "inDeck";      //Gif is in my deck 0 not in, 1 is in
    public static final String GCOL_MASTERED = "mastered";  //Gif has been right swipe and is mastered 0 not mastered, 1 mastered




//    public static final String TABLE_NAME_T = "category";   //CATEGORY TABLE
//    public static final String TCOL_NAME = "name";          //Category name

//    public static final String TABLE_NAME_U = "user";       //USER TABLE
//    public static final String TCOL_EMAIL = "email";        //User email
//    public static final String TCOL_PASSWORD = "password";  //User Password
//    public static final String TCOL_PASSWORD = "password";  //User gif ID


    public static final String[] GCOLUMNS = new String[]{"_id",
            GCOL_CAT,GCOL_NAME ,GCOL_TITLE,GCOL_INDECK,GCOL_MASTERED };

    SQLiteDatabase dbb;


    private static final String STRING_CREATE_G =
            "CREATE TABLE " + TABLE_NAME_G + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + GCOL_CAT + " TEXT, " + GCOL_NAME + " TEXT, " + GCOL_TITLE
                    + " TEXT, " + GCOL_INDECK + " TEXT, " + GCOL_MASTERED + " TEXT);";


    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(STRING_CREATE_G);
        System.out.print("CREATED TABLES");


        /*


        PUT GIFS HERE



         */
//        ContentValues values = new ContentValues();
//        values.put(TCOL_NAME, "Fruits");
//        values.put(TCOL_SIZE, 0);
//        values.put(TCOL_CORRECT, 0);
//        db.insert(TABLE_NAME_T, null, values );
//        dbb = db;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_G);
        onCreate(db);
    }

    public SQLiteDatabase getDB(){
        return dbb;
    }

    public Cursor getMasteredGifs(){
        /*
        TODO: GET ALL THE GIFS THAT ARE LISTED AS MASTERED
         */

        return null;
    }

    public boolean setMasteredGifs(String signal, String gifID){
        /*
        TODO: SET THE MASTERED COLUMN TO THE SIGNAL VALUE FOR THE GIF WITH gifID
         */

        return false;
    }

    public Cursor getCategoryGifs(String category){
        /*
        TODO: GET ALL THE GIFS WITH THIS CATEGORY
         */

        return null;
    }

    public Cursor getInDeckGifs(){
        /*
        TODO: GET ALL THE GIFS THAT ARE INDECK FOR THE USER
         */

        return null;
    }

    public boolean setInDeckGifs(String signal, String gifID){
        /*
        TODO: SET THAT SPECIFIED GIF TO BE IN THE USER'S DECK
         */

//        ContentValues data=new ContentValues();
//        data.put(column,value);
//        int updated = 0;
//        updated = db.update(MyDbHelper.TABLE_NAME_Q, data, "_id=" + id, null);
//
//        return updated;

        return false;
    }




    public long savetoDB( ){
        /*

        STORE EVERYTHING TO THE DATABASE FORM EXCELL SHEET

         */

        return 0;

    }




}
