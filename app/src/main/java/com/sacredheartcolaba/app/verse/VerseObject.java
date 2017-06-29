package com.sacredheartcolaba.app.verse;

public class VerseObject {
    String book_id, book_name, text;
    int chapter, verse;

    public VerseObject(String book_id, String book_name, String text, int chapter, int verse) {
        this.book_id = book_id;
        this.book_name = book_name;
        this.text = text;
        this.chapter = chapter;
        this.verse = verse;
    }

    public String getBook_id() {
        return book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public String getText() {
        return text;
    }

    public int getChapter() {
        return chapter;
    }

    public int getVerse() {
        return verse;
    }

    @Override
    public String toString() {
        return "VerseObject{" +
                "book_id='" + book_id + '\'' +
                ", book_name='" + book_name + '\'' +
                ", text='" + text + '\'' +
                ", chapter=" + chapter +
                ", verse=" + verse +
                '}';
    }
}