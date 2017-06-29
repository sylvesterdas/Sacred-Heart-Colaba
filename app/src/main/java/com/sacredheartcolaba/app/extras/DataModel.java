package com.sacredheartcolaba.app.extras;

import java.io.Serializable;

public abstract class DataModel implements Serializable {

    public static final String INTENT_EXTRA_DATA = "data";

    public abstract int getId();

    public abstract String getBody();

    public abstract String getAuthor();

    public abstract String getDate();

    public abstract String toString();
}