package com.miage.player.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miage.player.PlayerServerConfig;
import com.miage.player.services.PlayerService;
import com.miage.player.services.ShutdownService;
import com.miage.share.game.model.Game;
import com.miage.share.game.viewmodel.ChoicePlayer;
import com.miage.share.game.viewmodel.DataGame;
import com.miage.share.player.model.PlayerIdentity;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService service;

    @MockBean
    private PlayerServerConfig config;

    @MockBean
    private ShutdownService shutdownService;

    Game myGame;

    @Test
    void createDefaultInstance() {

        this.myGame = Game.createDefaultInstance();

        assertEquals(myGame.getNbTurn(), Game.maxTurns);
        assertEquals(myGame.getCurrentTurn(), 0);
        assertEquals(myGame.getCurrentPlayer(), 0);

    }

    @LocalServerPort
    String port = "9000";

    @Before
    void init(){

        this.myGame = new Game();
        config.serverPort = "9000";
        config.serverHost = "localhost";
        config.dockerUse = false;

        doNothing().when(config).setConfig(Mockito.any());
        doReturn(new ChoicePlayer()).when(service).playTurn(Mockito.any());

    }

    @Test
    void play() throws Exception {

        PlayerIdentity player = new PlayerIdentity();
        player.setId("Jordan");
        Game g = new Game();

        g.addPlayers(player);

        DataGame data = new DataGame(
                g.getId(),
                g.getWordsPlayed(),
                g.getBoard(),
                g.getPlayerCharacters(player.getId())
        );

        this.mockMvc.perform( post("http://localhost:9000/play")
                .content(asJsonString(data))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()
                );

        Mockito.verify(service, Mockito.times(1)).playTurn(Mockito.any());

    }

    @Test
    void exit() {
    }

    void interceptor() {
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}