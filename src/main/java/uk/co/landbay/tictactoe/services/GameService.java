package uk.co.landbay.tictactoe.services;

import uk.co.landbay.tictactoe.restEntities.Coordinate;
import uk.co.landbay.tictactoe.restEntities.Game;

import java.sql.SQLException;

public interface GameService {

    Game createGame();

    Game updateGame(String gameId, Coordinate coordinate) throws SQLException;

    Game showGame(String id) throws SQLException;
}
