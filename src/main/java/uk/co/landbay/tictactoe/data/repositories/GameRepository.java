package uk.co.landbay.tictactoe.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.co.landbay.tictactoe.data.entities.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, String> {
}
