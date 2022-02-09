package uk.co.landbay.tictactoe.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum PlayerType {
    PLAYER_ONE('X'),
    PLAYER_TWO('O');

    private final Character symbol;
    private PlayerType nextPlayer;

    static {
        PLAYER_ONE.nextPlayer = PLAYER_TWO;
        PLAYER_TWO.nextPlayer = PLAYER_ONE;
    }

    PlayerType(Character character) {
        symbol = character;
    }

}
