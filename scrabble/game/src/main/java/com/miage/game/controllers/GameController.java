package com.miage.game.controllers;

import com.miage.game.services.GameService;
import com.miage.share.player.model.PlayerIdentity;
import com.miage.share.game.viewmodel.ResultAuth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * GameController manage HTTP request for a Game
 */
@RestController
public class GameController {

    /**
     * Manager is injected in the constructor
     */
    private GameService manager;

    public GameController(@Autowired GameService manager){
        this.manager = manager;
    }

    /**
     * For displaying properly
     */
    private final Logger logger = LogManager.getLogger(GameController.class);

   /**
   *To intercept event when response connection is send
   */
    @Bean
    public MappedInterceptor interceptor() {
        return new MappedInterceptor(new String[]{"/connection"}, new HandlerInterceptor() {
            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                logger.info("Request connection : Execute logic code after send response to client");
                manager.afterSendConnection();
            }
        });
    }

 /**
     * playerConnection is a POST request for one player connection
     * @param identity
     * @return
     */
    @PostMapping("/connection")
    public ResultAuth playerConnection(@RequestBody PlayerIdentity identity){

        String str = String.format("Request connection for host : %s %s",identity.getId(),identity.getURL());
        logger.info(str);
        return manager.playerRequestConnection(identity);

    }



}
