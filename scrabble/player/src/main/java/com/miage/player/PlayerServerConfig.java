package com.miage.player;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PlayerServerConfig  {

    public String serverHost;
    public String serverPort;
    public String gameHostPort;
    public String wordHostPort;
    public boolean dockerUse;

    public void setConfig(String ...args){
        this.serverHost = args[0];
        this.serverPort = args[1];
        this.gameHostPort = args[2];
        this.wordHostPort = args[3];
        this.dockerUse = args[4].equals("true");
    }

    @Override
    public String toString(){
        return String.format("server host : %s, server port : %s, game server : %s, word server : %s, docker use : %s",
                this.serverHost,
                this.serverPort,
                this.gameHostPort,
                this.wordHostPort,
                this.dockerUse);
    }

}
