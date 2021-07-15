package com.miage.share.word.viewmodel;

import java.util.ArrayList;
import java.util.List;

public class PatternWord  {

    private String value;
    private int startIndex;

    public PatternWord(){

    }

    public PatternWord(String pattern, int index){
        this.setValue(pattern);
        this.setStartIndex(index);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public List<Character> getCharacters(){
        List<Character> chars = new ArrayList<>();
        for (char ch : this.getValue().toCharArray()){
            if(ch!=' ') chars.add(ch);
        }
        return chars;
    }

    public boolean haveLetter(){
        for (char ch : this.getValue().toCharArray()){
            if(ch!=' ')
                return true;
        }
        return false;
    }

    public boolean haveNotEmpty() {
        for (char ch : this.getValue().toCharArray()){
            if(ch==' ')
                return false;
        }
        return true;
    }

}
