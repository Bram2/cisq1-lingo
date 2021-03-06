package nl.hu.cisq1.lingo.trainer.domain;

import java.util.List;

public class Progress {

    private int id;
    private int score;
    private List<String> lastHint;
    private List<Mark> feedback;
    private GameState gameState;
    private int roundNumber;

    public Progress(int id, int score, List<String> lastHint, List<Mark> feedback, GameState gameState, int roundNumber) {
        this.id = id;
        this.score = score;
        this.lastHint = lastHint;
        this.feedback = feedback;
        this.gameState = gameState;
        this.roundNumber = roundNumber;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public List<Mark> getFeedback() {
        return feedback;
    }

    public List<String> getLastHint() {
        return lastHint;
    }
}
