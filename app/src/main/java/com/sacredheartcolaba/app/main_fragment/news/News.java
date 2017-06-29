package com.sacredheartcolaba.app.main_fragment.news;

import com.sacredheartcolaba.app.extras.DataModel;

public class News extends DataModel {
    public boolean isExpanded = false;
    private int id;
    private String newsBody, newsAuthor, newsDate;

    public News(int id, String newsBody, String newsAuthor, String newsDate) {
        this.id = id;
        this.newsBody = newsBody;
        this.newsAuthor = newsAuthor;
        this.newsDate = newsDate;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getBody() {
        return newsBody;
    }

    @Override
    public String getAuthor() {
        return newsAuthor;
    }

    @Override
    public String getDate() {
        return newsDate;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", newsBody='" + newsBody + '\'' +
                ", newsAuthor='" + newsAuthor + '\'' +
                ", newsDate='" + newsDate + '\'' +
                '}';
    }
}
