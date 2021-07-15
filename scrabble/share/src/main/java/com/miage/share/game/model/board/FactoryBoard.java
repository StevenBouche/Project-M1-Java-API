package com.miage.share.game.model.board;



public class FactoryBoard {

    public static Case[][] createCases(){
        int size = scrabbleMap.length;
        Case[][] cases = new Case[size][size];
        // Initialisation avec des cases vides
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                cases[y][x] = new Case(CaseType.getCaseType(scrabbleMap[y][x]));
            }
        }
        return cases;
    }

    public static final int[][] scrabbleMap = new int[][]{
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

}
