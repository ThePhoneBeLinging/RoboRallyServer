package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardsRepository extends JpaRepository<Card, Long>
{
    public List<Card> findAllByPlayerIDAndGameID(Long playerID, Long gameID);

    public List<Card> findAllByGameID(Long gameID);
}
