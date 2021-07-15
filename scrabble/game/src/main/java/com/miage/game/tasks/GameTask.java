package com.miage.game.tasks;

import com.miage.game.services.GameClientService;
import com.miage.share.game.model.Game;
import com.miage.game.tasks.events.OnEventGameTask;
import com.miage.share.game.model.Word;
import com.miage.share.game.model.board.Board;
import com.miage.share.game.model.board.Case;
import com.miage.share.share.Direction;
import com.miage.share.game.viewmodel.ChoicePlayer;
import com.miage.share.game.viewmodel.DataGame;
import com.miage.share.player.model.PlayerIdentity;
import com.miage.share.scrabble.LetterScrabble;
import com.miage.share.word.viewmodel.WordVerification;
import com.miage.share.word.viewmodel.WordsVerification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GameTask correspond to one game model. It defines how a Game work.
 */
public class GameTask implements Runnable {

    private final Logger logger = LogManager.getLogger(GameTask.class);

    private GameClientService client;

    private Game game;
    private GameTaskState state;
    private int nbMaxPlayer = 4;
    private boolean gameIsFinish = false;
    private List<OnEventGameTask> eventCallback = new ArrayList<>();

    public GameTask(Game game, GameClientService rest){
        this.setGame(game);
        this.client = rest;
        this.eventCallback = new ArrayList<>();
        this.setState(GameTaskState.WAITING_PLAYER);
    }

    @Override
    public void run() {
       this.setState(GameTaskState.IN_PROGRESS);
       this.beforeStartGame();
       this.executeGame();
    }

    private void beforeStartGame() {
        logger.info("Init letters for all players");
        this.getGame().initPlayersLetters();
    }

    /**
     * Add a player to current game
     * @param identity identity send by player
     * @return game ID or null if add player fail
     */
    public String addPlayer(PlayerIdentity identity) {

        //get current player in game
        List<PlayerIdentity> identities = this.getGame().getPlayers();

        //if size current player is less than max player authorize in this game
        if(identities.size() < this.nbMaxPlayer) {

            //push identity of new player
            this.getGame().addPlayers(identity);
            this.notifyPlayerHaveJoin(identity);

            //if now, nb player is the max player authorize change state of game to ready to start
            if(identities.size() == this.nbMaxPlayer) this.setState(GameTaskState.GAME_READY);

            //and return current id of this game
            return this.getGame().getId();
        }

        return null;
    }


    private void notifyPlayerHaveJoin(PlayerIdentity identity) {
        this.eventCallback.forEach((event) -> { event.playerHaveJoin(identity);});
    }

    /**
     * Execute logic of game
     */
    public void executeGame(){

        String id = getGame().getId();
        String str = String.format("Start game %s.",id);
        logger.info(str);

        this.executeTurns();

        str = String.format("Finish game %s.",id);
        logger.info(str);

        this.endGame();

    }

    /**
     * Execute a play for all players in game
     */
    public void executeTurns() {

        //extract current and max turn limit
        int currentTurn = this.getGame().getCurrentTurn();
        int nbTurn = this.getGame().getNbTurn();

        //print state turn
        String str = String.format("Execute turn %s/%s.",currentTurn,nbTurn);
        logger.info(str);

        //if nb current turn is more than nb turn max, stop recurse
        if(this.gameIsFinish||currentTurn>nbTurn)
            return;

        //reset player first position
        this.getGame().setCurrentPlayer(0);

        //execute turn for all players
        this.executeTurnPlayer();

        this.getGame().setCurrentTurn(currentTurn+1);

        //next turn
        this.executeTurns();
    }

    /**
     * Execute a play for the current player in this game
     */
    public void executeTurnPlayer() {

        //get the current player in game and list of all player identity
        int currentPlayer = this.getGame().getCurrentPlayer();
        List<PlayerIdentity> players = this.getGame().getPlayers();

        //stop recurse when current postion player is mote than size - 1
        if(currentPlayer>players.size()-1||this.gameIsFinish)
            return;

        //get current player
        PlayerIdentity result = players.get(currentPlayer);

        //execute a play for this player
        this.handleTurnPlayer(result);

        //set next player
        this.getGame().setCurrentPlayer(currentPlayer+1);

        //next player
        this.executeTurnPlayer();
    }

    /**
     * Handle a turn for a player
     * @param result player identity
     */
    public void handleTurnPlayer(PlayerIdentity result) {

        System.out.println("Handle Turn of player :" + result.getId() + " (" +result.getURL()+")");

        String idPlayer = result.getId();

        //if no bag and player current have no letter stop game
        if(this.getGame().getBag().isEmpty()&& this.getGame().getPlayerCharacters(idPlayer).isEmpty()){
            this.gameIsFinish = true;
            return;
        }

        //add new letter to current player
        this.getGame().addNewLetterToPlayer(result);

        //get character player after update add new letter and get board
        List<Character> chars = this.getGame().getPlayerCharacters(idPlayer);
        Board board = this.getGame().getBoard();

        this.printLetterInBag();
        this.printLetters("Current letters player : ",chars);

        //prepare data for choice player
        DataGame data = new DataGame(
                this.getGame().getId(),
                this.getGame().getWordsPlayed(),
                board,
                chars
        );

        String str = "Execute request /play to execute turn of current player.";
        logger.info(str);

        //execute post request to get play of current player
        String url = String.format("%s%s",result.getURL(),"/play");
        ChoicePlayer c = this.client.play(data,url);

        //print choice player
        System.out.println(c.toString());

        //if have take a choice
        if(c.isHaveChoice()){

            //verify have letter and set mapping result is letters used
            int[] mappingResult = new int[26];
            boolean haveLetter = this.haveLetterPlayer(result,c,mappingResult);

            if(!haveLetter) { System.out.println("Player have no use letter or bad letter."); return; }

            //boolean wordCanBePlay = this.validateWordPlayer(this.game.getBoard(),c);   validateOnlyHaveNotNewWord
            List<Word> resultWord = new ArrayList<>();
            boolean wordCanBePlay = this.validateOnlyHaveNotNewWord(this.getGame().getBoard(),c,resultWord);

            if(!wordCanBePlay) { System.out.println("Word cannot be play."); return; }

            //if word can be play and player have all letter to make work
            //Update letters
            this.getGame().updateLettersPlayer(result.getId(),mappingResult);
            chars = this.getGame().getPlayerCharacters(idPlayer);

            this.printLetters("Update letters player : ",chars);

            //Set word on board
            this.getGame().getBoard().setWordOnBoard(c);
            this.getGame().addWordPlayed(c.getWord());

            //add score word player
            this.addScorePlayer(idPlayer, resultWord);

            //notify subcribe
            //this.notifyPlayerHavePlay(result,c.getWord());
        }

        //print all word played
        this.printWordPlayedInGame();

        System.out.println("Print game board : ");
        System.out.println(this.getGame().getBoard().toString());

    }

    /**
     * for all word result, add a calculate score to player identify by her id
     * @param idPlayer id player
     * @param resultWord list of result word
     */
    private void addScorePlayer(String idPlayer, List<Word> resultWord) {
        //Calculate score of choice
        System.out.println("New words detected.");
        for(Word w : resultWord){
            System.out.println(w.toString());
            int score = this.getScorePlayer(w);
            this.getGame().addScorePlayer(idPlayer,score);
            int scorePlayer = this.getGame().getScorePlayer(idPlayer);
            System.out.println("Score word : "+score);
            System.out.println("Score player : "+scorePlayer);
        }
    }

    /**
     * Verify if player have take a valid choice letters
     * @param result player identity
     * @param c choice of player
     * @param mappingResult increment this array with result of letters used
     * @return if is a valid choice letters
     */
    private boolean haveLetterPlayer(PlayerIdentity result, ChoicePlayer c, int[] mappingResult) {

        //get letters player and letter used in this choice
        List<Character> lettersPlayer = this.getGame().getPlayerCharacters(result.getId());
        List<Character> lettersUsed = c.getUseLetters();

        //if player have not use letter is not a valid choice
        if(lettersUsed.isEmpty())
            return false;

        //prepare mapping of character
        int[] mappingPlayer = new int[26];
        int[] mappingUse = new int[26];

        for (Character toto : lettersPlayer) mappingPlayer[toto-'a']++;
        for (Character toto : lettersUsed)  mappingUse[toto-'a']++;

        //for all letter in alphabet compare current nb letters and nb letters used
        //if is less than 0 player have use a bad letter
        //else store the difference in mapping result
        boolean valid = true;
        for(int i = 0; i < 26 && valid; i++){
            int res = mappingPlayer[i] - mappingUse[i];
            if(res<0) valid = false;
            else {
                mappingResult[i] = mappingResult[i] + mappingUse[i];
            }
        }

        return valid;
    }

    /**
     * Verify possibility of a choice player, verify word played and all new word detected by this move
     * @param board board of game
     * @param choice choice of player
     * @param result list of all valid word result
     * @return if is a valid play
     */
    private boolean validateOnlyHaveNotNewWord(Board board, ChoicePlayer choice, List<Word> result) {

        Case[][] cases = board.getCases();
        int startX = choice.getStartX();
        int startY = choice.getStartY();
        int dirX = choice.getDirX();
        int dirY = choice.getDirY();
        String word = choice.getWord();

        Word current = new Word(startX,startY,word.length(),dirX,dirY,word);

        List<Word> wordsOnInvDir = new ArrayList<>();
        boolean valid = true;
        for(int i = 0; i < word.length() && valid; i++){
            Case c = cases[startY][startX];
            if(c.isEmpty()){
                Word w = board.getWordWithPosition(dirY,dirX,startX,startY,word.charAt(i));
                if(w!=null)
                    wordsOnInvDir.add(w);
            }
            else {
                valid = c.getValue() == word.charAt(i);
            }
            startY = startY + dirY;
            startX = startX + dirX;
        }

        //verify new words on inverse direction
        if(valid&&!wordsOnInvDir.isEmpty()){

            WordsVerification verif = new WordsVerification();
            wordsOnInvDir.forEach(value -> {
                verif.getWords().add(new WordVerification(value.getValue()));
            });
            WordsVerification finalVerif = this.client.searchMatchWords(verif);
            List<WordVerification> wordlist = finalVerif.getWords();
            for( int i = 0; i < wordlist.size() && valid; i++){
                valid = wordlist.get(i).isResult();
            }

        }

        if(valid){

            int x = choice.getStartX();
            int y = choice.getStartY();
            int sizeWord = word.length()-1;
            Word w = board.getWordPosition(dirX,dirY,x,y,-1);
            Word w2 = board.getWordPosition(dirX,dirY,x+(sizeWord*dirX),y+(sizeWord*dirY),1);

            valid = w==null&&w2==null;

            if(!valid) {
                System.out.println("HAVE LETTER BEFORE OR AFTER WORD");

                StringBuilder builder = new StringBuilder();
                if(w!=null) builder.append(w.getValue());
                builder.append(word);
                if(w2!=null) builder.append(w2.getValue());

                String verif = builder.toString();
                WordVerification verifWord = new WordVerification();
                verifWord.setWord(verif);

                verifWord = this.client.searchMatchWord(verifWord);

                valid = verifWord.isResult();

                if(!valid){
                    System.out.print("NOT VALID BUG");
                }

            }

            if(valid){
                result.add(current);
                result.addAll(wordsOnInvDir);
            }

        }

        return valid;
    }

    public int getScorePlayer(Word choice){

        Case[][] cases = this.getGame().getBoard().getCases();
        String word = choice.getValue(); //récupérer le mot placé

        int size = word.length(); //calculer sa longueur
        int scoreWord = 0; //score du mot initié à 0
        int multiplicatorWord = 1; //multiplcateur à initié à 0
        int scorePlayer = 0; //score du joueur initié à 0

        int dirX = choice.getDirX();
        int dirY = choice.getDirY();
        int posX = choice.getStartX();
        int posY = choice.getStartY();

        if(size == 0) return scorePlayer;

        for (int i = 0; i < size; i++) {
            //charAt trouve la position et conversion en char
            char currentLetter = word.charAt(i);
            int indexCurrentLetter = currentLetter - 'a';
            int scoreLetter = LetterScrabble.points[indexCurrentLetter];

            String type = cases[posY][posX].getType().getType();

            if (type.equals("LD"))  scoreLetter = scoreLetter * 2; //si la currentLetter tombe sur une case bonus LD on double le score de la lettre
            else if (type.equals("LT")) scoreLetter = scoreLetter * 3;  //si la currentLetter tombe sur une case bonus LT on triple le score de la lettre
            else if (type.equals("MD")) multiplicatorWord = multiplicatorWord * 2;  //si la currentLetter tombe sur une case bonus MD on multiplie la valeur du multiplicateur par 2
            else if (type.equals("MT")) multiplicatorWord = multiplicatorWord * 3; //si la currentLetter tombe sur une case bonus MT on multiplie la valeur du multiplicateur par 3

            scoreWord += scoreLetter;

            posX = posX + dirX;
            posY = posY + dirY;

        }

        scorePlayer = scoreWord * multiplicatorWord;

        return scorePlayer;
    }



    public boolean validateWordPlayer(Board board, ChoicePlayer choice) {
        Case[][] boardCases = board.getCases();

        int size= choice.getWord().length();
        int dirX = choice.getDirX();
        int dirY = choice.getDirY();

        int startX = choice.getStartX();
        int startY = startX + size;
        for(int i=0; i < size; i++) {

            //Start case
            if(i == 0) {
                if(!board.checkEmpty(startX, startY, Direction.NORTH, Direction.SOUTH,
                        Direction.EAST, Direction.WEST) && !boardCases[startX][startY].isEmpty()) {
                    return false;
                }
                else {
                    startX = startX + dirX;
                    startY = startY + dirY;
                }
            }

            else {
                if(dirX == 1) {
                    if(!board.checkEmpty(startX, startY, Direction.NORTH, Direction.EAST, Direction.WEST)) {
                        return false;
                    }
                    else {
                        startX = startX + dirX;
                        startY = startY + dirY;
                    }
                }
                if(dirY == 1) {
                    if(!board.checkEmpty(startX, startY, Direction.NORTH, Direction.EAST, Direction.SOUTH)) {
                        return false;
                    }
                    else {
                        startX = startX + dirX;
                        startY = startY + dirY;
                    }
                }
            }
        }
        return true;
    }

    private void notifyPlayerHavePlay(PlayerIdentity identity, String word){
        this.eventCallback.forEach((event) -> {
            event.playerHavePlay(identity,word);
        });
    }

    private void endGame() {
        this.setState(GameTaskState.FINISHED);
        Map<String,Integer> maps = this.game.getScorePlayers();
        for(String str : maps.keySet()) System.out.println("PLAYER "+str+" SCORE : "+ maps.get(str));
        this.eventCallback.forEach((event) -> {
            event.gameIsFinished(this.getGame());
        });
    }

    /**
     * print list of characters with an header message
     * @param message header message
     * @param chars list of char
     */
    private void printLetters(String message, List<Character> chars) {
        StringBuilder builder = new StringBuilder();
        builder.append(message);
        for(Character c : chars){
            builder.append(" ").append(c).append(" ");
        }
        builder.append("\n");
        System.out.println(builder.toString());
    }

    /**
     * Print letters in bag of game
     */
    private void printLetterInBag() {
        List<Character> chars = this.getGame().getBag().stream()
                .map(LetterScrabble::getCharacter)
                .collect(Collectors.toList());
        this.printLetters("Current letters in bag : ", chars);
    }


    private void printWordPlayedInGame() {
        logger.info(String.format("WORD PLAYED."));
        StringBuilder strbul = new StringBuilder();
        for(String str : this.getGame().getWordsPlayed())
        {
            strbul.append(str+" ");
        }
        System.out.println(strbul.toString());
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setState(GameTaskState state){
        String str = String.format("Change state of game : %s",state.toString());
        logger.info(str);
        this.state = state;
    }

    public void addListenerEventGame(OnEventGameTask event){
        this.eventCallback.add(event);
    }

    public GameTaskState getState() {
        return state;
    }
}
