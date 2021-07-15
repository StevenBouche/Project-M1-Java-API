package com.miage.word.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miage.share.word.viewmodel.*;
import com.miage.word.services.WordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
public class WordController {

    private static final Logger logger = LogManager.getLogger(WordController.class);

    private WordService manager;

    public WordController(@Autowired WordService manager){
        this.manager = manager;
    }

    /**
     * Accept a post request, player execute a choice
     * @param data data of current game
     * @return return word choice
     */
    @PostMapping("/searchwords")
    public DataWords getWords(@RequestBody DataWords data){
        logger.info("Game server ");
        return this.manager.getWords(data);
    }

    @PostMapping("/matchwords")
    public String getWordsMatch(@RequestBody String match) throws JsonProcessingException {
        MatchWord word = new ObjectMapper().readValue(match, MatchWord.class);
        word = this.manager.getWordsMatchPattern(word);
        return new ObjectMapper().writeValueAsString(word);
    }

    @PostMapping("/matchwordelements")
    public MatchWordElements getWordsMatch(@RequestBody MatchWordElements match){
        return this.manager.getWordsMatchPattern(match);
    }

    /**
     * Accept a post request, player execute a choice
     * @param data data of current game
     * @return return word choice
     */
    @PostMapping("/verifword")
    public WordVerification getWord(@RequestBody WordVerification data){
        return this.manager.wordExist(data);
    }


    @PostMapping("/verifwords")
    public WordsVerification getWords(@RequestBody WordsVerification data){
        return this.manager.wordsExist(data);
    }

    @GetMapping("/exit")
    public String exit(){
        return "ok";
    }

    /**
     *To intercept event when response connection is send
     */
    @Bean
    public MappedInterceptor interceptor() {
        return new MappedInterceptor(new String[]{"/exit"}, new HandlerInterceptor() {
            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                logger.info("Exit Shutdown word service");
               System.exit(0);
            }
        });
    }

}
