package dk.dtu.compute.se.pisd.RoboSpring.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.Model.Lobby;
import dk.dtu.compute.se.pisd.RoboSpring.Repository.LobbyRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LobbyController
{
    private final LobbyRepository lobbyRepository;

    LobbyController(LobbyRepository lobbyRepository)
    {
        this.lobbyRepository = lobbyRepository;
    }

    @RequestMapping(value = "lobby/create")
    public Lobby createLobby()
    {
        Long gameID = 1L;
        while (true)
        {
            List<Lobby> lobbyList = lobbyRepository.findLobbiesByGameID(gameID);
            if (lobbyList.isEmpty())
            {
                break;
            }
            gameID++;

        }
        Lobby lobby = new Lobby();
        lobby.setGameID(gameID);
        lobby.setPlayerID(1L);
        lobby = lobbyRepository.save(lobby);
        return lobby;
    }

    @RequestMapping(value = "lobby/join")
    public Lobby joinLobby(Long gameID)
    {
        Lobby lobby = new Lobby();
        lobby.setPlayerID(1L + lobbyRepository.countLobbyObjectsByGameID(gameID));
        lobby.setGameID(gameID);
        lobby = lobbyRepository.save(lobby);
        return lobby;
    }
}
