package com.example.translateproject.model;

public class WordHistory {
    private String word;
    private String content;
    private String time;
    private long numberID;
    public WordHistory() {
    }

    public WordHistory(String word, String content) {
        this.word = word;
        this.content = content;
        this.time = "";
    }

    public WordHistory(String word, String content, String time, long index) {
        this.word = word;
        this.content = content;
        this.time = time;
        this.numberID = index;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getNumberID() {
        return numberID;
    }

    public void setNumberID(long numberID) {
        this.numberID = numberID;
    }
}
