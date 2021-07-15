package com.miage.player.controllers;

import com.miage.player.PlayerServerConfig;
import com.miage.player.services.PlayerService;
import com.miage.player.services.ShutdownService;
import com.miage.share.game.viewmodel.ChoicePlayer;
import com.miage.share.game.viewmodel.DataGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Import log4j classes.
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * To handle REST player service
 */
@RestController
public class PlayerController  {

    private static final Logger logger = LogManager.getLogger(PlayerController.class);

    private PlayerService manager;
    private PlayerServerConfig config;
    private ShutdownService shutdown;

    public PlayerController(
            @Autowired PlayerService manager,
            @Autowired PlayerServerConfig config,
            @Autowired ShutdownService shutdown){
        this.manager = manager;
        this.config = config;
        this.shutdown = shutdown;
    }

    /**
     * Accept a post request, player execute a choice
     * @param data data of current game
     * @return return word choice
     */
    @PostMapping("/play")
    public ChoicePlayer play(@RequestBody DataGame data) {
        logger.info("Game server ");
        return this.manager.playTurn(data);
    }

    /**
     * Accept a get request, player shutdown after
     * @return word
     */
    @GetMapping("/exit")
    public String exit(){
        logger.info("Game Over");
        logger.info("Shutdown Player");
        return "OK";
    }

    /**
     *To intercept event when response connection is send
     */
    @Bean
    public MappedInterceptor interceptor() {
        return new MappedInterceptor(new String[]{"/exit"}, new HandlerInterceptor() {
            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            System.out.println("PLAYER SHUTDOWN AFTER SEND RESPONSE REQUEST SERVEUR EXIT");
            shutdown.shutdownApplication();
            }
        });
    }

}
