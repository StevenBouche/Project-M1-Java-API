package com.miage.share.scrabble;

import java.util.ArrayList;
import java.util.List;

public class FactoryLetterScrabble {


    public static List<LetterScrabble> createLettersBag(){

        List<LetterScrabble> bag = new ArrayList<>();
        int id = 1;
        int indexLetter = 0;

        for(int occur : LetterScrabble.number){
            for(int i =0; i < occur; i++){
                bag.add(new LetterScrabble(id, (char) (indexLetter+'a')));
                id++;
            }
            indexLetter++;
        }

        return bag;

    }

}
