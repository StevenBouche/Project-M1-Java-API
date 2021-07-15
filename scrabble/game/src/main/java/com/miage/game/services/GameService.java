package com.miage.game.services;

import com.miage.game.GameServerConfig;
import com.miage.game.tasks.GameTask;
import com.miage.game.tasks.GameTaskState;
import com.miage.game.tasks.events.OnEventGameTask;
import com.miage.share.game.model.Game;
import com.miage.share.game.viewmodel.ResultAuth;
import com.miage.share.player.model.PlayerIdentity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Component for Games
 * Handles multiples games behavior
 */
class GameExecution{
    /**
     * The Task.
     */
    public GameTask task;
    /**
     * The Future task.
     */
    public Future<StatsGame> futureTask;
}

/**
 * The type Game service.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GameService implements OnEventGameTask {

    private ShutdownService shutdown;
    private GameClientService client;
    private GameServerConfig config;

    //EXECUTE GAME
    private ExecutorService executor = Executors.newFixedThreadPool(10);
    private Map<String, GameExecution> games = new HashMap<>();

    /**
     * The Empty slot.
     */
    public GameTask emptySlot;
    private int maxGame = 1;
    private int currentGame = 0;
    private int gameFinish = 0;

    //OTHER
    private final Logger logger = LogManager.getLogger(GameService.class);
    private final Lock lock = new ReentrantLock();

    /**
     * Instantiates a new Game service.
     *
     * @param shutdown the shutdown
     * @param client   the client
     * @param config   the config
     */
    public GameService(@Autowired ShutdownService shutdown,
                       @Autowired GameClientService client,
                       @Autowired GameServerConfig config){
        this.shutdown = shutdown;
        this.client = client;
        this.config = config;
    }

    /**
     * Player request connection result auth.
     *
     * @param identity the identity
     * @return the result auth
     */
    public ResultAuth playerRequestConnection(PlayerIdentity identity) {

        //lock due to concurrence between most request connection and start game
        this.lock.lock();

        //build a new result auth
        ResultAuth auth = new ResultAuth("", "", "", identity);

        //if current game is null, create new Game to accept player
        if(this.emptySlot == null){
            logger.info("Current game is empty recreate a new game.");
            this.emptySlot = new GameTask(Game.createDefaultInstance(), this.client);
            this.emptySlot.addListenerEventGame(this);
            currentGame++;
        }

        //if current game more than max game not accept request connection
        if(currentGame > maxGame) {
            auth.setMessage("Cannot execute more game.");
            logger.info(auth.getMessage());
            return auth;
        }

        //if game task is in wainting player state add player
        if(this.emptySlot.getState() == GameTaskState.WAITING_PLAYER){

            logger.info("Add player to current game.");

            //add player
            auth.setGameId(this.emptySlot.addPlayer(identity));
            auth.setGameUrl(String.format("http://%s:%s",this.config.serverHost,this.config.serverPort));

            //if success set auth
            if(auth.getGameId()!=null)
                auth.setMessage("Player have join the current game.");
            else
                auth.setMessage("Cannot add player to the current game.");

        } else
            auth.setMessage("Current game have not status : WAITING PLAYER.");


        logger.info(auth.getMessage());

        //delock for other request to be execute
        this.lock.unlock();

        return auth;

    }

    private void executeNewTask(GameTask task){

        logger.info("Schedule task game.");

        GameExecution exec = new GameExecution();
        exec.task = task;
        exec.futureTask = this.executor.submit(new Callable<StatsGame>() {
            @Override
            public StatsGame call() throws Exception {

                Thread.currentThread().setName("Execution thread of game : "+ task.getGame().getId());
                //start game
                Thread t = new Thread(task);
                t.start();

                //waiting execution
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //when game is finish inc concurrent gameFinish
                lock.lock();
                gameFinish++;

                if(currentGame>=maxGame&&gameFinish==maxGame)
                    shutdown.shutdownApplication();

                lock.unlock();

                return null;
            }
        });

        this.games.put(task.getGame().getId(),exec);

    }

    /**
     * After send connection.
     */
    public void afterSendConnection() {
        logger.info("Execute code after response /connection.");
        this.lock.lock();
        if(this.emptySlot!=null && this.emptySlot.getState() == GameTaskState.GAME_READY){
            logger.info("Game is ready, schedule game start task and delink to current game.");
            GameTask current = this.emptySlot;
            this.emptySlot = null;
            this.executeNewTask(current);
            if(currentGame >= maxGame) executor.shutdown();
        }
        this.lock.unlock();
    }

    @Override
    public void playerHaveJoin(PlayerIdentity identity) {
        String str = String.format("Player %s have join",identity.getId());
        logger.info(str);
    }

    @Override
    public void playerHavePlay(PlayerIdentity identity, String word) {
        String str = String.format("%s, le mot joué est : %s",identity.getId(),word);
        logger.info(str);
    }

    @Override
    public void gameIsFinished(Game game) {
        String str = String.format("Game finished : %s",game.getId());
        logger.info(str);
        game.getPlayers().forEach(player -> {
            logger.info("Execute request exit to notify player game has finished.");
            String res = this.client.exit(player);
            String strLambda = player.getId() + " has been disconnected : "+res;
            logger.info(strLambda);
        });
    }

}
