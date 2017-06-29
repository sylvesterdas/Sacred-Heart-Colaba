package com.sacredheartcolaba.app.database;

import com.sacredheartcolaba.app.extras.DataModel;

import java.util.List;

interface DatabaseDefaults<T extends DataModel> {
    int DATABASE_VERSION = 5;
    String DATABASE_NAME = "sacred";
    String KEY_ID = "id";

    String TABLE_NEWS = "news";
    String KEY_NEWS_BODY = "news_body";
    String KEY_NEWS_AUTHOR = "author";
    String KEY_NEWS_MODIFIED_ON = "modified_on";

    String TABLE_EVENTS = "events";
    String KEY_EVENTS_BODY = "events_body";
    String KEY_EVENTS_AUTHOR = "author";
    String KEY_EVENTS_MODIFIED_ON = "modified_on";

    void addRow(T e);

    T getOneRow(int id);

    List<T> getAllRows();

    int updateRow(T e);

    void deleteRow(T e);

    int getRowCount();

    int getLastId();
}
