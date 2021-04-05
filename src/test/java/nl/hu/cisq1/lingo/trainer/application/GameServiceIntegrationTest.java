package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.GameState;
import nl.hu.cisq1.lingo.trainer.domain.Progress;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(CiTestConfiguration.class)
public class GameServiceIntegrationTest {

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


}
