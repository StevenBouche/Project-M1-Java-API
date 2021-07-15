package com.miage.share.word.viewmodel;

public class WordVerification {

    private String word;
    private boolean result = false;

    public WordVerification() {

    }

    public WordVerification(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

}
