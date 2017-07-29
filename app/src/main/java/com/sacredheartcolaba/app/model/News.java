package com.sacredheartcolaba.app.model;

/**
 * @author Sylvester Das
 * @since 7/24/2017.
 */

public class News {
    private int id;
    private String title;
    private String body;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
