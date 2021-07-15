package com.miage.word.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miage.share.game.viewmodel.ResultAuth;
import com.miage.share.player.model.PlayerIdentity;
import com.miage.share.word.viewmodel.*;
import com.miage.word.services.WordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WordController.class)
class WordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordService service;

    @LocalServerPort
    String port = "9000";

    @Before
    void init(){


    }

    @Test
    void getWordsDataWord() throws Exception {

        DataWords data = new DataWords();

        doReturn(new DataWords()).when(service).getWords(Mockito.any(DataWords.class));

        this.mockMvc.perform(
                post("http://localhost:9000//searchwords")
                        .content(asJsonString(data))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(service,times(1)).getWords(Mockito.any());

    }

    /*@Test
    void getWordsMatchString() throws Exception {

       MatchWord w = new MatchWord();
        w.setElement("aa");

        doReturn(w).when(service).getWordsMatchPattern(Mockito.any(MatchWord.class));

        this.mockMvc.perform(
                post("http://localhost:9000//matchwords")
                        .content(asJsonString(w))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(service,times(1)).getWordsMatchPattern(Mockito.any(MatchWord.class));

    }*/

    @Test
    void getWordsMatchMatchWord() throws Exception {

        MatchWordElements elements = new MatchWordElements();

        doReturn(elements).when(service).getWordsMatchPattern(Mockito.any(MatchWordElements.class));

        this.mockMvc.perform(
                post("http://localhost:9000//matchwordelements")
                        .content(asJsonString(elements))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(service,times(1)).getWordsMatchPattern(Mockito.any(MatchWordElements.class));

    }

    @Test
    void getWordWordVerification() throws Exception {
        //@RequestBody WordVerification data
       // @PostMapping("/verifword")
        //return this.manager.wordExist(data);

        WordVerification elements = new WordVerification();

        doReturn(elements).when(service).wordExist(Mockito.any(WordVerification.class));

        this.mockMvc.perform(
                post("http://localhost:9000//verifword")
                        .content(asJsonString(elements))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(service,times(1)).wordExist(Mockito.any(WordVerification.class));



    }

    @Test
    void getWords() throws Exception {
        //@RequestBody WordsVerification data
        //@PostMapping("/verifwords")1
        //return this.manager.wordsExist(data);
        WordsVerification elements = new WordsVerification();

        doReturn(elements).when(service).wordsExist(Mockito.any(WordsVerification.class));

        this.mockMvc.perform(
                post("http://localhost:9000//verifwords")
                        .content(asJsonString(elements))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(service,times(1)).wordsExist(Mockito.any(WordsVerification.class));
    }

    @Test
    void getWord() throws Exception {
        WordVerification w = new WordVerification();

        doReturn(w).when(service).wordExist(Mockito.any());

        this.mockMvc.perform(
                post("http://localhost:9000//verifword")
                        .content(asJsonString(w))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(service,times(1)).wordExist(Mockito.any());
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