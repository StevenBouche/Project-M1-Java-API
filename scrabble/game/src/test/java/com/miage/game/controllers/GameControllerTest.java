package com.miage.game.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miage.game.GameServerConfig;
import com.miage.game.services.GameService;
import com.miage.share.game.viewmodel.ResultAuth;
import com.miage.share.player.model.PlayerIdentity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
class GameControllerTest {

    private final Logger logger = LogManager.getLogger(GameControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService service;

    @MockBean
    private GameServerConfig config;

    @LocalServerPort
    String port = "9000";

    @Before
    void init(){

        logger.info("BEFORE INIT TEST");
        config.serverPort = "9000";
        config.serverHost = "localhost";
        config.dockerUse = false;

        doNothing().when(config).setConfig(anyList());
        doNothing().when(service).afterSendConnection();

    }

    @Test
    void playerConnection() throws Exception {

        PlayerIdentity identity = new PlayerIdentity();
        identity.setId(UUID.randomUUID().toString());
        identity.setName("toto");
        identity.setURL("http://localhost:9001");

        ResultAuth result = new ResultAuth(null, null, null, identity);

        when(service.playerRequestConnection(any(PlayerIdentity.class))).thenReturn(result);

        this.mockMvc.perform(
                post("http://localhost:9000/connection")
                        .content(asJsonString(identity))
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk());

        verify(service,times(1)).playerRequestConnection(any(PlayerIdentity.class));
        verify(service,times(1)).afterSendConnection();
    }

    @Test
    void interceptorConnection() {

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