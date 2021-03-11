package nl.hu.cisq1.lingo.trainer.domain;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private GameState gameState;
    private double score;

    private final List<Round> rounds = new ArrayList<>();

    public Game(String word){
        this.rounds.add((new Round(word)));
        gameState = GameState.ONGOING;
    }


    //TODO: feedback returnen
    public void guess(String attempt){

        Round round = rounds.get(rounds.size() - 1);

        if(round.roundDone)
           throw new RuntimeException("Invalid guess. Round is done.");

        round.guess(attempt);
    }

    public void startRound(String word){
        Round round = rounds.get(rounds.size() - 1);

        if(!round.roundDone)
            throw new RuntimeException("Previous round is still going!");

        this.rounds.add((new Round(word)));
    }

    public List<Round> getRounds() {
        return rounds;
    }
}
