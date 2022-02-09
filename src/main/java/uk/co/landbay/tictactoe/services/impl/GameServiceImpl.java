package uk.co.landbay.tictactoe.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.landbay.tictactoe.data.enums.GameOutcome;
import uk.co.landbay.tictactoe.data.enums.GameStatus;
import uk.co.landbay.tictactoe.data.enums.PlayerType;
import uk.co.landbay.tictactoe.data.repositories.GameRepository;
import uk.co.landbay.tictactoe.restEntities.Coordinate;
import uk.co.landbay.tictactoe.restEntities.Game;
import uk.co.landbay.tictactoe.services.GameService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private DataSource dataSource;

    @Override
    public Game createGame() {
        var res = new uk.co.landbay.tictactoe.data.entities.Game();
        res.setBoard(serializeBoard(new Character[3][3]));
        res.setNextTurn(PlayerType.PLAYER_ONE);
        res.setGameStatus(GameStatus.STARTED);

        return convert(gameRepository.save(res));
    }

    @Override
    public Game updateGame(String gameId, Coordinate coordinate) throws SQLException {
        var game = getGameFromDb(gameId);
        if (game.getGameStatus() != GameStatus.STARTED) {
            throw new RuntimeException("Game is already over.");
        }

        var arrayBoard = deserializeBoard(game.getBoard());

        game.setBoard(serializeBoard(applyMove(arrayBoard, coordinate, game.getNextTurn())));
        checkWinner(game, arrayBoard);
        switchPlayer(game);

        return convert(gameRepository.save(game));
    }

    @Override
    public Game showGame(String gameId) throws SQLException {
        return convert(getGameFromDb(gameId));
    }

    public void printGame(String gameId) throws SQLException {
        var game = convert(getGameFromDb(gameId));

        var asciiArt = new StringBuilder();
        for (int i = 0; i < game.getBoard().length; ++i) {
            asciiArt.append("\n+---+---+---+\n|");
            for (int j = 0; j < game.getBoard()[i].length; ++j) {
                asciiArt.append(" ").append(game.getBoard()[i][j] == null ? " " : game.getBoard()[i][j]).append(" |");
            }
        }
        asciiArt.append("\n+---+---+---+\n");

        System.out.println(asciiArt);
    }

    public uk.co.landbay.tictactoe.data.entities.Game getGameFromDb(String gameId) throws SQLException {
        String sql = "select * from game where id = '" + gameId + "'";
        var connection = dataSource.getConnection();
        var rs = connection.createStatement().executeQuery(sql);
        var res = new uk.co.landbay.tictactoe.data.entities.Game();
        if (rs.next()) {
            res.setId(rs.getString("id"));
            var gameStatus = rs.getString("game_status");
            if (gameStatus != null) {
                res.setGameStatus(GameStatus.valueOf(gameStatus));
            }

            var gameOutcome = rs.getString("game_outcome");
            if (gameOutcome != null) {
                res.setGameOutcome(GameOutcome.valueOf(gameOutcome));
            }

            res.setNextTurn(PlayerType.valueOf(rs.getString("next_turn")));
            res.setBoard(rs.getString("board"));
            return res;
        } else {
            throw new RuntimeException(gameId + " not found.");
        }
    }

    private void checkWinner(uk.co.landbay.tictactoe.data.entities.Game game, Character[][] arrayBoard) {
        // The way to win is to have a whole row or column or diagonal.
        // We can check for the current player who's just played, no need to check the other one
        var currentPlayerSymbol = game.getNextTurn().getSymbol();
        boolean diagonalWin = false;
        if (arrayBoard[1][1] == currentPlayerSymbol) {
            // There is a chance for diagonals. Otherwise ignore.
            diagonalWin = (arrayBoard[0][0] == currentPlayerSymbol && arrayBoard[1][1] == currentPlayerSymbol && arrayBoard[2][2] == currentPlayerSymbol)
                    || (arrayBoard[2][0] == currentPlayerSymbol && arrayBoard[1][1] == currentPlayerSymbol && arrayBoard[0][2] == currentPlayerSymbol);
        }

        boolean horizontalWin = Arrays.stream(arrayBoard).anyMatch(row -> Arrays.stream(row)
                .allMatch(symbol -> symbol == currentPlayerSymbol));

        boolean verticalWin = (arrayBoard[0][0] == currentPlayerSymbol && arrayBoard[1][0] == currentPlayerSymbol && arrayBoard[2][0] == currentPlayerSymbol)
                || (arrayBoard[0][1] == currentPlayerSymbol && arrayBoard[1][1] == currentPlayerSymbol && arrayBoard[2][1] == currentPlayerSymbol)
                || (arrayBoard[0][2] == currentPlayerSymbol && arrayBoard[1][2] == currentPlayerSymbol && arrayBoard[2][2] == currentPlayerSymbol);

        if (diagonalWin || horizontalWin || verticalWin) {
            game.setGameStatus(GameStatus.FINISHED);
            game.setGameOutcome(game.getNextTurn() == PlayerType.PLAYER_ONE ? GameOutcome.PLAYER_ONE_WINS : GameOutcome.PLAYER_TWO_WINS);
        }

        if (Arrays.stream(arrayBoard).allMatch(row -> Arrays.stream(row).noneMatch(Objects::isNull))) {
            game.setGameOutcome(GameOutcome.DRAW);
            game.setGameStatus(GameStatus.FINISHED);
        }
    }

    private void switchPlayer(uk.co.landbay.tictactoe.data.entities.Game game) {
        game.setNextTurn(game.getNextTurn().getNextPlayer());
    }

    private Character[][] applyMove(Character[][] arrayBoard, Coordinate coordinate, PlayerType playerType) {
        if (arrayBoard[coordinate.getX()][coordinate.getY()] != null) {
            throw new RuntimeException("Cell already in use. Please chose another one.");
        }

        arrayBoard[coordinate.getX()][coordinate.getY()] = playerType.getSymbol();
        return arrayBoard;
    }

    private Game convert(uk.co.landbay.tictactoe.data.entities.Game entity) {
        var res = new Game();
        res.setId(entity.getId());
        res.setGameOutcome(entity.getGameOutcome());
        res.setNextTurn(entity.getNextTurn());
        res.setGameStatus(entity.getGameStatus());

        res.setBoard(deserializeBoard(entity.getBoard()));
        return res;
    }

    private uk.co.landbay.tictactoe.data.entities.Game convert(Game entity) {
        var res = new uk.co.landbay.tictactoe.data.entities.Game();
        res.setId(entity.getId());
        res.setGameOutcome(entity.getGameOutcome());
        res.setNextTurn(entity.getNextTurn());
        res.setGameStatus(entity.getGameStatus());

        res.setBoard(serializeBoard(entity.getBoard()));
        return res;
    }

    private Character[][] deserializeBoard(String board) {
        var arrayBoard = new Character[3][3];

        int i = 0;
        int j = 0;
        for (var cell : board.toCharArray()) {
            arrayBoard[i][j] = cell == '0' ? null : cell;
            if (++j % 3 == 0) {
                ++i;
                j = 0;
            }
        }

        return arrayBoard;
    }

    private String serializeBoard(Character[][] arrayBoard) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                sb.append(arrayBoard[i][j] == null ? '0' : arrayBoard[i][j]);
            }
        }

        return sb.toString();
    }
}
