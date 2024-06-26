package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.UpgradeCard;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.PlayerRepository;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.UpgradeCardRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpgradeCardController
{

    private final UpgradeCardRepository upgradeCardRepository;
    private final PlayerRepository playerRepository;

    public UpgradeCardController(UpgradeCardRepository upgradeCardRepository, PlayerRepository playerRepository)
    {
        this.upgradeCardRepository = upgradeCardRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * Endpoint for buying a upgradeCard
     *
     * @param gameID,playerID,upgradeCardName gameID, and player specify the player, and the upgradeCardName
     *                                        specifies the upgradeCard
     * @return boolean whether you have bought the card
     * @author Adel
     */
    @RequestMapping(value = "set/boards/upgradeCards/addToPlayer")
    public boolean buyUpgradeCard(Long gameID, Long playerID, String upgradeCardName)
    {
        upgradeCardName = upgradeCardName.replace("%20", " ");
        UpgradeCard upgradeCard = upgradeCardRepository.findUpgradeCardByGameIDAndCardName(gameID, upgradeCardName);
        upgradeCardRepository.delete(upgradeCard);
        Player player = playerRepository.findPlayerByGameIDAndPlayerIDAndTurnID(gameID, playerID, 0);
        playerRepository.delete(player);
        upgradeCard.setPlayerID(playerID);
        player.setEnergyCubes(player.getEnergyCubes() - upgradeCard.getPrice());
        playerRepository.save(player);
        upgradeCardRepository.save(upgradeCard);
        return true;
    }
}
