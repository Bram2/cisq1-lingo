package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;
import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    @Test
    @DisplayName("feedback is added to round when an attempt is made")
    void roundAttemptFeedback(){
        Round round = new Round("WATER");

        round.guess("WOORD");

        assertEquals(1, round.getFeedback().size());
    }

    @Test
    @DisplayName("create hint after the first attempt has been made")
    void firstHintAfterAttempt(){
        Round round = new Round("WOORD");
        round.guess("WOZEN");
        assertEquals(round.giveHint(), List.of("W", "O", ".", ".", "."));
    }

    @Test
    @DisplayName("create first hint before first attempt")
    void firstHint(){
        Round round = new Round("WOORD");

        assertEquals(round.giveHint(), List.of("W", ".", ".", ".", "."));
    }

    @Test
    @DisplayName("a hint is created based on the previous hint")
    void hintFromPreviousHint(){
        Round round = new Round("WOORD");

        round.guess("WOZEN");
        round.guess("WERED");

        assertEquals(List.of("W", "O", ".", ".", "D"), round.giveHint());
    }

    @Test
    @DisplayName("throw exception if there is an attempt after the word has already been guessed")
    void wordGuessedAttempt(){
        Round round = new Round("WOORD");
        round.guess("WOORD");

        assertThrows(RuntimeException.class, () -> round.guess("WOORD"));
    }

    @ParameterizedTest
    @MethodSource("guessExamples")
    @DisplayName("A guess returns correct feedback")
    void correctHint(String guess, List<Mark> marks){
        Round round = new Round("WATER");
        round.guess(guess);

        assertEquals(marks, round.getFeedback().get(0).getMarks());
    }

    static Stream<Arguments> guessExamples(){
        return Stream.of(
                Arguments.of("AWTER", List.of(PRESENT, PRESENT, CORRECT, CORRECT, CORRECT)),
                Arguments.of("AAAWW",List.of(ABSENT, CORRECT, ABSENT, PRESENT, ABSENT)),
                Arguments.of("ARRAA",List.of(PRESENT, PRESENT, ABSENT, ABSENT, ABSENT)),
                Arguments.of("HOOFD", List.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT)),
                Arguments.of("WATEW",  List.of(CORRECT, CORRECT, CORRECT, CORRECT, ABSENT)),
                Arguments.of("AAAAE", List.of(ABSENT, CORRECT, ABSENT, ABSENT, PRESENT))
        );
    }

    @Test
    @DisplayName("round is done after 5 guesses")
    void tooManyGuesses(){
        Round round = new Round("WOORD");

        round.guess("WOZEN");
        round.guess("WERED");
        round.guess("WATER");
        round.guess("WEZEN");
        round.guess("WEVEN");

        assertThrows(RuntimeException.class, ()-> round.guess("WOORD"));
    }


}