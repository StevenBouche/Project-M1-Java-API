package com.miage.word.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miage.share.word.model.WordCollection;
import com.miage.share.word.viewmodel.PatternWord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SearchWordTest {

    SearchWord search;
    static WordCollection collection;
    Random r = new Random();

    @BeforeAll
    static void initCollection(){
        //to load dictionary
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("word_scrabble.json");
        //deserialize
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            collection = objectMapper.readValue(is, WordCollection.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void init(){
        search = new SearchWord();
    }

    @Test
    void mapOccurrenceChar(){
        //init attemp result
        int[] mapping = new int[] {0,3,0,0,3,0,0,0,2,0,0,0,1,0,0,0,4,0,0,2,0,0,0,5,0,0};
        //create list de character
        List<Character> c = new ArrayList<>();
        for (int i = 0; i < mapping.length; i++) {
            for(int t = 0; t < mapping[i]; t++)
                c.add((char) ('a' + i));
        }
        //shuffle because for random order input
        Collections.shuffle(c);
        //execute
        int[] mappingResult = search.mapOccurrenceChar(c);
        //assert arrays length
        assertEquals(mapping.length,mappingResult.length);
        //assert all value
        for (int i = 0; i < mapping.length; i++) {
            assertEquals(mapping[i],mappingResult[i]);
        }
    }

    @Test
    public void generateRegex(){

        String[] strs = new String[] {
                "abat           ",
                " abat          ",
                "abat t c       ",
                "abat te c      ",
                "      abat te c",
                "c    te    abat"
        };
        String[] regexs = new String[] {
                "^abat.{0,11}$",
                "^.{0,1}abat.{0,10}$",
                "^abat.{0,1}t.{0,1}c.{0,7}$",
                "^abat.{0,1}te.{0,1}c.{0,6}$",
                "^.{0,6}abat.{0,1}te.{0,1}c$",
                "^c.{0,4}te.{0,4}abat$" };

        for (int i = 0; i < strs.length; i++) {
            String regex = this.search.generateRegex(strs[i].toCharArray());
            System.out.println(regex);
            assertEquals(regex,regexs[i]);
        }

    }

    @Test
    void searchIfWordMatchWithCharacter() {

        int rand = r.nextInt(collection.getWords().size());
        String randomWord = collection.getWords().get(rand);
        char[] chars = randomWord.toCharArray();

        List<Character> characters = this.charArrayToList(chars);
        Collections.shuffle(characters);

        int[] mapping = search.mapOccurrenceChar(characters);
        int size = characters.size();

        for (String str: collection.getWords()) {

            boolean resultMatch = search.searchIfWordMatchWithCharacter(mapping,str,size);
            boolean match = true;
            char[] charCollec = str.toCharArray();
            List<Character> charactersCollec = this.charArrayToList(charCollec);

            if(charactersCollec.size()>characters.size())
                match = false;
            else {
                int[] mappingCollec = search.mapOccurrenceChar(charactersCollec);
                for (int i =0; i < mapping.length; i++) {
                    if (mapping[i] - mappingCollec[i] < 0) {
                        match = false;
                        break;
                    }
                }
            }
            assertEquals(match,resultMatch);
        }
    }

    @Test
    void getWordsMatchString() {
        String row = " bonj  r  t s  ";
        long startTime = System.currentTimeMillis();
        Map<PatternWord,List<String>> map = this.search.getWordsMatchString(collection.getWords(),row);
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }

    @Test
    void getWords() {

    }

    @Test
    void extractPattern() {
        for(int i = 0; i < inputs.length; i++){
            String input = inputs[i];
            String[] result = results[i];
            assertEquals(15,input.length());
            List<PatternWord> res = this.search.extractPattern(input.toCharArray());
            assertEquals(res.size(),result.length);
            List<String> testList = Arrays.asList(result);
            Collections.sort(testList);
            for (int t = 0; t < result.length; t++) {
                assertEquals(testList.get(t),res.get(t).getValue());
            }
        }
    }

    List<Character> charArrayToList(char[] array){
        Stream<Character> charsWord = IntStream
                .range(0, array.length)
                .mapToObj(i -> array[i]);
        return charsWord.collect(Collectors.toList());
    }

    private static final String[] inputs = new String[]{
            "  hello  t te s",
            " t f  g   m  li",
            "mt     f     li",
            "mt f   f   d li"
    };

    private static final String[][] results = new String[][]{
            {
                    "  hello ",
                    "  hello  t",
                    "  hello  t te",
                    "  hello  t te s",
                    "te s",
                    " t te s",
                    " t",
                    " t te"
            },
            {
                    " t",
                    " t f ",
                    " t f  g  ",
                    " t f  g   m ",
                    " t f  g   m  li",
                    " li",
                    "  m  li",
                    " g   m  li",
                    "f  g   m  li",
                    " g  ",
                    "f  g   m ",
                    " g   m "
            },
            {
                    "mt    ",
                    "mt     f    ",
                    "mt     f     li",
                    "    li",
                    "    f     li",
                    "   ",
                    "    f    "
            },
            {
                    "mt f  ",
                    "mt f   f  ",
                    "mt f   f   d",
                    "mt f   f   d li",
                    "  d li",
                    "  f   d li",
                    "f   f   d li",
                    "  f  ",
                    "  f   d",
                    "f   f   d"
            }
    };

}