package com.miage.share.game.model.board;

import com.miage.share.game.model.Word;
import com.miage.share.game.viewmodel.ChoicePlayer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    static Board board;

    @BeforeAll
    static void init(){ board = new Board(); }

    @Test
    void TestBoard(){
        setWordOnBoardHorizontal();
        setWordOnBoardVertical();

        getWordPositionVertical();
        getWordPositionVerticalInv();
        getWordPositionHorizontal();
        getWordPositionHorizontalInv();
        getWordPositionNull();
        getWordPositionFalse();

        isOutOfBoundTest();
        getWordWithPositionTest();

        setCasesTest();
    }

    void setWordOnBoardHorizontal() {

        ChoicePlayer choice= new ChoicePlayer();
        choice.setDirX(1);
        choice.setDirY(0);
        choice.setStartX(7);
        choice.setStartY(7);
        choice.setWord("hello");

        board.setWordOnBoard(choice);
        System.out.println(board.toString());

        Case[][] cases = this.board.getCases();
        String word = choice.getWord();

        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            char c2 = cases[choice.getStartY()][choice.getStartX()+i].getValue();
            assertEquals(c,c2);
        }

    }

    void setWordOnBoardVertical() {

        ChoicePlayer choice= new ChoicePlayer();
        choice.setDirX(0);
        choice.setDirY(1);
        choice.setStartX(9);
        choice.setStartY(7);
        choice.setWord("love");

        board.setWordOnBoard(choice);
        System.out.println(board.toString());

        Case[][] cases = this.board.getCases();
        String word = choice.getWord();

        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            char c2 = cases[choice.getStartY()+i][choice.getStartX()].getValue();
            assertEquals(c,c2);
        }

    }
    /**
     * Test Position verticale, lecture de haut en bas
     */

    void getWordPositionVertical(){

        int dirX = 0;
        int dirY = 1;
        int posX = 9;
        int posY = 7;
        int dirInc = 1;

        Word result = new Word();
        Word expected = new Word(9, 8, 3,dirX,dirY,"ove");

        result = board.getWordPosition(dirX, dirY,posX,posY,dirInc);
        System.out.println(result);
        System.out.println(expected);

        assertEquals(expected.toString(),result.toString());
    }

    /**
     * Test Position verticale, lecture de bas en haut
     */
    void getWordPositionVerticalInv(){

        int dirX = 0;
        int dirY = 1;
        int posX = 9;
        int posY = 10;
        int dirInc = -1;

        Word result = new Word();
        Word expected = new Word(9, 7, 3,dirX,dirY,"lov");

        result = board.getWordPosition(dirX, dirY,posX,posY,dirInc);
        System.out.println(result);
        System.out.println(expected);

        assertEquals(expected.toString(),result.toString());
    }

    /**
     * Test Position horizontal, lecture de gauche à droite
     */
    void getWordPositionHorizontal(){

        int dirX = 1;
        int dirY = 0;
        int posX = 7;
        int posY = 7;
        int dirInc = 1;

        Word result = new Word();
        Word expected = new Word(8, 7, 4,dirX,dirY,"ello");

        result = board.getWordPosition(dirX, dirY,posX,posY,dirInc);
        System.out.println(result);
        System.out.println(expected);

        assertEquals(expected.toString(),result.toString());
    }

    /**
     * Test Position horizontal, lecture de droite à gauche
     */
    void getWordPositionHorizontalInv(){

        int dirX = 1;
        int dirY = 0;
        int posX = 11;
        int posY = 7;
        int dirInc = -1;

        Word result = new Word();
        Word expected = new Word(7, 7, 4,dirX,dirY,"hell");

        result = board.getWordPosition(dirX, dirY,posX,posY,dirInc);
        System.out.println(result);
        System.out.println(expected);

        assertEquals(expected.toString(),result.toString());
    }

    /**
     * Test absence de mot à la position choisie
     */
    void getWordPositionNull(){

        int dirX = 0;
        int dirY = 1;
        int posX = 10;
        int posY = 7;
        int dirInc = 1;

        Word result = new Word();
        result = board.getWordPosition(dirX, dirY,posX,posY,dirInc);
        System.out.println(result);
        assertNull(result);
    }

    /**
     * Test mauvais Input
     */
    void getWordPositionFalse(){

        int dirX = 6;
        int dirY = 2;
        int posX = 18;
        int posY = 24;
        int dirInc = 3;

        Word result = new Word();
        result = board.getWordPosition(dirX, dirY,posX,posY,dirInc);
        System.out.println(result);
        assertNull(result);
    }

    /**
     * Test valeur dans la map
     */
    void isOutOfBoundTest(){
        int x= 7;
        int y= 7;
        int w= 16;
        int z= 7;
        int s= 7;
        int a= 18;

        boolean res = board.isOutOfBound(x,y);
        boolean res2= board.isOutOfBound(w,z);
        boolean res3= board.isOutOfBound(s,a);


        System.out.println(res);
        assertEquals(false,res);
        System.out.println(res2);
        assertEquals(true,res2);
        System.out.println(res);
        assertEquals(true,res3);
    }


    void getWordWithPositionTest(){
        int dirX = 1;
        int dirY = 0;
        int posX = 7;
        int posY = 7;

        Word result = new Word();
        Word expected = new Word(posX, posY, 5,dirX,dirY,"hello");

        String charTest="h";
        char valueChar= charTest.charAt(0);

        result = board.getWordWithPosition(dirX, dirY,posX,posY,valueChar);

        assertEquals(expected.toString(),result.toString());
    }


    void setCasesTest(){
        this.board = new Board();
        Case[][] cases = FactoryBoard.createCases();
        Case[][] expected = this.board.getCases();

        board.setCases(cases);
        assertEquals(printCases(expected),printCases(cases));

    }






    public String printCases(Case[][] cases){
        StringBuilder builder = new StringBuilder();
        for(int y = 0; y < cases.length; y++){
            for (int x = 0; x < cases[y].length; x++){
                char cha = cases[y][x].getValue();
                if(cha==' '){
                    cha='_';
                }
                builder.append(cha).append("\t");
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}