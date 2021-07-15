package com.miage.share.word.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WordsVerification {


    private List<WordVerification> words = new ArrayList<>();


    public WordsVerification() {

    }

    public List<WordVerification> getWords() {
        return words;
    }

    public void setWords(List<WordVerification> words) {
        this.words = words;
    }

    public void distinctWord() {
        this.words = this.getWords().stream().distinct().collect(Collectors.toList());
    }

}
