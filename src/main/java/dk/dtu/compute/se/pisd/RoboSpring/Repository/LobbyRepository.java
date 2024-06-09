package dk.dtu.compute.se.pisd.RoboSpring.Repository;

import dk.dtu.compute.se.pisd.RoboSpring.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.Model.Lobby;
import dk.dtu.compute.se.pisd.RoboSpring.Model.LobbyObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LobbyRepository extends JpaRepository<LobbyObject, Long> {

    public LobbyObject findLobbyObjectByGameID(Long id);
}
