package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.LingoGameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    @DisplayName("A round is created when a new game is made")
    void firstRoundCreated(){
        Game game = new Game("WOORD");

        assertEquals(1, game.getRounds().size());
    }

    @Test
    @DisplayName("Round is done after the word is guessed")
    void wordGuessed(){
        Game game = new Game("WOORD");

        game.guess("WOORD");

        assertEquals(GameState.WAITING_FOR_ROUND, game.getGameState());
    }

    @Test
    @DisplayName("Player is eliminated after 5 wrong guesses")
    void playerEliminated(){
        Game game = new Game("WOORD");

        game.guess("PAARD");
        game.guess("PAARD");
        game.guess("PAARD");
        game.guess("PAARD");
        game.guess("PAARD");

        assertEquals(GameState.ELIMINATED, game.getGameState());
    }

    @Test
    @DisplayName("Score is added when a word is guessed")
    void scoreAdded(){
        Game game = new Game("WOORD");

        game.guess("WAARD");
        game.guess("WOORD");

        assertEquals(20, game.getScore());
    }

    @Test
    @DisplayName("Throw an exception if a guess is made in an invalid gamestate")
    void notPlayingGuess(){
        Game game = new Game("WOORD");

        game.guess("WOORD");

        assertThrows(LingoGameException.class, () -> game.guess("BAARD"));
    }

    @Test
    @DisplayName("Not allowed to start a new round when still playing")
    void stillPlayingNewRound(){
        Game game = new Game("WOORD");

        assertThrows(LingoGameException.class, () -> game.startRound("BAARD"));
    }

    @Test
    @DisplayName("Create a new round after a word has been guessed")
    void createNewRound(){
        Game game = new Game("WOORD");
        game.guess("WOORD");

        game.startRound("BAARD");

        assertEquals(2, game.getRounds().size());
    }

}