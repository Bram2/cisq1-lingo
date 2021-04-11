package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.GameState;
import nl.hu.cisq1.lingo.trainer.domain.Progress;
import nl.hu.cisq1.lingo.trainer.domain.exception.LingoGameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(CiTestConfiguration.class)
class GameServiceIntegrationTest {

    @Autowired
    private GameService gameService;

    @MockBean
    private GameRepository gameRepository;


    @ParameterizedTest
    @MethodSource("guessExamples")
    @DisplayName("Making a guess gives the correct hint")
    void makeGuess(String word, String attempt, List<String> hint){
        when(gameRepository.getGameById(anyInt())).thenReturn(Optional.of(new Game(word)));

        Progress progress = gameService.guess(1, attempt);

        assertEquals(hint, progress.getLastHint());
    }

    static Stream<Arguments> guessExamples(){
        return Stream.of(
                Arguments.of("BAARD", "BARST", List.of("B","A",".",".",".")),
                Arguments.of("BAARD", "DRAAD", List.of("B",".","A",".","D")),
                Arguments.of("WATER", "WATER", List.of("W","A","T","E","R")),
                Arguments.of("WATER", "WAGEN", List.of("W","A",".","E",".")),
                Arguments.of("PAARD", "AAPRD", List.of("P","A",".","R","D"))
        );
    }

    @Test
    @DisplayName("Guessing a word correctly ends the current round")
    void correctGuess(){
        when(gameRepository.getGameById(anyInt())).thenReturn(Optional.of(new Game("WOORD")));

        Progress progress = gameService.guess(1, "WOORD");

        assertEquals(GameState.WAITING_FOR_ROUND, progress.getGameState());
    }

    @Test
    @DisplayName("Making a guess when not playing throws an exception")
    void guessNotPlaying(){

        Game game = new Game("woord");
        game.guess("woord");

        when(gameRepository.getGameById(anyInt())).thenReturn(Optional.of(game));

        assertThrows(LingoGameException.class, () -> gameService.guess(1, "paard"));
    }

    @Test
    @DisplayName("Starting a new round when current round isnt over throws an exception")
    void startRoundException(){
        when(gameRepository.getGameById(anyInt())).thenReturn(Optional.of(new Game("woord")));

        assertThrows(LingoGameException.class, () -> gameService.newRound(1));
    }

    @Test
    @DisplayName("Starting the second round gets the correct word length")
    void correctWordLength(){
        Game game = new Game("woord");
        game.guess("woord");

        when(gameRepository.getGameById(anyInt())).thenReturn(Optional.of(game));

        Progress progress = gameService.newRound(1);

        assertEquals(6, progress.getLastHint().size());
    }

    @Test
    @DisplayName("After a 7 letter word the length goes back to 5")
    void wordLengthReset(){
        Game game = new Game("woorden");
        game.guess("woorden");

        when(gameRepository.getGameById(anyInt())).thenReturn(Optional.of(game));

        Progress progress = gameService.newRound(1);

        assertEquals(5, progress.getLastHint().size());
    }




}
