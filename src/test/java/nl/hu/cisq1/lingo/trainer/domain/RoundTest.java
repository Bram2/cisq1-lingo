package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    @Test
    @DisplayName("feedback is added to round when an attempt is made")
    void roundAttemptFeedback(){
        Round round = new Round(new Word("WATER"));

        round.guess("WOORD");

        assertEquals(1, round.getFeedback().size());
    }

    @Test
    @DisplayName("create hint after the first attempt has been made")
    void firstHintAfterAttempt(){
        Round round = new Round(new Word("WOORD"));
        round.guess("WOZEN");
        assertEquals(round.giveHint(), List.of("W", "O", ".", ".", "."));
    }

    @Test
    @DisplayName("create first hint before first attempt")
    void firstHint(){
        Round round = new Round(new Word("WOORD"));

        assertEquals(round.giveHint(), List.of("W", ".", ".", ".", "."));
    }

    @Test
    @DisplayName("a hint is created based on the previous hint")
    void hintFromPreviousHint(){
        Round round = new Round(new Word("WOORD"));

        round.guess("WOZEN");
        round.guess("WERED");

        assertEquals(List.of("W", "O", ".", ".", "D"), round.giveHint());
    }

    @Test
    @DisplayName("throw exception if there is an attempt after the word has already been guessed")
    void wordGuessedAttempt(){
        Round round = new Round(new Word("WOORD"));
        round.guess("WOORD");

        assertThrows(RuntimeException.class, () -> round.guess("WOORD"));
    }

    @Test
    @DisplayName("round is done after 5 guesses")
    void tooManyGuesses(){
        Round round = new Round(new Word("WOORD"));

        round.guess("WOZEN");
        round.guess("WERED");
        round.guess("WATER");
        round.guess("WEZEN");
        round.guess("WEVEN");

        assertThrows(RuntimeException.class, ()-> round.guess("WOORD"));
    }


}