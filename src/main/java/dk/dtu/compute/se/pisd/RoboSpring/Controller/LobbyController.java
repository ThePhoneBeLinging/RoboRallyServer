package dk.dtu.compute.se.pisd.RoboSpring.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.Model.Lobby;
import dk.dtu.compute.se.pisd.RoboSpring.Model.LobbyObject;
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
    public LobbyObject createLobby()
    {
        LobbyObject lobbyObject = new LobbyObject();
        lobbyObject = lobbyRepository.save(lobbyObject);
        lobbyObject.setPlayerID(1L);
        return lobbyObject;
    }
}
