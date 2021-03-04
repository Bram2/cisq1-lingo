package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;
import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {


    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed(){
        Feedback feedback = new Feedback("hoofd", List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT));
        assertTrue(feedback.wordIsGuessed());
    }

    @Test
    @DisplayName("word is not guessed if not all letters are correct")
    void wordIsNotGuessed(){
        Feedback feedback = new Feedback("hoofd", List.of(CORRECT, CORRECT, ABSENT, ABSENT, CORRECT));
        assertFalse(feedback.wordIsGuessed());
    }

    @Test
    @DisplayName("guess is valid if there are no invalid letters")
    void guessIsValid(){
        Feedback feedback = new Feedback("hoofd", List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT));
        assertTrue(feedback.guessIsValid());
    }

    @Test
    @DisplayName("guess is not valid if there are any invalid letters")
    void guessIsNotValid(){
        Feedback feedback = new Feedback("hoofd", List.of(INVALID, INVALID, INVALID, INVALID, INVALID));
        assertFalse(feedback.guessIsValid());
    }

    @Test
    @DisplayName("throw exception if mark size is not the same as attempt length")
    void marksAreNotValid(){
        assertThrows(InvalidFeedbackException.class,
                () -> new Feedback("woord", List.of(CORRECT, CORRECT, CORRECT, CORRECT))
        );
    }

    @ParameterizedTest
    @MethodSource("hintExamples")
    @DisplayName("A guess should get a correct hint")
    void hintIsCorrect(String word, String attempt, List<Mark> marks, List<String> previousHint, List<String> expectedHint){
        Feedback feedback = new Feedback(attempt, marks);

        assertEquals(expectedHint, feedback.giveHint(previousHint, word));
    }

    static Stream<Arguments> hintExamples(){
        return Stream.of(
                Arguments.of("BAARD", "BARST", List.of(CORRECT, CORRECT, PRESENT, ABSENT, ABSENT), List.of("B",".",".",".","."), List.of("B","A",".",".",".")),
                Arguments.of("BAARD", "DRAAD", List.of(ABSENT, PRESENT, CORRECT, PRESENT, CORRECT), List.of("B","A",".",".","."), List.of("B","A","A",".","D")),
                Arguments.of("WATER", "BAARD", List.of(ABSENT, CORRECT, ABSENT, PRESENT, ABSENT), List.of("W",".",".",".","."), List.of("W","A",".",".",".")),
                Arguments.of("WATER", "WAGEN", List.of(CORRECT, CORRECT, ABSENT, CORRECT, ABSENT), List.of("W",".",".",".","."), List.of("W","A",".","E",".")),
                Arguments.of("WATER", "WAGEN", List.of(CORRECT, CORRECT, ABSENT, CORRECT, ABSENT), List.of("W",".","T",".","."), List.of("W","A","T","E","."))
        );
    }

    @Test
    @DisplayName("throw exception if previoushint size is not equal to word length")
    void invalidHintTest(){

        Feedback feedback = new Feedback("WAGEN", List.of(CORRECT, CORRECT, ABSENT, CORRECT, ABSENT));

        assertThrows(InvalidFeedbackException.class,
                () -> feedback.giveHint(List.of("W",".","T","."), "WATER")
        );
    }















}