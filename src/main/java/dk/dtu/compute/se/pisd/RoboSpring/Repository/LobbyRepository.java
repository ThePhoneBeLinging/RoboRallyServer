package dk.dtu.compute.se.pisd.RoboSpring.Repository;

import dk.dtu.compute.se.pisd.RoboSpring.Model.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LobbyRepository extends JpaRepository<Lobby, Long>
{

    List<Lobby> findLobbiesByGameID(Long id);

    Long countLobbyObjectsByGameID(Long gameID);
}
