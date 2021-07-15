package com.miage.word.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.miage.share.word.viewmodel.*;
import com.miage.share.word.model.WordCollection;
import com.miage.word.utils.PatternCache;
import com.miage.word.utils.SearchWord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


/**
 * The type Word service.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WordService {

    private ResourceLoader loader;
    private  WordCollection collection;
    private SearchWord search;

    private static final Logger logger = LogManager.getLogger(WordService.class);
    private static final String EMPTY = "";

    private PatternCache cache;

    /**
     * Instantiates a new Word service.
     *
     * @param cache the cache
     * @throws IOException the io exception
     */
    public WordService(@Autowired PatternCache cache) throws IOException {
        this.cache = cache;
        this.search = new SearchWord();
        this.loadCollectionJSON();
    }

    private void loadCollectionJSON() {
        //to load dictionary
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("word_scrabble.json");
        //deserialize
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.collection = objectMapper.readValue(is, WordCollection.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return data that match with input data, set words variable match with list of characters
     * Character list need to be between 2 and 12
     *
     * @param data the data
     * @return words
     */
    public DataWords getWords(DataWords data) {

        List<Character> characters = data.getCharacters();
        int len = characters.size();

        if(len < 2 || len > 12) return data;

        List<String> result = this.search.getWords(this.collection.getWords(),characters);

        data.addWords(result);

        return data;
    }

    /**
     * Get words match pattern match word.
     *
     * @param pattern the pattern
     * @return the match word
     */
    public MatchWord getWordsMatchPattern(MatchWord pattern){
        Map<PatternWord,List<String>> result = this.search.getWordsMatchString(this.collection.getWords(),pattern.getElement());
        pattern.setPatterns(result.keySet().stream().collect(Collectors.toList()));
        pattern.setWordsPatterns(result.values().stream().collect(Collectors.toList()));
        return pattern;
    }

    /**
     * Get words match pattern match word elements.
     *
     * @param pattern the pattern
     * @return the match word elements
     */
    public MatchWordElements getWordsMatchPattern(MatchWordElements pattern){
        long start = System.currentTimeMillis();
        pattern.getMatchElements().parallelStream().forEach(match -> {

            String element = match.getElement();

            if(this.cache.containKey(element)){

                List<PatternWord> patterns = this.cache.getEntryPattern(element);
                List<List<String>> words = this.cache.getEntryWordsPattern(element);

                match.setPatterns(patterns);
                match.setWordsPatterns(words);
            } else {

                Map<PatternWord,List<String>> result = this.search.getWordsMatchString(this.collection.getWords(),match.getElement());
                List<PatternWord> patterns = new ArrayList<>(result.keySet());
                List<List<String>> words = new ArrayList<>(result.values());

                this.cache.addEntry(element, patterns, words);

                match.setPatterns(patterns);
                match.setWordsPatterns(words);
            }

        });
        long diff = System.currentTimeMillis() - start;
        System.out.println("WORD PATTERN TIME ELAPSED : "+diff);
        return pattern;
    }

    /**
     * Word exist word verification.
     *
     * @param words the words
     * @return the word verification
     */
    public WordVerification wordExist(WordVerification words){
        boolean b = this.search.wordExist(this.collection.getWords(), words.getWord());
        words.setResult(b);
        return words;
    }

    /**
     * Generate Map of char in key and Integer of occurence in parameter list
     * @param characters
     * @return
     */
    private Map<Character, Integer> getOccurenceOfCharacter(List<Character> characters) {
        return characters.stream().collect(Collectors.toMap(Character::charValue, l -> 1,Integer::sum));
    }

    /**
     * Words exist words verification.
     *
     * @param data the data
     * @return the words verification
     */
    public WordsVerification wordsExist(WordsVerification data) {

        List<String> words = this.collection.getWords();

        data.getWords().forEach(word -> {
            boolean b = words.contains(word.getWord());
            word.setResult(b);
        });

        return data;
    }
}
