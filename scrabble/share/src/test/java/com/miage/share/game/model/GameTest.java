package com.miage.share.game.model;

import com.miage.share.game.model.board.Board;
import com.miage.share.player.model.PlayerIdentity;
import com.miage.share.scrabble.LetterScrabble;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game myGame;

    @BeforeEach
    void init(){
        this.myGame = new Game();

    }

    @Test
    void createDefaultInstance() {

        this.myGame = Game.createDefaultInstance();

        assertEquals(myGame.getNbTurn(), Game.maxTurns);
        assertEquals(myGame.getCurrentTurn(), 0);
        assertEquals(myGame.getCurrentPlayer(), 0);

    }

    @Test
    void getBag() {

        assertEquals(myGame.getBag().size(), 100);

    }

    @Test
    void getCurrentPlayer() {

        assertEquals(myGame.getCurrentPlayer(), 0);

    }

    @Test
    void setCurrentPlayer() {

        myGame.setCurrentPlayer(2);
        assertEquals(myGame.getCurrentPlayer(), 2);

    }

    @Test
    void getCurrentTurn() {

        assertEquals(myGame.getCurrentTurn(), 0);

    }

    @Test
    void setCurrentTurn() {

        myGame.setCurrentTurn(2);
        assertEquals(myGame.getCurrentTurn(), 2);

    }

    @Test
    void getNbTurn() {

        assertEquals(myGame.getNbTurn(), 0);

    }

    @Test
    void setNbTurn() {

        myGame.setNbTurn(2);
        assertEquals(myGame.getNbTurn(), 2);
    }

    @Test
    void getWordsPlayed() {

        List<String> res = new ArrayList<>();
        assertEquals(myGame.getWordsPlayed(), res);

    }

    @Test
    void addWordPlayed() {

        myGame.addWordPlayed("Bonjour");
        List<String> res = myGame.getWordsPlayed();
        Boolean isIn = false;

        for(String word : res) {

            if(word == "Bonjour"){
                isIn = true;
            }

        }

        assertEquals(isIn, true);
    }

    @Test
    void removeWordPlayed() {

        myGame.addWordPlayed("Bonjour");
        myGame.removeWordPlayed(0);

        List<String> res = myGame.getWordsPlayed();
        Boolean isIn = false;

        for(String word : res) {

            if(word == "Bonjour"){
                isIn = true;
            }

        }

        assertEquals(isIn, false);

    }

    @Test
    void getPlayers() {

        PlayerIdentity player1 = new PlayerIdentity();
        PlayerIdentity player2 = new PlayerIdentity();
        PlayerIdentity player3 = new PlayerIdentity();

        myGame.addPlayers(player1);
        myGame.addPlayers(player2);
        myGame.addPlayers(player3);

        List<PlayerIdentity> list = myGame.getPlayers();

        assertEquals(list.size(), 3);

    }

    @Test
    void addPlayers() {

        PlayerIdentity player = new PlayerIdentity();
        myGame.addPlayers(player);
        assertEquals(myGame.getPlayers().size(), 1);

    }

    @Test
    void removePlayers() {

        PlayerIdentity player = new PlayerIdentity();
        myGame.addPlayers(player);
        myGame.removePlayers(0);
        assertEquals(myGame.getPlayers().size(), 0);

    }

    @Test
    void getPlayerCharacters() {

        PlayerIdentity identityRequest = new PlayerIdentity();
        identityRequest.setId(UUID.randomUUID().toString());
        identityRequest.setName(identityRequest.getId());
        identityRequest.setURL("http://localhost:4200");

        myGame.addPlayers(identityRequest);

        List<Character> res = myGame.getPlayerCharacters(identityRequest.getId());
        int count = 0;
        assertEquals(res.size(), count);

    }

    @Test
    void getPlayerLetters() {

        PlayerIdentity identityRequest = new PlayerIdentity();
        identityRequest.setId(UUID.randomUUID().toString());
        identityRequest.setName(identityRequest.getId());
        identityRequest.setURL("http://localhost:4200");

        myGame.addPlayers(identityRequest);
        List<LetterScrabble> res = myGame.getPlayerLetters(identityRequest.getId());

        assertEquals(res.size(), 0);

    }

    @Test
    void getId() {

        String id = UUID.randomUUID().toString();
        myGame.setId(id);
        String res = myGame.getId();
        assertEquals(res, id);

    }

    @Test
    void setId() {

        String id = UUID.randomUUID().toString();
        myGame.setId(id);
        String res = myGame.getId();
        assertEquals(res, id);

    }

    @Test
    void getBoard() {

        Board b = new Board();
        myGame.setBoard(b);

        assertEquals(b, myGame.getBoard());

    }

    @Test
    void setBoard() {

        Board b = new Board();
        myGame.setBoard(b);

        assertEquals(b, myGame.getBoard());

    }

    @Test
    void initPlayersLetters() {

        PlayerIdentity player1 = new PlayerIdentity();
        PlayerIdentity player2 = new PlayerIdentity();
        player1.setId(UUID.randomUUID().toString());
        player2.setId(UUID.randomUUID().toString());

        myGame.addPlayers(player1);
        myGame.addPlayers(player2);
        myGame.initPlayersLetters();

        for(PlayerIdentity player : myGame.getPlayers()){

            List<LetterScrabble> list = myGame.getPlayerLetters(player.getId());
            assertEquals(7, list.size());

        }

    }

    @Test
    void addNewLetterToPlayer() {

        PlayerIdentity player2 = new PlayerIdentity();
        player2.setId(UUID.randomUUID().toString());
        myGame.addPlayers(player2);
        myGame.initPlayersLetters();
        myGame.addNewLetterToPlayer(player2);

        for(PlayerIdentity player : myGame.getPlayers()){

            List<LetterScrabble> list = myGame.getPlayerLetters(player.getId());
            assertEquals(8, list.size());

        }

    }

    @Test
    void updateLettersPlayer() {

        PlayerIdentity player2 = new PlayerIdentity();
        player2.setId(UUID.randomUUID().toString());
        myGame.addPlayers(player2);

        List<LetterScrabble> res = myGame.getPlayerLetters(player2.getId());
        res.add(new LetterScrabble(1, 'a'));
        res.add(new LetterScrabble(2, 'a'));

        int[] mappingResult = new int[26];
        mappingResult[0] = 2;


        myGame.updateLettersPlayer(player2.getId(), mappingResult);

        assertEquals(0, res.size());

    }

    @Test
    void addScorePlayer() {

        PlayerIdentity player2 = new PlayerIdentity();
        player2.setId(UUID.randomUUID().toString());
        myGame.addPlayers(player2);

        myGame.addScorePlayer(player2.getId(), 4);
        assertEquals(myGame.getScorePlayer(player2.getId()), 4);

    }

    @Test
    void getScorePlayer() {

        PlayerIdentity player2 = new PlayerIdentity();
        player2.setId(UUID.randomUUID().toString());
        myGame.addPlayers(player2);

        myGame.addScorePlayer(player2.getId(), 4);
        assertEquals(myGame.getScorePlayer(player2.getId()), 4);

    }
}