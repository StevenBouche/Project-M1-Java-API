package com.miage.share.scrabble;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FactoryLetterScrabbleTest {

    @Test
    void createLettersBag() {

        List<LetterScrabble> list = FactoryLetterScrabble.createLettersBag();
        List<Character> list1 = list.stream().map(LetterScrabble::getCharacter).collect(Collectors.toList());
        int[] mapping = new int[26];

        for (Character toto : list1) {

            mapping[toto.charValue() - 'a']++;

        }

        for(int i = 0 ; i < LetterScrabble.number.length; i++){

            assertEquals(LetterScrabble.number[i], mapping[i]);

        }

    }
}