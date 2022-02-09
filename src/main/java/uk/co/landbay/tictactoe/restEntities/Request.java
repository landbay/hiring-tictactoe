package uk.co.landbay.tictactoe.restEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Request {
    private Coordinate coordinate;

    private String gameId;
}
