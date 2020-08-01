package com.example.translateproject.model;

public class WordDefinitions {
    String word;
    Definition[] definitions;

    public WordDefinitions() {
    }

    public WordDefinitions(String word, Definition[] definitions) {
        this.word = word;
        this.definitions = definitions;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Definition[] getDefinitions() {
        return definitions;
    }

    public void setDefinitions(Definition[] definitions) {
        this.definitions = definitions;
    }
}
