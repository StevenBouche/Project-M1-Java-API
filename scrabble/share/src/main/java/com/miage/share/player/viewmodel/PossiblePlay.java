package com.miage.share.player.viewmodel;

import com.miage.share.game.model.Word;

import java.util.ArrayList;
import java.util.List;

public class PossiblePlay{

    private int[] lettersUse;
    private List<Character> letters = new ArrayList<>();
    private Word newWord;
    private List<Word> words = new ArrayList<>();

    public PossiblePlay(){

    }

    public int[] getLettersUse() {
        return lettersUse;
    }

    public void setLettersUse(int[] lettersUse) {
        this.lettersUse = lettersUse;
        for (int z = 0; z < lettersUse.length; z++){
            for(int t = 0; t < lettersUse[z]; t++){
                letters.add((char)('a'+z));
            }
        }
    }

    public List<Character> getLetters() {
        return letters;
    }

    public Word getNewWord() {
        return newWord;
    }

    public void setNewWord(Word newWord) {
        this.newWord = newWord;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }
}
