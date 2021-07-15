package com.miage.share.game.model.board;

public class Case {

    private CaseType type;
    private char value = ' ';

    public Case(){

    }

    public Case(CaseType type){ this.type = type; } // pas fait dans les tests

    public boolean isEmpty() { return this.value == ' '; }

    public char getValue() { return this.value; }

    public void setValue(char letter) {
        this.value = letter;
    }

    public CaseType getType() {
        return type;
    }

    public void setType(CaseType type) {
        this.type = type;
    }

}
