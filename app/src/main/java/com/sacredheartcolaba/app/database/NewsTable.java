package com.sacredheartcolaba.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.sacredheartcolaba.app.main_fragment.news.News;

import java.util.ArrayList;
import java.util.List;

public class NewsTable extends DatabaseHelper<News> {

    private static final String TAG = "NewsTable";

    public NewsTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        super.onUpgrade(db, i, i1);
    }

    @Override
    public void addRow(News e) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ID, e.getId()); // Events ID
            values.put(KEY_NEWS_BODY, e.getBody()); // Events body
            values.put(KEY_NEWS_AUTHOR, e.getAuthor()); // Events author
            values.put(KEY_NEWS_MODIFIED_ON, e.getDate()); // Events modified date

            // Inserting Row
            db.insert(TABLE_NEWS, null, values);
            //2nd argument is String containing nullColumnHack
        } catch (SQLiteConstraintException ignore) {
        } finally {
            if (db != null) {
                if (db.isOpen())
                    db.close(); // Closing database connection
            }
        }
    }

    @Override
    public News getOneRow(int id) {
        News news = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            cursor = db.query
                    (
                            TABLE_NEWS,
                            new String[]
                                    {
                                            KEY_ID,
                                            KEY_NEWS_BODY,
                                            KEY_NEWS_AUTHOR,
                                            KEY_NEWS_MODIFIED_ON
                                    },
                            KEY_ID + "=?",
                            new String[]
                                    {
                                            String.valueOf(id)
                                    },
                            null,
                            null,
                            null,
                            null
                    );

            if (cursor != null) {
                cursor.moveToFirst();
                news = new News(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            }
        } catch (Exception e) {
            news = null;
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            if (db != null && db.isOpen())
                db.close();
        }
        // return news
        return news;
    }

    @Override
    public List<News> getAllRows() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<News> newsList;
        try {
            newsList = new ArrayList<>();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_NEWS;

            db = this.getWritableDatabase();
            String[] columns = {KEY_ID, KEY_NEWS_BODY, KEY_NEWS_AUTHOR, KEY_NEWS_MODIFIED_ON};
            cursor = db.query(TABLE_NEWS, columns, null, null, null, null, KEY_ID + " DESC");

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    News news = new News(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                    // Adding news to list
                    newsList.add(news);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen())
                db.close();
        }

        return newsList;
    }

    @Override
    public int updateRow(News news) {
        SQLiteDatabase db = null;
        int update;
        try {
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_NEWS_BODY, news.getBody());
            values.put(KEY_NEWS_AUTHOR, news.getAuthor());
            values.put(KEY_NEWS_MODIFIED_ON, news.getDate());

            update = db.update(TABLE_NEWS, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(news.getId())});
        } finally {
            if (db != null && db.isOpen())
                db.close();
        }
        // updating row
        return update;
    }

    @Override
    public void deleteRow(News news) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.delete(TABLE_NEWS, KEY_ID + " = ?",
                    new String[]{String.valueOf(news.getId())});
        } finally {
            if (db != null && db.isOpen())
                db.close();
        }
    }

    @Override
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NEWS;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        int count;
        try {

            db = this.getReadableDatabase();
            cursor = db.rawQuery(countQuery, null);
            count = cursor.getCount();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen())
                db.close();
        }

        // return count
        return count;
    }

    @Override
    public int getLastId() {
        List<News> newses = getAllRows();

        int lastNewsId = 0;
        for (News news : newses) {
            if (news.getId() > 0)
                lastNewsId = news.getId();
        }
        return lastNewsId;
    }
}
