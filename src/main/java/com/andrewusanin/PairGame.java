package com.andrewusanin;


import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the rule for pair game.
 */
public class PairGame implements GameRule {

    private static final int NUMBER_OF_PLAYERS = 2;
    private static final int SHOULD_FINISHED = 1;

    private final Map<String, String> gamePairs = new ConcurrentHashMap<>();

    public PairGame(String firstPlayer, String secondPlayer) {
        gamePairs.put(firstPlayer, secondPlayer);
        gamePairs.put(secondPlayer, firstPlayer);
    }

    @Override
    public int numberOfPlayers() {
        return NUMBER_OF_PLAYERS;
    }

    @Override
    public int shouldFinished() {
        return SHOULD_FINISHED;
    }

    @Override
    public void sendMessage(String sender, String message, Map<String, BlockingQueue<String>> players) {
        final BlockingQueue<String> receiverQueue = Optional.ofNullable(gamePairs.get(sender))
                .map(players::get)
                .orElseThrow(() -> new RuntimeException("Player is not registered"));
        receiverQueue.offer(message);
    }
}
