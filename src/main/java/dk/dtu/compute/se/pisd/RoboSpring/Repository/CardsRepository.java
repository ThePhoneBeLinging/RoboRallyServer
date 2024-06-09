package dk.dtu.compute.se.pisd.RoboSpring.Repository;

import dk.dtu.compute.se.pisd.RoboSpring.Model.Player.Card;
import dk.dtu.compute.se.pisd.RoboSpring.Model.Player.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardsRepository extends JpaRepository<Card, Long> {
    public List<Card> findAllByPlayerID(Long playerID);
}
