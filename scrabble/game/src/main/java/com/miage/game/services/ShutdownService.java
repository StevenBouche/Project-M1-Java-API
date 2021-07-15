package com.miage.game.services;

import com.miage.game.GameApplication;
import com.miage.game.GameServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The type Shutdown service.
 */
@Component
public class ShutdownService {

    private final Logger logger = LogManager.getLogger(ShutdownService.class);

    private ApplicationContext ctx;
    private GameServerConfig config;
    private GameClientService client;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    /**
     * Instantiates a new Shutdown service.
     *
     * @param config             the config
     * @param applicationContext the application context
     * @param client             the client
     */
    public ShutdownService(
            @Autowired GameServerConfig config,
            @Autowired ApplicationContext applicationContext,
            @Autowired GameClientService client
    )      {
        this.config = config;
        this.ctx = applicationContext;
        this.client=client;
    }

    /**
     * Shutdown application.
     */
    public void shutdownApplication(){

        logger.info("Schedule a shutdown task in 1 second.");

        Runnable task = new Runnable() {
            @Override
            public void run() {
                //notify word service for shutdown
                client.notifyWordServiceShutdown();

                String str = String.format("Shutdown application : http://%s:%s",config.serverHost, config.serverPort);
                logger.info(str);

                if(config.dockerUse){
                    logger.info("Shutdown with System.exit() code : 0");
                    System.exit(0);
                }
                else{
                    logger.info("Shutdown with Thread.currentThread().interrupt()");
                    SpringApplication.exit(ctx, () -> 0);
                    GameApplication.lock.lock();
                    GameApplication.gameFinish.signal();
                    GameApplication.lock.unlock();
                }
            }
        };

        executor.schedule(task, 1, TimeUnit.SECONDS);

        executor.shutdown();

    }


}


