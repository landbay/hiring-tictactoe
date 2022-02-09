package uk.co.landbay.tictactoe.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.co.landbay.tictactoe.restEntities.Coordinate;
import uk.co.landbay.tictactoe.restEntities.Game;
import uk.co.landbay.tictactoe.restEntities.Request;
import uk.co.landbay.tictactoe.services.GameService;

import javax.transaction.Transactional;

import java.sql.SQLException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping(value = { "/game" }, produces = {APPLICATION_JSON_VALUE})
@Transactional
public class GameEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameEndpoint.class);

    @Autowired
    private GameService gameService;

    @RequestMapping(value = { "/create" })
    public ResponseEntity<Game> createGame() {
        LOGGER.info("Creating new Game");
        var res = gameService.createGame();
        return ok(res);
    }

    @RequestMapping(value = { "/play" },
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.PUT)
    public ResponseEntity<Game> play(@RequestBody Request request) throws SQLException {
        LOGGER.info("Playing");
        var res = gameService.updateGame(request.getGameId(), request.getCoordinate());
        return ok(res);
    }

    @RequestMapping(value = { "/show" },
            produces = { "application/json" },
            method = RequestMethod.POST)
    public ResponseEntity<Game> showGame(@RequestBody Request request) throws SQLException {
        LOGGER.info("Playing");
        var res = gameService.showGame(request.getGameId());
        return ok(res);
    }
}
