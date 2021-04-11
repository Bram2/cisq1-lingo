package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.GameService;
import nl.hu.cisq1.lingo.trainer.domain.Progress;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService service;


    public GameController(GameService service) {
        this.service = service;
    }


    @GetMapping("/{id}")
    public Progress getGame(@PathVariable int id){
        return service.getGameProgress(id);
    }

    @PostMapping
    public Progress startGame(){
        return service.startGame();
    }

    @PostMapping("/{id}/round")
    public Progress newRound(@PathVariable int id){
        return service.newRound(id);
    }

    @PostMapping("/{id}/guess")
    public Progress guess(@PathVariable int id, @RequestBody String attempt){
        return service.guess(id, attempt);
    }
}
