package com.miage.word.utils;

import com.miage.share.word.viewmodel.PatternWord;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Search word.
 */
public class SearchWord {

    /**
     * Map occurrence char int [ ].
     *
     * @param characters the characters
     * @return the int [ ]
     */
    public int[] mapOccurrenceChar(List<Character> characters){
        int[] mapping = new int[26];
        for (Character toto : characters) {
            mapping[toto.charValue() - 'a']++;
        }
        return mapping;
    }

    /**
     * Gets words.
     *
     * @param collection the collection
     * @param characters the characters
     * @return the words
     */
    public List<String> getWords(List<String> collection, List<Character> characters) {

        int[] mapping = this.mapOccurrenceChar(characters);
        int size = characters.size();

        //filter all word that match with search and generate new list
        return collection.stream()
                .filter(word -> this.searchIfWordMatchWithCharacter(mapping, word, size))
                .collect(Collectors.toList());

    }

    /**
     * Word exist boolean.
     *
     * @param collection the collection
     * @param word       the word
     * @return the boolean
     */
    public boolean wordExist(List<String> collection, String word){
        return collection.stream()
                .filter(w -> w.equals(word))
                .findFirst()
                .orElse(null) != null;
    }

    /**
     * Gets words match string.
     *
     * @param collection the collection
     * @param str        the str
     * @return the words match string
     */
    public Map<PatternWord,List<String>> getWordsMatchString(List<String> collection, String str) {

        int size = str.length();
        Map<PatternWord,List<String>> result = new HashMap<>();

        if(size!=15) return result;

        long start = System.currentTimeMillis();
        char[] characters = str.toCharArray();
        List<PatternWord> patterns = this.extractPattern(characters);
        long diff = System.currentTimeMillis() - start;
        //System.out.println("EXTRACT PATTERN TIME ELAPSED : "+diff);

        start = System.currentTimeMillis();
        patterns.forEach(pattern -> {
            String regex = this.generateRegex(pattern.getValue().toCharArray());
            List<String> match = collection.stream()
                    .filter(word -> word.length() <= pattern.getValue().length())
                    .filter(word -> word.matches(regex))
                    .collect(Collectors.toList());
            result.put(pattern,match);
        });
        diff = System.currentTimeMillis() - start;
        //System.out.println("GENERATE REGEX AND GET WORD MATCH TIME ELAPSED : "+diff);

        result.values().removeIf(List::isEmpty);
        result.keySet().removeIf(PatternWord::haveNotEmpty);

        return result;
    }

    /**
     * Extract pattern list.
     *
     * @param row the row
     * @return the list
     */
    public List<PatternWord> extractPattern(char[] row){

        List<PatternWord> resultPattern = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        StringBuilder builderInv = new StringBuilder();

        int sizeRow = row.length;

        if(sizeRow != 15 ) return new ArrayList<>();

        int current = 0;
        int currentInv = sizeRow-1;
        int lastHit = current;
        int lastHitInv = currentInv;
        String builderResult;

        for(current = 0, currentInv = sizeRow-1; current<sizeRow; current++, currentInv--){

            builder.append(row[current]);

            int n1 = current+1;
            int n2 = current+2;
            int p1 = current-1;
            boolean n1IsEmpty = n1<sizeRow && row[n1] == ' ';
            boolean n2IsLetter = n2<sizeRow && row[n2] != ' ';
            boolean isInBound = p1 >= 1;
            boolean lastCharIsFirstAndEmpty = p1 == 0 && row[p1] == ' ';

            if(n1IsEmpty&&n2IsLetter&&(isInBound||lastCharIsFirstAndEmpty)){
                builderResult = builder.toString();
                PatternWord p = new PatternWord(builderResult,current-(builderResult.length()-1));
                resultPattern.add(p);
                lastHit=current;
                this.onHitSearchChange(lastHit, lastHitInv, resultPattern, row);
            }

            builderInv.append(row[currentInv]);

            n1 = currentInv-1;
            n2 = currentInv-2;
            p1 = currentInv+1;
            n1IsEmpty = n1>=0 && row[n1] == ' ';
            n2IsLetter = n2>=0 && row[n2] != ' ';
            isInBound = p1 <= sizeRow - 2;
            lastCharIsFirstAndEmpty = p1 == sizeRow - 1 && row[p1] == ' ';

            if(n1IsEmpty&&n2IsLetter&&(isInBound||lastCharIsFirstAndEmpty)){
                builderResult = builderInv.toString();
                char[] elem = new char[builderResult.length()];
                for (int i = 0; i < elem.length; i++) elem[i] = builderResult.charAt((builderResult.length()-1)-i);
                PatternWord p = new PatternWord(new String(elem),currentInv);
                resultPattern.add(p);
                lastHitInv=currentInv;
                this.onHitSearchChange(lastHit, lastHitInv, resultPattern, row);
            }
         }

        String last = builder.toString();
        PatternWord p = new PatternWord(last,0);
        resultPattern.add(p);

        return resultPattern.stream()
                .sorted(Comparator.comparing(PatternWord::getValue))
                .collect(Collectors.toList());

    }

    private void onHitSearchChange(int lastHit, int lastHitInv, List<PatternWord> result, char[] row){
        if(lastHit>lastHitInv){
            StringBuilder builder = new StringBuilder();
            for(int i = lastHitInv; i <= lastHit; i++) builder.append(row[i]);
            String str = builder.toString();
            PatternWord p = new PatternWord(str,lastHitInv);
            result.add(p);
        }
    }

    /**
     * Generate regex string.
     *
     * @param characters the characters
     * @return the string
     */
    public String generateRegex(char[] characters){

        StringBuilder builder = new StringBuilder();

        builder.append('^');
        int nbEmpty = 0;
        for (int i = 0; i < characters.length; i++) {

            if(characters[i]==' ')
                nbEmpty++;
            else{
                if(nbEmpty!=0){
                    builder.append(".{0,").append(nbEmpty).append("}");
                    nbEmpty = 0;
                }
                builder.append(characters[i]);
            }

            if(i==characters.length-1&&nbEmpty>0)
                builder.append(".{0,").append(nbEmpty).append("}");

        }

        builder.append("$");
        return builder.toString();
    }


    /**
     * Search if word match with character boolean.
     *
     * @param mapping the mapping
     * @param word    the word
     * @param size    the size
     * @return the boolean
     */
    public boolean searchIfWordMatchWithCharacter(int[] mapping, String word, int size) {

        if (word.length() > size) {
            return false;
        }

        //declare new array of 26 int for deep copy
        int[] localMapping = new int[26];
        System.arraycopy(mapping, 0, localMapping, 0, 26);

        boolean matched = true;
        //while currentLetter is less than word size and matched => currentLetter++
        for (int currentLetter = 0; currentLetter < word.length() && matched; currentLetter++) {

            //difference integer between 2 char
            int indexCurrentLetter = word.charAt(currentLetter) - 'a';

            //decrement value of char
            localMapping[indexCurrentLetter]--;

            //if nb at index char is less than 0
            if (localMapping[indexCurrentLetter] < 0)  matched = false;

        }

        return matched;
    }
}
