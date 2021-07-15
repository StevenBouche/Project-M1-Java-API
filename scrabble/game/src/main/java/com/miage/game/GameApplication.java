package com.miage.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Main class, boot the web service Game
 */
@SpringBootApplication
public class GameApplication implements ApplicationRunner {

    //use when execution is not docker
    public static final Lock lock = new ReentrantLock();
    public static final Condition gameFinish = lock.newCondition();
    public static final Condition gameReady = lock.newCondition();

    private static final Logger logger = LogManager.getLogger(GameApplication.class);

    /**
     * Start Main program game Application
     * @param args arguments application :
     *       - SERVER_PORT : port of this server
     *       - SERVER_HOST : host or ip of this server
     *       - DOCKER_USE : if docker is use or thread
     */
    public static void main(String[] args) {

        logger.info("Start Game Application.");

        logger.info("Game Application arguments start : ");
        for (String arg: args ) {logger.info(arg);}

        //create spring application of type GameApplication
        SpringApplication app = new SpringApplication(GameApplication.class);

        //define configuration host and port for spring boot application
        Map<String,Object> props = new HashMap<>();
        props.put("server.port", args[1]);
        props.put("server.address", args[0]);
        app.setDefaultProperties(props);

        //start application
        app.run(args);
    }

    private GameServerConfig config;

    public GameApplication( @Autowired GameServerConfig config){
        this.config = config;
    }

    /**
     * To run logic when application is run
     * @param args arguments when run application (Main args)
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

        logger.info("Game Application Runner : set global configuration.");
        this.config.setConfig(Arrays.asList(args.getSourceArgs()));

        //for notify application is ready (only in dockerUser is false)
        if(!this.config.dockerUse){
            GameApplication.lock.lock();
            GameApplication.gameReady.signal();
            GameApplication.lock.unlock();
        }

    }

}
