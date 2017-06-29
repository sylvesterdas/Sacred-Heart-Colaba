package com.sacredheartcolaba.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sacredheartcolaba.app.main_fragment.events.Events;

import java.util.ArrayList;
import java.util.List;

public class EventsTable extends DatabaseHelper<Events> {

    private static final String TAG = "EventsTable";

    public EventsTable(Context context) {
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
    public void addRow(Events e) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ID, e.getId()); // Events ID
            values.put(KEY_EVENTS_BODY, e.getBody()); // Events body
            values.put(KEY_EVENTS_AUTHOR, e.getAuthor()); // Events author
            values.put(KEY_EVENTS_MODIFIED_ON, e.getDate()); // Events modified date
            // Inserting Row
            db.insert(TABLE_EVENTS, null, values);
        } catch (SQLiteConstraintException ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close(); // Closing database connection
            }
        }
    }

    @Override
    public Events getOneRow(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Events events = null;

        try {
            db = this.getReadableDatabase();

            cursor = db.query
                    (
                            TABLE_EVENTS,
                            new String[]
                                    {
                                            KEY_ID,
                                            KEY_EVENTS_BODY,
                                            KEY_EVENTS_AUTHOR,
                                            KEY_EVENTS_MODIFIED_ON
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
                events = new Events(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            }
        } catch (Exception e) {
            events = null;
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            if (db != null && db.isOpen())
                db.close();
        }
        // return events
        return events;
    }

    @Override
    public List<Events> getAllRows() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Events> eventsList = null;
        try {
            eventsList = new ArrayList<>();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;

            db = this.getWritableDatabase();
            String[] columns = {KEY_ID, KEY_EVENTS_BODY, KEY_EVENTS_AUTHOR, KEY_EVENTS_MODIFIED_ON};
            cursor = db.query(TABLE_EVENTS, columns, null, null, null, null, KEY_ID + " DESC");

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Events events = new Events(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                    // Adding events to list
                    eventsList.add(events);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            if (db != null && db.isOpen())
                db.close();
        }

        // return events list
        return eventsList;
    }

    @Override
    public int updateRow(Events events) {
        SQLiteDatabase db = null;
        int update;
        try {
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_EVENTS_BODY, events.getBody());
            values.put(KEY_EVENTS_AUTHOR, events.getAuthor());
            values.put(KEY_EVENTS_MODIFIED_ON, events.getDate());
            update = db.update(TABLE_EVENTS, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(events.getId())});
        } finally {
            if (db != null && db.isOpen())
                db.close();
        }

        return update;
    }

    @Override
    public void deleteRow(Events events) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.delete(TABLE_EVENTS, KEY_ID + " = ?", new String[]{String.valueOf(events.getId())});
        } finally {
            if (db != null && db.isOpen())
                db.close();
        }
    }

    @Override
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        int count;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(countQuery, null);
            count = cursor.getCount();

        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            if (db != null && db.isOpen())
                db.close();
        }
        // return count
        return count;
    }

    @Override
    public int getLastId() {
        List<Events> eventses = getAllRows();

        int lastEventId = 0;
        for (Events events : eventses) {
            if (events.getId() > 0)
                lastEventId = events.getId();
        }
        return lastEventId;
    }
}
