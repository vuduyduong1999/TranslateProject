package com.example.translateproject.model;

public class WordAntonyms {
    String word;
    String[] antonyms;

    public WordAntonyms() {
    }

    public WordAntonyms(String word, String[] antonyms) {
        this.word = word;
        this.antonyms = antonyms;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String[] getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(String[] antonyms) {
        this.antonyms = antonyms;
    }
}
