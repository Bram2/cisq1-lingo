package nl.hu.cisq1.lingo.trainer.domain;

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

        assertEquals(game.getRounds().size(), 1);
    }

    @Test
    @DisplayName("Guess is invalid if the round is done")
    void roundIsDoneGuess(){
        Game game = new Game("WOORD");

        game.guess("PAARD");
        game.guess("PAARD");
        game.guess("PAARD");
        game.guess("PAARD");
        game.guess("PAARD");

        assertThrows(RuntimeException.class, () -> game.guess("PAARD"));
    }



}