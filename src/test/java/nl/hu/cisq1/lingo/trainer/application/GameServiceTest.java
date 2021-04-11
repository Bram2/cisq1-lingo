package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.GameState;
import nl.hu.cisq1.lingo.trainer.domain.Progress;
import nl.hu.cisq1.lingo.trainer.domain.exception.LingoGameException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.ABSENT;
import static nl.hu.cisq1.lingo.trainer.domain.Mark.CORRECT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GameServiceTest {

    @Test
    @DisplayName("Start a new game")
    void startGame(){

        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);
        GameService gameService = new GameService(gameRepository,wordService);

        when(wordService.provideRandomWord(anyInt())).thenReturn("WOORD");

        Progress progress = gameService.startGame();

        assertEquals(GameState.PLAYING, progress.getGameState());
        assertEquals(1, progress.getRoundNumber());
    }

    @Test
    @DisplayName("Make an attempt")
    void guessWord(){

        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);
        GameService gameService = new GameService(gameRepository,wordService);

        when(gameRepository.getGameById(anyInt())).thenReturn(Optional.of(new Game("WOORD")));

        Progress progress = gameService.guess(1, "BAARD");

        assertEquals(List.of("W", ".", ".", "R", "D"), progress.getLastHint());
    }

    @Test
    @DisplayName("Round is over when word is guessed")
    void correctGuess(){

        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);
        GameService gameService = new GameService(gameRepository,wordService);

        when(gameRepository.getGameById(anyInt())).thenReturn(Optional.of(new Game("WOORD")));

        Progress progress = gameService.guess(1, "WOORD");

        assertEquals(GameState.WAITING_FOR_ROUND, progress.getGameState());
    }

    @Test
    @DisplayName("Start a new round")
    void startRound(){

        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);
        GameService gameService = new GameService(gameRepository,wordService);

        Game game = new Game("WOORD");
        game.guess("WOORD");

        when(gameRepository.getGameById(anyInt())).thenReturn(Optional.of(game));
        when(wordService.provideRandomWord(6)).thenReturn("HUIZEN");

        Progress progress = gameService.newRound(1);

        assertEquals(GameState.PLAYING, progress.getGameState());
        assertEquals(2, progress.getRoundNumber());
    }

    @Test
    @DisplayName("Cant start a round when current round isnt done")
    void startRoundError(){

        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);
        GameService gameService = new GameService(gameRepository,wordService);

        Game game = new Game("WOORD");

        when(gameRepository.getGameById(anyInt())).thenReturn(Optional.of(game));

        assertThrows(LingoGameException.class, () -> gameService.newRound(1));
    }

    @Test
    @DisplayName("Cant guess when round is done")
    void guessRoundDone(){

        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);
        GameService gameService = new GameService(gameRepository,wordService);

        Game game = new Game("WOORD");
        game.guess("WOORD");

        when(gameRepository.getGameById(anyInt())).thenReturn(Optional.of(game));

        assertThrows(LingoGameException.class, () -> gameService.guess(1, "BAARD"));
    }

    @Test
    @DisplayName("Guessing a word adds score correctly")
    void addScore(){

        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);
        GameService gameService = new GameService(gameRepository,wordService);

        Game game = new Game("WOORD");

        when(gameRepository.getGameById(anyInt())).thenReturn(Optional.of(game));

        Progress progress = gameService.guess(1,"WOORD");

        assertEquals(25, progress.getScore());
    }

    @Test
    @DisplayName("Guessing a word gives correct amount of feedback")
    void correctFeedback(){

        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);
        GameService gameService = new GameService(gameRepository,wordService);

        Game game = new Game("WOORD");

        when(gameRepository.getGameById(anyInt())).thenReturn(Optional.of(game));

        Progress progress = gameService.guess(1,"PAARD");

        assertEquals(List.of(ABSENT, ABSENT, ABSENT, CORRECT, CORRECT), progress.getFeedback());
    }

    @Test
    @DisplayName("Get game returns game progress")
    void getGame(){

        WordService wordService = mock(WordService.class);
        GameRepository gameRepository = mock(GameRepository.class);
        GameService gameService = new GameService(gameRepository,wordService);

        Game game = new Game("WOORD");

        when(gameRepository.getGameById(anyInt())).thenReturn(Optional.of(game));

        Progress progress = gameService.getGameProgress(1);

        assertEquals(List.of("W",".",".",".","."), progress.getLastHint());
        assertEquals(GameState.PLAYING, progress.getGameState());
    }



}