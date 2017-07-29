package com.sacredheartcolaba.app.model;

/**
 * @author Sylvester Das
 * @since 7/24/2017.
 */

public class Token {
    private int id;
    private String token;
    private String created_at, updated_at;

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
