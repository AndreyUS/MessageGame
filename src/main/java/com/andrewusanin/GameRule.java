package com.andrewusanin;


import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Describes rule of game
 */
public interface GameRule {

    /**
     * @return number of participants in the game
     */
    int numberOfPlayers();

    /**
     * @return how many players should finish
     */
    int shouldFinished();

    /**
     * Sends message from one user to another
     * @param sender, the sender
     * @param message, the message
     * @param players, current registered players in the game
     */
    void sendMessage(String sender, String message, Map<String, BlockingQueue<String>> players);
}
