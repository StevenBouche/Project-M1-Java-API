package com.miage.share.game.model.board;

import com.miage.share.game.model.Game;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FactoryBoardTest {

    Case[][] board = FactoryBoard.createCases();

    @Test
    void ClassFactoryBoardTest(){
        BoardInitTest();
        BoardValueTest();
    }

    // Test of initialization of the empty scrabble map
    void BoardInitTest() {
        printCases(board);

        for(int y = 0; y < board.length; y++){
            for(int x = 0; x < board[y].length; x++) {

                char value = board[y][x].getValue();
                String s=" ";
                char expected=s.charAt(0); //returns h

                assertEquals(expected,value);

            }
        }
    }
    // Test of initialization of the empty virtual scrabble map (value of cases)

    void BoardValueTest() {
        int[][] expectedValues = new int[][]{
                {4, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 4},
                {0, 3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0},
                {0, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 0, 3, 0, 0},
                {1, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1},
                {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0},
                {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
                {4, 0, 0, 1, 0, 0, 0, 5, 0, 0, 0, 1, 0, 0, 4},
                {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
                {0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0},
                {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0},
                {1, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1},
                {0, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, 0, 3, 0, 0},
                {0, 3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0},
                {4, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 4}
        };
        for (int y = 0; y < FactoryBoard.scrabbleMap.length; y++) {
            for (int x = 0; x < FactoryBoard.scrabbleMap[y].length; x++) {

                int value = FactoryBoard.scrabbleMap[y][x];
                int expected= expectedValues[y][x] ;

                assertEquals(expected,value);

            }
        }

    }

    // Function to print Cases of board to String
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