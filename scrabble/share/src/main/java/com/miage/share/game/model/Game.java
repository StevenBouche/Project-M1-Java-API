package com.miage.share.game.model;

import com.miage.share.game.model.board.Board;
import com.miage.share.player.model.PlayerIdentity;
import com.miage.share.scrabble.FactoryLetterScrabble;
import com.miage.share.scrabble.LetterScrabble;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Game represents the structure of a game
 */
public class Game {

    private static int nbLettersStartGame = 7;
    private static int maxNbLetters = 12;
    /**
     * The constant maxTurns.
     */
    public static int maxTurns = 30;

    /**
     * Create default instance game.
     *
     * @return the game
     */
    public static Game createDefaultInstance(){
        return new Game(maxTurns,0,0);
    }

    private String id;
    private List<PlayerIdentity> players;

    private List<String> wordsPlayed;
    private int nbTurn;
    private int currentTurn;
    private int currentPlayer;
    private Board board;

    private Map<String, List<LetterScrabble>> playerCharacters = new HashMap<>();
    private Map<String, List<LetterScrabble>> lettersPlayed = new HashMap<>();
    private Map<String, Integer> playerScores = new HashMap<>();
    private List<LetterScrabble> bag = FactoryLetterScrabble.createLettersBag();
    private Random random = new Random();

    /**
     * Instantiates a new Game.
     *
     * @param nbTurn        the nb turn
     * @param currentPlayer the current player
     * @param currentTurn   the current turn
     */
    public Game(int nbTurn, int currentPlayer, int currentTurn){
        this.board = new Board();
        this.id = UUID.randomUUID().toString();
        this.players = new ArrayList<>();
        this.wordsPlayed = new ArrayList<>();
        this.nbTurn = nbTurn;
        this.currentPlayer = currentPlayer;
        this.currentTurn = currentTurn;
    }

    /**
     * Instantiates a new Game.
     */
    public Game(){
        this.players = new ArrayList<>();
        this.wordsPlayed = new ArrayList<>();
        this.nbTurn = 0;
        this.currentPlayer = 0;
        this.currentTurn = 0;
    }

    /**
     * Get bag list.
     *
     * @return the list
     */
    public List<LetterScrabble> getBag(){
        return this.bag;
    }

    /**
     * Gets current player.
     *
     * @return the current player
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets current player.
     *
     * @param currentPlayer the current player
     */
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Gets current turn.
     *
     * @return the current turn
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Sets current turn.
     *
     * @param currentTurn the current turn
     */
    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    /**
     * Gets nb turn.
     *
     * @return the nb turn
     */
    public int getNbTurn() {
        return nbTurn;
    }

    /**
     * Sets nb turn.
     *
     * @param nbTurn the nb turn
     */
    public void setNbTurn(int nbTurn) {
        this.nbTurn = nbTurn;
    }

    /**
     * Gets words played.
     *
     * @return the words played
     */
    public List<String> getWordsPlayed() {
        return wordsPlayed;
    }

    /**
     * Add word played.
     *
     * @param word the word
     */
    public void addWordPlayed(String word) {
        this.wordsPlayed.add(word);
    }

    /**
     * Remove word played string.
     *
     * @param index the index
     * @return the string
     */
    public String removeWordPlayed(int index) {
        return this.wordsPlayed.remove(index);
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public List<PlayerIdentity> getPlayers() {
        return players;
    }

    /**
     * Add players.
     *
     * @param player the player
     */
    public void addPlayers(PlayerIdentity player) {
        this.players.add(player);
        this.playerCharacters.put(player.getId(), new ArrayList<>());
        this.playerScores.put(player.getId(), 0);
    }

    /**
     * Remove players player identity.
     *
     * @param index the index
     * @return the player identity
     */
    public PlayerIdentity removePlayers(int index) {
        PlayerIdentity p = this.players.remove(index);
        this.playerCharacters.remove(p.getId());
        return p;
    }

    /**
     * Get player characters list.
     *
     * @param id the id
     * @return the list
     */
    public List<Character> getPlayerCharacters(String id){
        return this.playerCharacters.get(id).stream()
                .map(LetterScrabble::getCharacter)
                .collect(Collectors.toList());
    }

    /**
     * Get player letters list.
     *
     * @param id the id
     * @return the list
     */
    public List<LetterScrabble> getPlayerLetters(String id){
        return this.playerCharacters.get(id);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets board.
     *
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Sets board.
     *
     * @param board the board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Init players letters.
     */
    public void initPlayersLetters() {
        // for all players
        for (PlayerIdentity identity : this.players){
            //for default nb letters start
            for(int i = 0; i < nbLettersStartGame; i++){
                this.addNewLetterToPlayer(identity);
            }
        }
    }

    /**
     * Add new letter to player boolean.
     *
     * @param identity the identity
     * @return the boolean
     */
    public boolean addNewLetterToPlayer(PlayerIdentity identity){

        //if bag is empty
        int size = this.bag.size();
        if(size==0)
            return false;

        //take a random letter in bag
        int randomLetter = this.random.nextInt(size);
        LetterScrabble letter = this.bag.remove(randomLetter);

        //add letter to player
        this.addLetterToPlayer(identity,letter);

        return true;
    }

    private void addLetterToPlayer(PlayerIdentity identity, LetterScrabble letter){
        String id = identity.getId();
        //if players first time have letters
        if(!this.playerCharacters.containsKey(id)){
            this.playerCharacters.put(id, new ArrayList<>());
        }

        int size = this.playerCharacters.get(id).size();
        if(size<maxNbLetters){
            //add letter to player
            this.playerCharacters.get(id).add(letter);
        }

    }

    /**
     * Update letters player.
     *
     * @param id            the id
     * @param mappingResult the mapping result
     */
    public void updateLettersPlayer(String id, int[] mappingResult) {

        List<Character> listCh = new ArrayList<>();
        for (int i = 0; i < mappingResult.length; i++){
            for(int t = 0; t < mappingResult[i]; t++){
                listCh.add((char)('a'+i));
            }
        }

        for(Character c : listCh){
            List<Character> chars = this.getPlayerCharacters(id);
            int index = chars.indexOf(c);
            if(index>=0){
                LetterScrabble letter = this.playerCharacters.get(id).remove(index);
                if(!this.lettersPlayed.containsKey(id)){
                    this.lettersPlayed.put(id, new ArrayList<>());
                }
                this.lettersPlayed.get(id).add(letter);
            }
        }

    }

    /**
     * Add score player.
     *
     * @param idPlayer the id player
     * @param score    the score
     */
    public void addScorePlayer(String idPlayer, int score) {
        int current = this.playerScores.get(idPlayer);
        this.playerScores.replace(idPlayer,current,score+current);
    }

    /**
     * Get score player int.
     *
     * @param idPlayer the id player
     * @return the int
     */
    public int getScorePlayer(String idPlayer){
        return this.playerScores.get(idPlayer);
    }

    /**
     * Get score players map.
     *
     * @return the map
     */
    public Map<String, Integer> getScorePlayers(){
        return this.playerScores;
    }
}
