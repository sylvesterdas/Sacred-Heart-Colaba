package com.sacredheartcolaba.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sacredheartcolaba.app.extras.DataModel;


abstract class DatabaseHelper<T extends DataModel> extends SQLiteOpenHelper implements DatabaseDefaults<T> {

    DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NEWS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NEWS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NEWS_BODY + " TEXT,"
                + KEY_NEWS_AUTHOR + " TEXT,"
                + KEY_NEWS_MODIFIED_ON + " DATETIME" + ")";
        db.execSQL(CREATE_NEWS_TABLE);
        String CREATE_EVENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_EVENTS_BODY + " TEXT,"
                + KEY_EVENTS_AUTHOR + " TEXT,"
                + KEY_EVENTS_MODIFIED_ON + " DATETIME" + ")";
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
        // Create tables again
        onCreate(db);

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        // Create tables again
        onCreate(db);
    }
}
