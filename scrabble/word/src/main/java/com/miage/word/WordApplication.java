package com.miage.word;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class WordApplication {

    private static final Logger logger = LogManager.getLogger(WordApplication.class);

    public static void main(String[] args) {

        logger.info("Start Word Application.");

        logger.info("Word Application arguments start : ");
        for (String arg: args ) {logger.info(arg);}

        //create spring application of type GameApplication
        SpringApplication app = new SpringApplication( WordApplication.class);

        //define configuration host and port for spring boot application
        Map<String,Object> props = new HashMap<>();
        //props.put("server.port", 8080);
        //props.put("server.address", "localhost");
        props.put("server.port", args[1]);
        props.put("server.address", args[0]);
        app.setDefaultProperties(props);

        //start application
        app.run(args);

    }

}
