package com.andrewusanin;


import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class MessageGame {

    private final Map<String, BlockingQueue<String>> registeredPayers  = new ConcurrentHashMap<>();
    private final CountDownLatch playersReady;
    private final CountDownLatch playersFinished;
    private final GameRule gameRule;

    public MessageGame(GameRule gameRule) {
        this.gameRule = gameRule;
        playersReady = new CountDownLatch(gameRule.numberOfPlayers());
        playersFinished = new CountDownLatch(gameRule.shouldFinished());
    }

    public void register(String name, BlockingQueue<String> queue) {
        registeredPayers.put(name, queue);
    }

    /**
     * Sends message to player. Uses {@param gameRule} for it
     * @param sender, the sender
     * @param message, the message
     */
    public void sendMessage(String sender, String message) {
        gameRule.sendMessage(sender, message, registeredPayers);
    }

    /**
     * Waits until all players are ready.
     * If players are ready will be executed immediately
     * @throws InterruptedException if the current thread is interrupted
     *         while waiting
     */
    public void waitWhenPlayersReady() throws InterruptedException {
        playersReady.await();
    }

    /**
     * Removes player from registered players
     */
    public void playerFinished() {
        playersFinished.countDown();
    }

    /**
     * Removes player from registered players.
     * @param name, the player name
     */
    public void unregisterPlayer(String name) {
        registeredPayers.remove(name);
    }

    /**
     * Decrements the count
     */
    public void playerReady() {
        playersReady.countDown();
    }

    /**
     * Waits until game will be finished.
     * @throws InterruptedException if the current thread is interrupted
     *         while waiting
     */
    public void waitWhenGameFinished() throws InterruptedException {
        playersFinished.await();
    }
}
