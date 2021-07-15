package com.miage.share.word.model;

import java.util.ArrayList;
import java.util.List;

public class WordCollection {

    private List<String> words = new ArrayList<>();

    public WordCollection(){

    }

    public List<String> getWords() {
        return words;
    }

    public void addWords(String word) {
        this.words.add(word);
    }

    public String removeWords(int index) {
        return this.words.remove(index);
    }

}
