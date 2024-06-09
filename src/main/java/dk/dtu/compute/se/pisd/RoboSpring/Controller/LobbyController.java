package dk.dtu.compute.se.pisd.RoboSpring.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.Repository.LobbyRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LobbyController
{
    private LobbyRepository lobbyRepository;
    LobbyController(LobbyRepository lobbyRepository)
    {
        this.lobbyRepository = lobbyRepository;
    }
}
