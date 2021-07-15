package com.miage.share.word.viewmodel;

import java.util.ArrayList;
import java.util.List;

public class DataWords {

    private List<Character> characters;
    private List<String> words;

    public DataWords(){
        this.characters = new ArrayList<Character>();
        this.words = new ArrayList<String>();
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void addCharacters(Character character) {
        this.characters.add(character);
    }

    public void addCharacters(List<Character> character) {
        this.characters.addAll(character);
    }

    public Character removeCharacters(int index) {
        return this.characters.remove(index);
    }

    public List<String> getWords() {
        return words;
    }

    public void addWords(String word) {
       this.words.add(word);
    }

    public void addWords(List<String> word) {
        this.words.addAll(word);
    }

    public String removeWords(int index) {
       return this.words.remove(index);
    }

}
