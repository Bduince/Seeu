package com.eb.seeu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class OrderDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Data.db";
    public static final String TABLE_NAME = "Friends_list";
    public static final String TABLE_NAME2 = "Enemies_list";

    public OrderDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists " + TABLE_NAME + " (Num text primary key, CustomName text, Latitude text, Longitude text)";
        sqLiteDatabase.execSQL(sql);

        String sql2 = "create table if not exists " + TABLE_NAME2 + " (Num text primary key, CustomName text, Latitude text, Longitude text)";
        sqLiteDatabase.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        String sql2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;
        sqLiteDatabase.execSQL(sql2);
        onCreate(sqLiteDatabase);
    }
}
