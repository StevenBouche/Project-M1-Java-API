package com.miage.word.utils;

import com.miage.share.word.viewmodel.PatternWord;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Pattern cache.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PatternCache {

    private Map<String, List<PatternWord>> cacheMatchingPattern = new HashMap<>();
    private Map<String, List<List<String>>> cacheMatchingWord = new HashMap<>();


    /**
     * Contain key boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public synchronized boolean containKey(String key){
        return cacheMatchingPattern.containsKey(key)&&cacheMatchingWord.containsKey(key);
    }

    /**
     * Add entry.
     *
     * @param key          the key
     * @param pattern      the pattern
     * @param wordsPattern the words pattern
     */
    public synchronized void addEntry(String key, List<PatternWord> pattern, List<List<String>> wordsPattern){
        cacheMatchingPattern.put(key,pattern);
        cacheMatchingWord.put(key,wordsPattern);
    }

    /**
     * Get entry pattern list.
     *
     * @param key the key
     * @return the list
     */
    public synchronized List<PatternWord> getEntryPattern(String key){
        return cacheMatchingPattern.get(key);
    }

    /**
     * Get entry words pattern list.
     *
     * @param key the key
     * @return the list
     */
    public synchronized List<List<String>> getEntryWordsPattern(String key){
        return cacheMatchingWord.get(key);
    }

}
