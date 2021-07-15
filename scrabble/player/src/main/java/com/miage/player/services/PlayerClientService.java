package com.miage.player.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miage.player.PlayerServerConfig;
import com.miage.share.player.model.PlayerIdentity;
import com.miage.share.game.viewmodel.ResultAuth;
import com.miage.share.word.viewmodel.*;
import com.miage.share.word.model.WordCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Component to execute request on game service
 * Execute request for game service
 */
@Component
public class PlayerClientService {

    private RestTemplate restTemplate;
    private PlayerServerConfig config;

    /**
     * The Logger.
     */
    static final Logger logger = LogManager.getLogger(PlayerClientService.class);

    /**
     * Instantiates a new Player client service.
     *
     * @param builder the builder
     * @param config  the config
     */
    public PlayerClientService(
            @Autowired RestTemplateBuilder builder,
            @Autowired PlayerServerConfig config
    ) {
        this.restTemplate = builder.build();
        this.config = config;
    }

    /**
     * Player connection result auth.
     *
     * @param identity the identity
     * @return the result auth
     */
    public ResultAuth playerConnection(PlayerIdentity identity) {
        return restTemplate.postForObject(
                "http://"+this.config.gameHostPort+"/connection",
                identity,
                ResultAuth.class);
    }

    /**
     * Search words data words.
     *
     * @param words the words
     * @return the data words
     */
    public DataWords searchWords(DataWords words){
        return restTemplate.postForObject(
                "http://"+this.config.wordHostPort+"/searchwords",
                words,
                DataWords.class);
    }

    /**
     * Search match words match word.
     *
     * @param word the word
     * @return the match word
     */
    public MatchWord searchMatchWords(MatchWord word){
        return restTemplate.postForObject(
                "http://"+this.config.wordHostPort+"/matchwords",
                word,
                MatchWord.class);
    }

    /**
     * Search match words match word.
     *
     * @param value the value
     * @return the match word
     * @throws JsonProcessingException the json processing exception
     */
    public MatchWord searchMatchWords(String value) throws JsonProcessingException {
        String str = restTemplate.postForObject(
                "http://"+this.config.wordHostPort+"/matchwords",
                value,
                String.class);
        return new ObjectMapper().readValue(str, MatchWord.class);
    }

    /**
     * Search match words match word elements.
     *
     * @param word the word
     * @return the match word elements
     */
    public MatchWordElements searchMatchWords(MatchWordElements word){
        return restTemplate.postForObject(
                "http://"+this.config.wordHostPort+"/matchwordelements",
                word,
                MatchWordElements.class);
    }

    /**
     * Search match words word verification.
     *
     * @param word the word
     * @return the word verification
     */
    public WordVerification searchMatchWords(WordVerification word){
        return restTemplate.postForObject(
                "http://"+this.config.wordHostPort+"/verifwords",
                word,
               WordVerification.class);
    }

    /**
     * Search match words words verification.
     *
     * @param word the word
     * @return the words verification
     */
    public WordsVerification searchMatchWords(WordsVerification word){
        return restTemplate.postForObject(
                "http://"+this.config.wordHostPort+"/verifwords",
                word,
                WordsVerification.class);
    }

}
