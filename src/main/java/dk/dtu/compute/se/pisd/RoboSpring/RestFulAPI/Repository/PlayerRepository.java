package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long>
{

    Player findPlayerById(Long id);

    List<Player> findAllByGameID(Long id);
    List<Player> findPlayersByGameIDAndTurnID(Long id, int turnID);
}
