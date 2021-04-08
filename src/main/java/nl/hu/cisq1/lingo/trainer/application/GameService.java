package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Mark;
import nl.hu.cisq1.lingo.trainer.domain.Progress;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GameService {

    public final GameRepository gameRepository;
    public final WordService wordService;

    public GameService(GameRepository gameRepository, WordService wordService) {
        this.gameRepository = gameRepository;
        this.wordService = wordService;
    }

    public Progress startGame(){
        Game game = new Game(wordService.provideRandomWord(5));

        gameRepository.save(game);
        return getProgress(game);
    }

    public Progress guess(int id, String attempt){
        Game game = getGame(id);
        game.guess(attempt.toUpperCase());

        gameRepository.save(game);
        return getProgress(game);
    }

    public Progress newRound(int id){
        Game game = getGame(id);

        int lastWordLength = game.getLastRound().getWord().length();
        String randomWord;

        if(lastWordLength < 7)
            randomWord = wordService.provideRandomWord(game.getWordLength() + 1);
        else
            randomWord = wordService.provideRandomWord(5);

        game.startRound(randomWord);
        gameRepository.save(game);

        return getProgress(game);
    }

    public Game getGame(int id){
        return gameRepository.getGameById(id).orElseThrow(RuntimeException::new);
    }

    public Progress getProgress(Game game){
        List<Mark> feedback = new ArrayList<>();
        if(!game.getLastRound().getFeedback().isEmpty())
            feedback = game.getLastRound().getFeedback().get(game.getLastRound().getFeedback().size() - 1).getMarks();

        return new Progress(
                game.getId(),
                game.getScore(),
                game.getLastRound().giveHint(),
                feedback,
                game.getGameState(),
                game.getRounds().size());
    }

}
