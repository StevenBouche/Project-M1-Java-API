package com.miage.player;

import com.miage.player.services.PlayerClientService;
import com.miage.player.services.PlayerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class PlayerApplication implements ApplicationRunner {

    private static final Logger logger = LogManager.getLogger(PlayerApplication.class);

    public static void main(String[] args) {

        logger.info("Start Player Application.");

        logger.info("Player Application arguments start : ");
        for (String arg: args ) {logger.info(arg);}

        //create spring application of type GameApplication
        SpringApplication app = new SpringApplication(PlayerApplication.class);

        //define configuration host and port for spring boot application
        Map<String,Object> props = new HashMap<>();
        props.put("server.port", args[1]);
        props.put("server.address", args[0]);
        app.setDefaultProperties(props);

        //start application
        app.run(args);

    }

    private PlayerService manager;
    private ApplicationContext applicationContext;
    private PlayerServerConfig config;

    public PlayerApplication(
            @Autowired PlayerService manager,
            @Autowired ApplicationContext applicationContext,
            @Autowired PlayerServerConfig config
            ){
        this.manager = manager;
        this.applicationContext =applicationContext;
        this.config = config;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Player Application Runner : set global configuration and start request for connection game.");
        this.config.setConfig(args.getSourceArgs());
        manager.executeClient(0);
    }

}
