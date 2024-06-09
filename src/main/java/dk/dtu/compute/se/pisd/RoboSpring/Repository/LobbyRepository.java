package dk.dtu.compute.se.pisd.RoboSpring.Repository;

import dk.dtu.compute.se.pisd.RoboSpring.Model.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LobbyRepository extends JpaRepository<Lobby, Long> {

    public Lobby findLobbyObjectByGameID(Long id);
    public int countLobbyObjectsByGameID(Long gameID);
}
