package uk.co.landbay.tictactoe.restEntities;

import lombok.Getter;
import lombok.Setter;
import uk.co.landbay.tictactoe.data.enums.GameOutcome;
import uk.co.landbay.tictactoe.data.enums.GameStatus;
import uk.co.landbay.tictactoe.data.enums.PlayerType;

@Getter
@Setter
public class Game {

    private String id;

    private Character[][] board;

    private PlayerType nextTurn;

    private GameOutcome gameOutcome;

    private GameStatus gameStatus;
}
