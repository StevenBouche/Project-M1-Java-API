package com.miage.share.game.viewmodel;

import com.miage.share.player.viewmodel.PossiblePlay;

import java.util.ArrayList;
import java.util.List;

public class ChoicePlayer {
    private List<Character> useLetters = new ArrayList<>();
    private String word;
    private int startX;
    private int startY;
    private int dirX;
    private int dirY;
    private boolean haveChoice = false;
    private PossiblePlay thinkPlayer;

    public ChoicePlayer(){
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getDirX() {
        return dirX;
    }

    public void setDirX(int dirX) {
        this.dirX = dirX;
    }

    public int getDirY() {
        return dirY;
    }

    public void setDirY(int dirY) {
        this.dirY = dirY;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Choice : ").append(this.haveChoice).append("\n");
        builder.append("Word : ").append(this.word).append("\n");
        builder.append("Pos X : ").append(this.startX).append("\n");
        builder.append("Pos Y : ").append(this.startY).append("\n");
        builder.append("Dir X : ").append(this.dirX).append("\n");
        builder.append("Dir Y : ").append(this.dirY).append("\n");
        builder.append("Letters played : ");
        for (Character c : this.useLetters){
            builder.append(" ").append(c).append(" ");
        }
        builder.append("\n");
        return builder.toString();
    }

    public boolean isHaveChoice() {
        return haveChoice;
    }

    public void setHaveChoice(boolean haveChoice) {
        this.haveChoice = haveChoice;
    }

    public List<Character> getUseLetters() {
        return useLetters;
    }

    public void setUseLetters(List<Character> useLetters) {
        this.useLetters = useLetters;
    }

    public PossiblePlay getThinkPlayer() {
        return thinkPlayer;
    }

    public void setThinkPlayer(PossiblePlay thinkPlayer) {
        this.thinkPlayer = thinkPlayer;
    }
}
