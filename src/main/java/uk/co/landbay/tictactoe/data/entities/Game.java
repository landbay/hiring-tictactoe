package uk.co.landbay.tictactoe.data.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import uk.co.landbay.tictactoe.data.enums.GameOutcome;
import uk.co.landbay.tictactoe.data.enums.GameStatus;
import uk.co.landbay.tictactoe.data.enums.PlayerType;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Game {

    @Id
    @GeneratedValue(
            generator = "hibernate-uuid"
    )
    @GenericGenerator(
            name = "hibernate-uuid",
            strategy = "uuid2"
    )
    private String id;

    @Column
    private String board;

    @Column
    @Enumerated(EnumType.STRING)
    private PlayerType nextTurn;

    @Column
    @Enumerated(EnumType.STRING)
    private GameOutcome gameOutcome;

    @Column
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;
}
