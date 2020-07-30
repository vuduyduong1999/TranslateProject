package com.example.translateproject.model;

public class WordResults {
    String definition;
    String partOfSpeech;
    String[] synonyms, examples;

    public WordResults() {
    }

    public WordResults(String definition, String partOfSpeech, String[] synonyms, String[] examples) {
        this.definition = definition;
        this.partOfSpeech = partOfSpeech;
        this.synonyms = synonyms;
        this.examples = examples;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String[] getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String[] synonyms) {
        this.synonyms = synonyms;
    }

    public String[] getExamples() {
        return examples;
    }

    public void setExamples(String[] examples) {
        this.examples = examples;
    }
}
