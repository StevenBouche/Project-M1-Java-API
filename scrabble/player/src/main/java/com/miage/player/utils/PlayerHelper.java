package com.miage.player.utils;

import com.miage.share.game.model.Word;
import com.miage.share.game.model.board.Board;
import com.miage.share.game.model.board.Case;
import com.miage.share.player.viewmodel.PossiblePlay;
import com.miage.share.word.viewmodel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Player helper.
 */
public class PlayerHelper {

    private static int INDEX_MIDDLE_BOARD = 7;
    private static String defaultMatchString = "               ";
    private static MatchWord defaultMatchRow = new MatchWord(INDEX_MIDDLE_BOARD, defaultMatchString, TypeElementMatch.ROW);
    private static MatchWord defaultMatchCol = new MatchWord(INDEX_MIDDLE_BOARD, defaultMatchString, TypeElementMatch.COLUMN);

    /**
     * Get and transform all line or column with a letter in a MatchWord Object because is a line/column payable
     *
     * @param b board of game
     * @return All lines or cols in MatchWord list
     */
    public MatchWordElements getMatchWordElements(Board b) {

        List<MatchWord> all = new ArrayList<>();
        List<MatchWord> rows = this.getMatchElementBoardRow(b);
        List<MatchWord> columns = this.getMatchElementBoardColumn(b);

        boolean firstLine = rows.isEmpty()&&columns.isEmpty();

        MatchWordElements element = new MatchWordElements();
        element.setFirst(firstLine);

        if(firstLine){
            all.add(defaultMatchRow);
            all.add(defaultMatchCol);

        } else {
            all.addAll(rows);
            all.addAll(columns);
        }

        element.setMatchElements(all);

        return element;
    }

    private List<MatchWord> getMatchElementBoardRow(Board b){
        Case[][] cases = b.getCases();
        List<MatchWord> result = new ArrayList<>();
        for (int y = 0; y < cases.length; y++){
            StringBuilder builder = new StringBuilder();
            Case[] letters = cases[y];
            boolean payable = false;
            for (int x = 0; x < letters.length; x++){
                builder.append(letters[x].getValue());
                if(!letters[x].isEmpty())
                    payable = true;
            }
            if(payable){
                MatchWord word = new MatchWord(y, builder.toString(), TypeElementMatch.ROW);
                result.add(word);
            }
        }
        return result;
    }

    private List<MatchWord> getMatchElementBoardColumn(Board b){
        List<MatchWord> result = new ArrayList<>();
        Case[][] cases = b.getCases();
        for (int x = 0; x < cases[0].length; x++){
            StringBuilder builder = new StringBuilder();
            boolean payable = false;
            for (int y = 0; y < cases.length; y++){
                builder.append(cases[y][x].getValue());
                if(!cases[y][x].isEmpty())
                    payable = true;
            }
            if(payable){
                MatchWord word = new MatchWord(x, builder.toString(), TypeElementMatch.COLUMN);
                result.add(word);
            }
        }
        return result;
    }


    /**
     * Gets possible play filter with letter.
     *
     * @param elements         the elements
     * @param b                the b
     * @param charactersPlayer the characters player
     * @param firstLine        the first line
     * @return the possible play filter with letter
     */
    public List<PossiblePlay> getPossiblePlayFilterWithLetter(MatchWordElements elements, Board b, List<Character> charactersPlayer, boolean firstLine) {
        //get all possible plays with result of word service
        //and filter play where i dont use letters and convert result to a list of possiblePlay
        return this.getPossiblePlay(elements,b,charactersPlayer,firstLine)
                .stream()
                .filter(play -> !play.getLetters().isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Return all possible play in function of word service results
     *
     * @param element   word service result
     * @param b         board of game
     * @param myLetters letters player
     * @param firstPlay is the first play
     * @return all possible plays
     */
    public List<PossiblePlay> getPossiblePlay(MatchWordElements element, Board b, List<Character> myLetters, boolean firstPlay) {

        List<PossiblePlay> plays = new ArrayList<>();

        //get nb occur letters player
        int[] occurLetters = this.mapOccurrenceChar(myLetters);

        //for all MatchWordElement
        element.getMatchElements().parallelStream().forEach(mw -> {

            List<PatternWord> patterns = mw.getPatterns();
            List<List<String>> words = mw.getWordsPatterns();

            for(int i = 0; i < patterns.size(); i++){
                PatternWord pattern = patterns.get(i);
                List<String> wordsPattern = words.get(i);
                List<PossiblePlay> play = this.searchPlayPattern(b, occurLetters, pattern,wordsPattern, mw, firstPlay);
                plays.addAll(play);
            }

        });

        return plays;
    }

    /**
     * Determine all possible plays for an pattern word
     * @param b bord of the game
     * @param occurLetters mapping nb occur of all letters
     * @param pattern pattern word
     * @param wordsPattern all words of the pattern word
     * @param firstPlay if is the first play
     * @return list of all possible play for the pattern word
     */
    private List<PossiblePlay> searchPlayPattern(Board b, int[] occurLetters, PatternWord pattern, List<String> wordsPattern, MatchWord mw, boolean firstPlay) {

        int dirX = mw.getDirX();
        int dirY = mw.getDirY();
        int index = mw.getIndex();

        Case[] cases = b.getCasesDir(dirX,dirY,index);

        int sizePattern = pattern.getValue().length();
        int startIndex = pattern.getStartIndex();
        int nbLetterUsed = 0;

        //declare new array of 26 int for deep copy
        int[] localMapping = new int[26];
        System.arraycopy(occurLetters, 0, localMapping, 0, 26);

        List<PossiblePlay> plays = new ArrayList<>();

        // for all cases row or column
        for(int nbCase = startIndex, i = 0; nbCase < sizePattern; nbCase++, i++){

            //test all word in this position
            for(String str : wordsPattern){

                int nbLetterCase = 0;
                List<Word> verificationWord = new ArrayList<>();
                //payable only if the word size is less then size pattern
                boolean payable = i+str.length() <= sizePattern;

                //for all letter of word and while is payable test if is a valid letter at this position
                for(int indexWord = 0; indexWord < str.length() && payable; indexWord++){

                    //index of current case, and get this case and get letter of this case
                    int currentCaseLetter = nbCase+indexWord;
                    Case c = cases[currentCaseLetter];
                    char currentCharWord = str.charAt(indexWord);

                    //if this case have not a letter set
                    if(c.isEmpty()){

                        //get current occur letter player for current letter
                        int occur = localMapping[currentCharWord-'a'];

                        //this letter is playable only if occur player - 1 is more than or equals to 0
                        payable = occur - 1 >= 0;

                        if(payable){

                            //is playable we decrement the nb occur of this letter and inc variable nb letters used
                            localMapping[currentCharWord-'a']--;
                            nbLetterUsed++;

                            //determine index in function of direction
                            int startX = dirX == 1 ? currentCaseLetter : index;
                            int startY = dirX == 1 ? index : currentCaseLetter;

                            //inverse dirY and dirX for get word in with inverse direction
                            Word w = b.getWordWithPosition(dirY,dirX,startX,startY,currentCharWord);

                            //if word is not null and in a list for future verification of this word
                            if(w!=null) verificationWord.add(w);

                        }

                    } else {
                        //if this letter case exist test if is equal to current char of word
                        payable = c.getValue()==currentCharWord;
                        nbLetterCase++;

                    }

                }

                //after iterate on all letters, is payable only if payable,
                // have letter case, or this is the first play,
                // and player need use letter for play
                payable = payable && (nbLetterCase > 0 || firstPlay) && nbLetterUsed > 0;

                if(payable){

                    int startX = dirX == 1 ? nbCase : index;
                    int startY = dirX == 1 ? index : nbCase;
                    int sizeWord = str.length()-1;

                    //test line after and before word
                    Word w2 = b.getWordPosition(dirX,dirY,startX,startY,-1);
                    Word w3 = b.getWordPosition(dirX,dirY,startX+(sizeWord*dirX),startY+(sizeWord*dirY),1);

                    //payable only if word have not letters before or before him
                    payable = w2 == null && w3 == null;

                    if(payable){

                        //determine nb occur letters use by this player for this play
                        int[] lettersUse = new int[26];
                        for (int indexLetter = 0; indexLetter < lettersUse.length; indexLetter++){
                            lettersUse[indexLetter] = occurLetters[indexLetter] - localMapping[indexLetter];
                        }

                        //create the payable word
                        Word w = new Word(startX,startY,sizeWord,dirX,dirY, str);

                        //create new play possible and at it in the list of result
                        PossiblePlay p = new PossiblePlay();
                        p.setNewWord(w);
                        p.setLettersUse(lettersUse);
                        p.getWords().addAll(verificationWord);
                        plays.add(p);

                    }

                }

                //reset mapping occur for reuse it in the loop
                System.arraycopy(occurLetters, 0, localMapping, 0, 26);

            }

        }

        return plays;
    }

    private int[] mapOccurrenceChar(List<Character> characters){
        int[] mapping = new int[26];
        for (Character toto : characters) {
            mapping[toto.charValue() - 'a']++;
        }
        return mapping;
    }

    /**
     * Filter plays where word is middle board list.
     *
     * @param plays the plays
     * @return the list
     */
    public List<PossiblePlay> filterPlaysWhereWordIsMiddleBoard(List<PossiblePlay> plays) {
        return plays.stream()
                .filter(play -> play.getNewWord().isMiddle())
                .collect(Collectors.toList());
    }

    /**
     * Extract verification plays.
     *
     * @param wordsExist the words exist
     * @param plays      the plays
     */
    public void extractVerificationPlays(WordsVerification wordsExist, List<PossiblePlay> plays) {
        //filter on plays where have word to verification
        // and for all plays filter before, add all word to word service verification
        plays.stream()
                .filter(play -> play.getWords() != null && !play.getWords().isEmpty())
                .forEach(play -> wordsExist.getWords().addAll(
                        play.getWords().stream()
                                .map(word -> new WordVerification(word.getValue())).collect(Collectors.toList())
                        )
                );
        wordsExist.distinctWord();
    }

    /**
     * Extract word exist list.
     *
     * @param finalWordsExist the final words exist
     * @return the list
     */
    public List<String> extractWordExist(WordsVerification finalWordsExist) {
        //finally we extract word exist and distinct to not have than more same one word
        return finalWordsExist.getWords()
                .stream()
                .filter(WordVerification::isResult)
                .map(WordVerification::getWord)
                .collect(Collectors.toList());
    }

    /**
     * Filter plays have not word and is valid list.
     *
     * @param plays the plays
     * @param verif the verif
     * @return the list
     */
    public List<PossiblePlay> filterPlaysHaveNotWordAndIsValid(List<PossiblePlay> plays, List<String> verif) {
        //filter play have not words or all words exist and use letter player
        return plays.stream()
                .filter(play ->
                        play.getWords().isEmpty() ||
                                verif.containsAll(play.getWords().stream()
                                        .map(Word::getValue)
                                        .collect(Collectors.toList())
                                )
                ).collect(Collectors.toList());
    }
}
