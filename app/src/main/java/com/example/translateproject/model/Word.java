package com.example.translateproject.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Word {
    String wordn, definition;
    List<Definition> definitions;
    List<String> synonyms,antonyms, examples;

    public Word() {
    }

    public Word(String wordn, String definition, List<Definition> definitions, List<String> synonyms, List<String> antonyms, List<String> examples) {
        this.wordn = wordn;
        this.definition = definition;
        this.definitions = definitions;
        this.synonyms = synonyms;
        this.antonyms = antonyms;
        this.examples = examples;
    }

    public Word(String wordn, String definition,List<Definition> definitions, String[] synonyms, String[] antonyms, String[] examples) {
        this.wordn = wordn;
        this.definition = definition;
        this.definitions = definitions;

        this.synonyms = new ArrayList<>(Arrays.asList(synonyms));
        this.antonyms = new ArrayList<>(Arrays.asList(antonyms));
        this.examples = new ArrayList<>(Arrays.asList(examples));
    }
    public Word(String wordn, String definition,Definition[] definitions, String[] synonyms, String[] antonyms, String[] examples) {
        this.wordn = wordn;
        this.definition = definition;
        this.definitions = new ArrayList<>(Arrays.asList(definitions));

        this.synonyms = new ArrayList<>(Arrays.asList(synonyms));
        this.antonyms = new ArrayList<>(Arrays.asList(antonyms));
        this.examples = new ArrayList<>(Arrays.asList(examples));
    }


    public String getWordn() {
        return wordn;
    }

    public void setWordn(String wordn) {
        this.wordn = wordn;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(List<String> antonyms) {
        this.antonyms = antonyms;
    }

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }
}
