package com.miage.scrabble;

import com.miage.game.GameApplication;
import com.miage.player.PlayerApplication;
import com.miage.word.WordApplication;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {


    static long startTime;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        //time stats
        startTime = System.currentTimeMillis();
        //thread pool executor
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

        //Process game server
        Runnable game = new Runnable() {
            @Override
            public void run() {

                Thread.currentThread().setName("Game");

                System.out.println("start server");
                GameApplication.main(new String[]{"localhost",String.format("%s", 9000),"localhost:8081","false"});

                GameApplication.lock.lock();
                try {
                    GameApplication.gameFinish.await(1,TimeUnit.HOURS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
                GameApplication.lock.unlock();

            }
        };

        //Process player server
        Runnable word = new Runnable() {
            @Override
            public void run() {

                Thread.currentThread().setName("Word");
                System.out.println("start word");
                WordApplication.main(new String[]{"localhost",String.format("%s", 8081)});
            }
        };

        Runnable r1 = createPlayer("Player1",8082);
        Runnable r2 = createPlayer("Player2",8083);
        Runnable r3 = createPlayer("Player3",8084);
        Runnable r4 = createPlayer("Player4",8085);

        //Execute game
        executor.execute(word);
        executor.execute(game);
        //Waiting game server is ready
        GameApplication.lock.lock();
        try {
            GameApplication.gameReady.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GameApplication.lock.unlock();

        System.out.println("Game Application is ready, start Player Application");

        //execute player server
        executor.execute(r1);
        executor.execute(r2);
        executor.execute(r3);
        executor.execute(r4);

        //stop accept new thread
        executor.shutdown();

        //waiting game server and player server shutdown
        try {
            executor.awaitTermination(1000, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        //exit program
        exitProgram(0);
    }

    private static Runnable createPlayer(String player, int port) {
        return new Runnable() {
            @Override
            public void run() {

                Thread.currentThread().setName(player);

                System.out.println("start "+player);
                PlayerApplication.main(new String[]{"localhost",String.format("%s", port),"localhost:9000","localhost:8081","false"});

            }
        };


    }

    private static void exitProgram(int status){
        // time stats
        long endTime = System.currentTimeMillis();
        long timeExec = (endTime - startTime) / 1000;
        System.out.println("EXIT SCRABBLE PROGRAM, TIME : "+timeExec+" seconds.");
        System.exit(status);
    }

}
