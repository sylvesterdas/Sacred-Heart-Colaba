package com.sacredheartcolaba.app.main_fragment.events;


import com.sacredheartcolaba.app.extras.DataModel;

public class Events extends DataModel {
    public boolean isExpanded = false;
    private int id;
    private String eventsBody, eventsAuthor, eventsDate;

    public Events(int id, String eventsBody, String eventsAuthor, String eventsDate) {
        this.id = id;
        this.eventsBody = eventsBody;
        this.eventsAuthor = eventsAuthor;
        this.eventsDate = eventsDate;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getBody() {
        return eventsBody;
    }

    @Override
    public String getAuthor() {
        return eventsAuthor;
    }

    @Override
    public String getDate() {
        return eventsDate;
    }

    @Override
    public String toString() {
        return "Events{" +
                "id=" + id +
                ", eventsBody='" + eventsBody + '\'' +
                ", eventsAuthor='" + eventsAuthor + '\'' +
                ", eventsDate='" + eventsDate + '\'' +
                '}';
    }
}
