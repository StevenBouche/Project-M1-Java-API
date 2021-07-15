package com.miage.game;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GameServerConfig {

    public String serverHost = "";
    public String serverPort = "";
    public String wordHost = "";
    public boolean dockerUse = false;

    /**
     * Set configuration with args of main
     * @param args
     */
    public void setConfig(List<String> args){
        this.serverHost = args.get(0);
        this.serverPort =  args.get(1);
        this.wordHost =  args.get(2);
        this.dockerUse =  args.get(3).equals("true");
    }

    @Override
    public String toString(){
        return String.format(" server host : %s, server port : %s, docker use : %s",
                this.serverHost,
                this.serverPort,
                this.dockerUse
                );
    }

}
