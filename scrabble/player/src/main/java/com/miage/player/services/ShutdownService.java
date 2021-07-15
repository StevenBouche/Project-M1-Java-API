package com.miage.player.services;

import com.miage.player.PlayerServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The type Shutdown service.
 */
@Component
public class ShutdownService {

    private ApplicationContext ctx;
    private PlayerServerConfig config;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static final Logger logger = LogManager.getLogger(ShutdownService.class);

    /**
     * Instantiates a new Shutdown service.
     *
     * @param config             the config
     * @param applicationContext the application context
     */
    public ShutdownService(
            @Autowired PlayerServerConfig config,
            @Autowired ApplicationContext applicationContext
    ) {
        this.config = config;
        this.ctx = applicationContext;
    }

    /**
     * Shutdown application.
     */
    public void shutdownApplication(){

        logger.info("Schedule a shutdown task in 1 second.");

        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.info(String.format("Shutdown application : http://%s:%s",config.serverHost, config.serverPort));
                if(config.dockerUse){
                    logger.info("Shutdown with System.exit() code : 0");
                    System.exit(0);
                }
                else{
                    logger.info("Shutdown with Thread.currentThread().interrupt()");
                    SpringApplication.exit(ctx, () -> 0);
                }
            }
        };

        executor.schedule(task, 1, TimeUnit.SECONDS);

        executor.shutdown();

    }

}
