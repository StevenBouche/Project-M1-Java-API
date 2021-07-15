package com.miage.share.word.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchWord {

    private String element;
    private int index;
    private TypeElementMatch type;
    private List<PatternWord> patterns = new ArrayList<>();
    private List<List<String>> wordsPatterns = new ArrayList<>();

    public MatchWord(){

    }

    public MatchWord(int index, String element, TypeElementMatch type){
        this.setIndex(index);
        this.setElement(element);
        this.setType(type);
    }

    public TypeElementMatch getType() {
        return type;
    }

    public void setType(TypeElementMatch type) {
        this.type = type;
    }

    public int getDirX(){
        return this.getType() == TypeElementMatch.ROW ? 1 : 0;
    }

    public int getDirY(){
        return this.getType() == TypeElementMatch.ROW ? 0 : 1;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public List<PatternWord> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<PatternWord> patterns) {
        this.patterns = patterns;
    }

    public List<List<String>> getWordsPatterns() {
        return wordsPatterns;
    }

    public void setWordsPatterns(List<List<String>> wordsPatterns) {
        this.wordsPatterns = wordsPatterns;
    }

    public Map<PatternWord, List<String>> mapMatched(){
        Map<PatternWord, List<String>> result = new HashMap<>();
        for(int i = 0; i < this.patterns.size(); i++){
            result.put(this.patterns.get(i),this.wordsPatterns.get(i));
        }
        return result;
    }
}
