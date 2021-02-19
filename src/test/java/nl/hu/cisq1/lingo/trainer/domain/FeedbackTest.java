package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {


    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed(){
        Feedback feedback = new Feedback("hoofd", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertTrue(feedback.wordIsGuessed());
    }

    @Test
    @DisplayName("word is not guessed if not all letters are correct")
    void wordIsNotGuessed(){
        Feedback feedback = new Feedback("hoofd", List.of(Mark.CORRECT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT));
        assertFalse(feedback.wordIsGuessed());
    }

    @Test
    @DisplayName("guess is valid if there are no invalid letters")
    void guessIsValid(){
        Feedback feedback = new Feedback("hoofd", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertTrue(feedback.guessIsValid());
    }

    @Test
    @DisplayName("guess is not valid if there are any invalid letters")
    void guessIsNotValid(){
        Feedback feedback = new Feedback("hoofd", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));
        assertFalse(feedback.guessIsValid());
    }



}