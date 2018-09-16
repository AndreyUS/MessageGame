package com.andrewusanin;


import java.util.concurrent.BlockingQueue;

/**
 * Represents participant of the game.
 */
public class Player extends Thread {

    private PlayerState state = PlayerState.NOT_READY;
    private final MessageGame messageGame;
    private final String name;
    private final BlockingQueue<String> messagesQueue;
    private final int messagesLimit;
    private final boolean startFirst;
    private int messageCount;

    public Player(MessageGame messageGame, String name, int messagesLimit, boolean startFirst, BlockingQueue<String> messagesQueue) {
        setDaemon(true);
        setName(name);
        this.messageGame = messageGame;
        this.name = name;
        this.messagesLimit = messagesLimit;
        this.startFirst = startFirst;
        this.messagesQueue = messagesQueue;
    }

    /**
     * The method makes registration player in the game and notify when it's ready.
     * If {@param startFist} true the player sends message first. Otherwise, listens about incoming message.
     * When {@param messageLimit} will be achieved it stops to send and listens messages.
     * During exit from the game it notifies about it and unregister player from game players.
     */
    @Override
    public void run() {
        messageGame.register(name, messagesQueue);
        state = PlayerState.READY;
        messageGame.playerReady();
        try {
            if (startFirst) {
                messageGame.sendMessage(name, "Hello from: " + name);
            }
            while (true) {
                final String message = messagesQueue.take();
                messageCount++;
                System.out.println(String.format("Player: %s received #%s message with text: %s", name, messageCount, message));
                if (messageCount == messagesLimit) {
                    break;
                }
                messageGame.sendMessage(name, String.format("%s #%s", message, messageCount));
            }
            messageGame.playerFinished();
            messageGame.unregisterPlayer(name);
            state = PlayerState.NOT_READY;
        } catch (InterruptedException e) {
            messageGame.unregisterPlayer(name);
        }
        System.out.println(String.format("Player: %s finished", name));
    }
}
