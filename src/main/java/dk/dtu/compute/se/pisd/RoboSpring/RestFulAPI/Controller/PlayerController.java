package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.PlayerRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class PlayerController
{
    private final PlayerRepository playerRepository;

    PlayerController(PlayerRepository playerRepository)
    {
        this.playerRepository = playerRepository;
    }


    /**
     * @param gameID,playerID playerID to determine who wants to leave and which game to leave.
     * @return boolean for whether the player has left successfully.
     * @author Adel
     */
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
