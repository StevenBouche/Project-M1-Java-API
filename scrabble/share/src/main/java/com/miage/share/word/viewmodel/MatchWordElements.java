package com.miage.share.word.viewmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchWordElements {

    private List<Character> letters = new ArrayList<>();
    private List<MatchWord> matchElements =  new ArrayList<>();
    private boolean isFirst = false;

    public MatchWordElements(){

    }

    public MatchWordElements(List<MatchWord>... lists){
        this.matchElements =  new ArrayList<>();
        for (List<MatchWord> elements: lists) {
            this.matchElements.addAll(elements);
        }
    }

    public MatchWordElements(MatchWord... list){
        this.matchElements =  new ArrayList<>();
        this.matchElements.addAll(Arrays.asList(list));
    }

    public MatchWordElements(List<MatchWord> lists){
        this.matchElements = lists;
    }

    public List<MatchWord> getMatchElements() {
        return matchElements;
    }

    public void setMatchElements(List<MatchWord> matchElements) {
        this.matchElements = matchElements;
    }

    public List<Character> getLetters() {
        return letters;
    }

    public void setLetters(List<Character> letters) {
        this.letters = letters;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }
}
