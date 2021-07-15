package com.miage.game.tasks;

import com.miage.game.services.GameClientService;
import com.miage.share.game.model.Game;
import com.miage.share.game.model.Word;
import com.miage.share.player.model.PlayerIdentity;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameTaskTest {

    GameClientService client;
    Game game;
    GameTask task;

    @BeforeEach
    void init(){
        client = Mockito.mock(GameClientService.class);
        game = Game.createDefaultInstance();
        game = Mockito.spy(game);
        task = new GameTask(game,client);
        task = Mockito.spy(task);
    }



    @Test
    void run() {

        //addPlayerBeforeStartTest();

        //task.run();

       // Mockito.verify(task, Mockito.times(1)).setState(GameTaskState.IN_PROGRESS);
       // Mockito.verify(game, Mockito.times(1)).initPlayersLetters();




        //this.setState(GameTaskState.IN_PROGRESS);
        //this.beforeStartGame();
        //this.executeGame();
    }

    void addPlayerBeforeStartTest(){

        Assert.assertEquals(GameTaskState.WAITING_PLAYER, task.getState());

        for(int i = 0; i < 4; i++){

            PlayerIdentity identity = new PlayerIdentity();
            identity.setId(UUID.randomUUID().toString());

            task.addPlayer(identity);
            Mockito.verify(game,Mockito.times(1)).addPlayers(identity);

        }

        Assert.assertEquals(4, game.getPlayers().size());
        Assert.assertEquals(GameTaskState.GAME_READY, task.getState());

    }

    @Test
    void  getScorePlayer(){

        Word w = new Word(0,0,5,1,0, "hello");;
        int scoreExpectedHello = (4 + 1 + 1 + (1 * 2) + 1) * 3;
        int score = task.getScorePlayer(w);
        Assert.assertEquals(scoreExpectedHello,score);
    }

}