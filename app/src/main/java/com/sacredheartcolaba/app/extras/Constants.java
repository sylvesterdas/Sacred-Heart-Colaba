package com.sacredheartcolaba.app.extras;

public interface Constants {
    /*SharedPreference*/
    String SP_SAINT_OF_THE_DAY = "_sp_saint_of_the_day";
    String SP_QUOTE_OF_THE_DAY = "_sp_quote_of_the_day";
    String SP_NEWS = "_sp_news";
    String SP_EVENTS = "_sp_events";

    String STRING_SAINT_LAST_DATE = "saint_last_date";
    String STRING_SAINT_NAME = "saint_name";

    String STRING_QUOTE_LAST_DATE = "quote_last_date";
    String STRING_QUOTE_TEXT = "quote_text";
    String STRING_QUOTE_VERSE = "quote_verse";

    String BOOLEAN_NEW_NEWS = "new_news";
    String BOOLEAN_NEW_EVENT = "new_event";
    /*END*/

    /*Intent Extras*/
    String EXTRA_KEY_NOTIFICATION = "notification";
    String EXTRA_VALUE_NEWS = "news";
    String EXTRA_VALUE_EVENTS = "events";
    /*END*/

    /*Notification*/
    int NOTIFICATION_NEWS_ID = 10;
    int NOTIFICATION_EVENTS_ID = 11;
    /*END*/
}
