package com.miage.share.game;

import com.miage.share.scrabble.LetterScrabble;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LetterScrabbleTest {

    @BeforeAll
    static void init(){

    }

    @Test
    void testValueLetter(){

        //index = nombre ascii de la lettre en question - nombre ascii de a
        //donc par exemple si on veut l'index de c : on fait c - a donc 99 - 97 = 2 !

        char a = 'a';
        int scoreWord = 0;

        String word = "hello"; //le mot en question
        int lenght = word.length(); //calculer sa longueur

        for(int i=0; i < lenght ; i++)
        {
            //charAt trouve la position et conversion en char
            char currentLetter = word.charAt(i);
            int indexCurrentLetter = currentLetter - a;
            int scoreLetter = LetterScrabble.points[indexCurrentLetter];
            System.out.println("Letter : "+currentLetter+", position : "+indexCurrentLetter+", value : "+scoreLetter);
            scoreWord += scoreLetter;
        }

        //test unitaires

        int score1 = LetterScrabble.points['h'-'a'];
        int score2 = LetterScrabble.points['e'-'a'];
        int score3 = LetterScrabble.points['l'-'a'];
        int score4 = LetterScrabble.points['o'-'a'];

        assertEquals(4,score1);
        assertEquals(1,score2);
        assertEquals(1,score3);
        assertEquals(1,score4);

        System.out.println("Value of the word :"+word+" is : "+scoreWord); //afficher le score du mot d'exemple

        //int dirX = 1;
        //int dirY = 0;

        //int x = 0;
        //int y = 0;

        //x = x + ( 1 * dirX);
        //y = y + ( 1 * dirY);

    }

}