package dk.dtu.compute.se.pisd.RoboSpring.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.Model.Lobby;
import dk.dtu.compute.se.pisd.RoboSpring.Repository.LobbyRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LobbyController
{
    private LobbyRepository lobbyRepository;
    LobbyController(LobbyRepository lobbyRepository)
    {
        this.lobbyRepository = lobbyRepository;
    }

    @RequestMapping(value = "lobby/create")
    public Lobby createLobby()
    {
        Lobby lobby = new Lobby();
        lobby = lobbyRepository.save(lobby);
        lobby.setPlayerID(1L);
        return lobby;
    }
}
