package com.miage.game.services;

import com.miage.game.GameServerConfig;
import com.miage.share.player.model.PlayerIdentity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = GameService.class)
class GameServiceTest {

    @MockBean
    ShutdownService shutdown;

    @MockBean
    GameClientService client;

    @MockBean
    GameServerConfig config;

    @Autowired
    GameService service;

    @Test
    void test(){
        System.out.print("toto" );
    }


    @Test
    void playerRequestConnection() {
    }

    @Test
    void afterSendConnection() {
    }

    @Test
    void playerHaveJoin() {
    }

    @Test
    void playerHavePlay() {
    }

    @Test
    void gameIsFinished() {
    }

    List<PlayerIdentity> getIdentity(){
        List<PlayerIdentity> identities = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            int portStart = 9000;
            //Create new identity
            PlayerIdentity identityRequest = new PlayerIdentity();
            identityRequest.setId(UUID.randomUUID().toString());
            identityRequest.setName(identityRequest.getId());
            identityRequest.setURL("http://localhost:"+(9000+i));
            identities.add(identityRequest);
        }
        return identities;
    }

}