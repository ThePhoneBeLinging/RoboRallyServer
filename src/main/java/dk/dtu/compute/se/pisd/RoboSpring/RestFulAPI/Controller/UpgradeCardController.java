package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.UpgradeCard;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.PlayerRepository;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.UpgradeCardRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @RequestMapping(value = "get/boards/upgradeCards/shop")
    public List<UpgradeCard> getUpgradeCards(Long gameID)
    {
        return upgradeCardRepository.findByPlayerIDIsNullAndGameID(gameID);
    }

    @RequestMapping(value = "get/boards/upgradeCards/active")
    public List<UpgradeCard> getActiveUpgradeCards(Long gameID, Long playerID)
    {
        return upgradeCardRepository.findActiveUpgradeCardsByPlayerIDAndGameID(playerID, gameID);
    }

    @RequestMapping(value = "set/boards/upgradeCards/addToPlayer")
    public boolean getUpgradeCards(Long gameID, Long playerID, Long turnID, String upgradeCardName)
    {
        UpgradeCard upgradeCard = new UpgradeCard();
        Player player = playerRepository.findPlayerByGameIDAndPlayerIDAndTurnID(gameID, Math.toIntExact(playerID), Math.toIntExact(turnID));
        upgradeCard.setGameID(gameID);
        upgradeCard.setPlayerID(playerID);
        upgradeCard.setCardName(upgradeCardName);
        upgradeCard.setPrice(upgradeCardRepository.findUpgradeCardByGameIDAndCardName(gameID, upgradeCardName).getPrice());
        player.setEnergyCubes(player.getEnergyCubes() - upgradeCardRepository.findUpgradeCardByGameIDAndCardName(gameID, upgradeCardName).getPrice());
        playerRepository.save(player);
        upgradeCardRepository.save(upgradeCard);
        return true;
    }
}
