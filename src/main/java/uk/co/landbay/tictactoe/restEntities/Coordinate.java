package uk.co.landbay.tictactoe.restEntities;

import lombok.Getter;

/**
 * The coordinate on the board. X is the horizontal axis
 * and y the vertical one. Values range: [0-2].
 */
@Getter
public class Coordinate {

    private int x;

    private int y;

    public void setX(int x) {
        if (x > 2) {
            throw new RuntimeException("Value is too high for the X-axis");
        }
        this.x = x;
    }

    public void setY(int y) {
        if (y > 2) {
            throw new RuntimeException("Value is too high for the Y-axis");
        }
        this.y = y;
    }
}
