package com.miage.player.services;

import com.miage.player.PlayerServerConfig;
import com.miage.player.utils.PlayerHelper;
import com.miage.share.game.model.board.Board;
import com.miage.share.game.viewmodel.ChoicePlayer;
import com.miage.share.game.viewmodel.DataGame;
import com.miage.share.game.viewmodel.ResultAuth;
import com.miage.share.player.model.PlayerIdentity;
import com.miage.share.player.viewmodel.PossiblePlay;
import com.miage.share.word.viewmodel.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * Singleton component for logic player and store data of current player
 * Execute response for game service
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PlayerService {

    /**
     * Gets identity.
     *
     * @return the identity
     */
    public PlayerIdentity getIdentity() {
        return identity;
    }

    private PlayerIdentity identity;
    private int nbTryMax = 3;
    private static Random r = new Random();


    private ShutdownService shutdown;
    private PlayerServerConfig config;
    private PlayerClientService client;
    private PlayerHelper helper;

    private static final Logger logger = LogManager.getLogger(PlayerService.class);

    /**
     * Instantiates a new Player service.
     *
     * @param shutdown the shutdown
     * @param config   the config
     * @param client   the client
     */
    public PlayerService(
            @Autowired ShutdownService shutdown,
            @Autowired PlayerServerConfig config,
            @Autowired PlayerClientService client){
        this.shutdown = shutdown;
        this.config = config;
        this.client = client;
        this.helper = new PlayerHelper();
    }

    /**
     * Execute start request to connect on game server
     *
     * @param nbTry nb try
     */
    public void executeClient(int nbTry) {

        //recursive break and shutdown when failed
        if(nbTry>nbTryMax) {
            this.shutdown.shutdownApplication();
            return;
        }

        logger.info("Start execute action player");

        //Create new identity
        PlayerIdentity identityRequest = new PlayerIdentity();
        identityRequest.setId(UUID.randomUUID().toString());
        identityRequest.setName(identityRequest.getId());
        identityRequest.setURL("http://"+this.config.serverHost+":" + this.config.serverPort);

        //request api game to comfirm player identity and get id of my game
        try{

            //execute post request to get ResultAuth to join one game
            ResultAuth result = this.client.playerConnection(identityRequest);

            String message = String.format("Result request connection : %s",result);
            logger.info(message);

            String id = result.getGameId();
            //if game id is not set
            if(id==null||id.equals(""))
                throw new NullPointerException("Game id cannot be null, no game found.");

            //set my identity to confirm login to game server
            this.identity = result.getIdentity();

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        //retry connect on game server recursive
        nbTry++;

        if(this.identity == null) this.executeClient(nbTry);

    }

    /**
     * Play turn choice player.
     *
     * @param game the game
     * @return the choice player
     */
    public ChoicePlayer playTurn(DataGame game) {

        ChoicePlayer c = new ChoicePlayer();
        Board b = game.getBoard();
        List<Character> charactersPlayer = game.getPlayerLetters();

        //get all lines and cols payable and set letters player to prepare send to word service
        MatchWordElements elements = helper.getMatchWordElements(b);
        elements.setLetters(game.getPlayerLetters());

        boolean firstLine = elements.isFirst();
        elements = this.client.searchMatchWords(elements);

        //get all possible plays with result of word service
        //and filter play where i dont use letters and convert result to a list of possiblePlay
        List<PossiblePlay> plays = helper.getPossiblePlayFilterWithLetter(elements,b,charactersPlayer,firstLine);

        //if is the first play filter possiblePlay with only new word is in middle of board
        if(firstLine){
            plays = helper.filterPlaysWhereWordIsMiddleBoard(plays);
        }

        WordsVerification wordsExist = new WordsVerification();

        //filter on plays where have word to verification
        // and for all plays filter before, add all word to word service verification
        helper.extractVerificationPlays(wordsExist,plays);

        //call on word service for verification of differents words exist
        WordsVerification finalWordsExist = this.client.searchMatchWords(wordsExist);

        //finally we extract word exist and distinct to not have than more same one word
        List<String> verif = helper.extractWordExist(finalWordsExist);

        //filter play have not words or all words exist and use letter player
        plays = helper.filterPlaysHaveNotWordAndIsValid(plays,verif);


        //if player can play determine choice of the player by an decision
        if(!plays.isEmpty()){
            PossiblePlay p = this.randomDecisionPlayer(plays, c);
            System.out.println(p.toString());
        } else {
            System.out.println("Cannot have a possible play with my letters.");
        }

        return c;

    }

    private PossiblePlay randomDecisionPlayer(List<PossiblePlay> plays, ChoicePlayer c) {

        int index = r.nextInt(plays.size());
        PossiblePlay p = plays.get(index);

        c.setDirX(p.getNewWord().getDirX());
        c.setDirY(p.getNewWord().getDirY());
        c.setStartY(p.getNewWord().getStartY());
        c.setStartX(p.getNewWord().getStartX());
        c.setWord(p.getNewWord().getValue());
        c.setUseLetters(p.getLetters());
        c.setHaveChoice(true);
        c.setThinkPlayer(p);

        String message = String.format("My decision : ");
        logger.info(message);
        System.out.print(c.toString());

        return p;
    }

}
