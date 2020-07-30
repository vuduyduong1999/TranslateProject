package com.example.translateproject.model;

public class WordSynonyms {
    String word;
    String[] synonyms;

    public WordSynonyms() {
    }

    public WordSynonyms(String word, String[] synonyms) {
        this.word = word;
        this.synonyms = synonyms;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String[] getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String[] synonyms) {
        this.synonyms = synonyms;
    }

}
