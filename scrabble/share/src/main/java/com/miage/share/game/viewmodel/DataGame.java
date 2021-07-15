package com.miage.share.game.viewmodel;

import com.miage.share.game.model.board.Board;

import java.util.ArrayList;
import java.util.List;

public class DataGame {

    private String id;
    private List<String> wordPlayed = new ArrayList<>();
    private Board board;
    private List<Character> playerLetters = new ArrayList<>();

    public DataGame(){

    }

    public DataGame(String id, List<String> wordsPlayed, Board board, List<Character> playerLetters) {
        this.id = id;
        this.wordPlayed = wordsPlayed;
        this.board = board;
        this.playerLetters = playerLetters;
    }

    public List<String> getWordPlayed() {
        return wordPlayed;
    }

    public void addWordPlayed(String word) {
        this.wordPlayed.add(word);
    }

    public String removeWordPlayed(int index) {
        return this.wordPlayed.remove(index);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Character> getPlayerLetters() {
        return playerLetters;
    }

    public void setPlayerLetters(List<Character> playerLetters) {
        this.playerLetters = playerLetters;
    }
}
