package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.LingoGameException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Game {


    @Id
    @GeneratedValue
    private int id;

    private GameState gameState;
    private int score;

    @OneToMany(cascade= CascadeType.ALL)
    private final List<Round> rounds = new ArrayList<>();


    public Game(){}

    public Game(String word){
        this.rounds.add((new Round(word)));
        gameState = GameState.PLAYING;
    }

    public void guess(String attempt){

        if(gameState != GameState.PLAYING)
            throw new LingoGameException("Currently not playing a round");

        Round round = rounds.get(rounds.size() - 1);

        round.guess(attempt);

        if(round.wordGuessed) {
            gameState = GameState.WAITING_FOR_ROUND;
            score = score + (5 * (5 - round.getFeedback().size()) + 5);
        }
        else if(round.getFeedback().size() >= 5)
            gameState = GameState.ELIMINATED;

    }

    public void startRound(String word){
        if(gameState != GameState.WAITING_FOR_ROUND)
            throw new LingoGameException("Not allowed to start a new round");

        this.rounds.add((new Round(word)));
        gameState = GameState.PLAYING;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public int getWordLength(){
        return rounds.get(rounds.size() - 1).getWord().length();
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }

    public Round getLastRound(){
        return rounds.get(rounds.size() - 1);
    }
}
