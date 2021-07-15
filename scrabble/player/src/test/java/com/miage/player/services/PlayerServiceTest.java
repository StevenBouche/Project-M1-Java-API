package com.miage.player.services;

import com.miage.player.PlayerServerConfig;
import com.miage.share.game.viewmodel.ResultAuth;
import com.miage.share.player.model.PlayerIdentity;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = PlayerService.class)
class PlayerServiceTest {

    @MockBean
    ShutdownService shutdown;

    @MockBean
    PlayerClientService client;

    @MockBean
    PlayerServerConfig config;

    @Autowired
    PlayerService service;

    @BeforeEach
    void init(){
        Mockito.reset(shutdown, client);
    }

    @Test
    void executeClientShutdown() {

        //setup return
        ResultAuth auth = new ResultAuth();

        //setup return mock
        Mockito.doReturn(auth).when(client).playerConnection(Mockito.any());
        Mockito.doNothing().when(shutdown).shutdownApplication();

        service.executeClient(1);

        Mockito.verify(client,  times(3)).playerConnection(Mockito.any(PlayerIdentity.class));
        Mockito.verify(shutdown,  times(1)).shutdownApplication();

    }

    @Test
    void executeClient() {

        //setup return
        ResultAuth auth = new ResultAuth();
        PlayerIdentity identity = new PlayerIdentity();

        auth.setIdentity(identity);
        auth.setGameId("totoID");
        auth.setGameUrl("http://game");
        auth.setMessage("success");

        //setup return mock
        Mockito.doReturn(auth).when(client).playerConnection(Mockito.any());

        service.executeClient(1);

        Mockito.verify(client,  times(1)).playerConnection(Mockito.any(PlayerIdentity.class));
        Assert.assertEquals(service.getIdentity(),identity);

    }

    @Test
    void playTurn() {




    }



}
