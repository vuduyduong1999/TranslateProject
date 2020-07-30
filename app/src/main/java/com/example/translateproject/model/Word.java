package com.example.translateproject.model;

public class Word {
    String word;
    WordResults[] results;
    WordPronunciation pronunciation;

    public Word() {
    }

    public Word(String word, WordResults[] results, WordPronunciation pronunciation) {
        this.word = word;
        this.results = results;
        this.pronunciation = pronunciation;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public WordResults[] getResults() {
        return results;
    }

    public void setResults(WordResults[] results) {
        this.results = results;
    }

    public WordPronunciation getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(WordPronunciation pronunciation) {
        this.pronunciation = pronunciation;
    }
}
