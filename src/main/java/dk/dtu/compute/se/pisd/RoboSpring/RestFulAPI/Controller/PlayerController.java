package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.PlayerRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class PlayerController
{
    private final PlayerRepository playerRepository;

    PlayerController(PlayerRepository playerRepository)
    {
        this.playerRepository = playerRepository;
    }

    @RequestMapping("get/players")
    public List<Player> getPlayers()
    {
        return playerRepository.findAll();
    }

    @RequestMapping("players/leave")
    public boolean leaveGame(Long gameID, Long playerID)
    {
        Player player = playerRepository.findPlayerByGameIDAndPlayerID(gameID, playerID);
        if (player == null)
        {
            return false;
        }
        playerRepository.delete(player);
        return true;
    }

}
