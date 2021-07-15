package com.miage.game.services;

import com.miage.game.GameServerConfig;
import com.miage.share.game.viewmodel.ChoicePlayer;
import com.miage.share.game.viewmodel.DataGame;
import com.miage.share.player.model.PlayerIdentity;
import com.miage.share.word.viewmodel.WordVerification;
import com.miage.share.word.viewmodel.WordsVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

/**
 * The type Game client service.
 */
@Component
public class GameClientService {

    private RestTemplate restTemplate;
    private GameServerConfig config;

    /**
     * Instantiates a new Game client service.
     *
     * @param builder the builder
     * @param config  the config
     */
    public GameClientService(
            @Autowired RestTemplateBuilder builder,
            @Autowired GameServerConfig config
            ){
        this.restTemplate = builder.build();
        this.config = config;
    }

    /**
     * Play choice player.
     *
     * @param data the data
     * @param url  the url
     * @return the choice player
     */
    public ChoicePlayer play(DataGame data, String url) {
        return restTemplate.postForObject(url, data, ChoicePlayer.class);
    }

    /**
     * Exit string.
     *
     * @param identity the identity
     * @return the string
     */
    public String exit(PlayerIdentity identity){
        return this.restTemplate.getForObject(identity.getURL() + "/exit", String.class);
    }

    /**
     * Notify word service shutdown string.
     *
     * @return the string
     */
    public String notifyWordServiceShutdown() {
        return this.restTemplate.getForObject("http://"+ this.config.wordHost + "/exit", String.class);
    }

    /**
     * Search match word word verification.
     *
     * @param word the word
     * @return the word verification
     */
    public WordVerification searchMatchWord(WordVerification word){
        return restTemplate.postForObject(
                "http://"+this.config.wordHost+"/verifword",
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
                "http://"+this.config.wordHost+"/verifwords",
                word,
                WordsVerification.class);
    }

}
