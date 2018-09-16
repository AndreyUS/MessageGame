package com.andrewusanin;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class MessageGameApp {

    private static final int MESSAGES_LIMIT = 10;

    public static void main(String[] args) throws InterruptedException {

        //create name for players
        final String firstPlayerName = "Player1";
        final String secondPlayerName = "Player2";

        //create game rule
        final GameRule gameRule = new PairGame(firstPlayerName, secondPlayerName);

        //create game
        final MessageGame messageGame = new MessageGame(gameRule);

        //decide who will start first
        final int i = new Random().nextInt();
        final boolean fistStarted = i % 2 == 0;

        //Create and run players
        final Player first = new Player(messageGame, firstPlayerName, MESSAGES_LIMIT, fistStarted, new ArrayBlockingQueue<>(MESSAGES_LIMIT));
        final Player second = new Player(messageGame, secondPlayerName, MESSAGES_LIMIT, !fistStarted, new ArrayBlockingQueue<>(MESSAGES_LIMIT));
        first.start();
        second.start();

        messageGame.waitWhenPlayersReady();

        messageGame.waitWhenGameFinished();

        System.out.println("Game was finished");
    }
}
