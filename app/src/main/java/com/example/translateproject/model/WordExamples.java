package com.example.translateproject.model;

public class WordExamples {
    String word;
    String[] examples;
    public WordExamples() {

    }

    public WordExamples(String word, String[] examples) {
        this.word = word;
        this.examples = examples;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String[] getExamples() {
        return examples;
    }

    public void setExamples(String[] examples) {
        this.examples = examples;
    }
}
