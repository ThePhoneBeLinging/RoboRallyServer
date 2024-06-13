package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.UpgradeCard;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.UpgradeCardRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UpgradeCardController
{

    private final UpgradeCardRepository upgradeCardRepository;

    public UpgradeCardController(UpgradeCardRepository upgradeCardRepository)
    {
        this.upgradeCardRepository = upgradeCardRepository;
    }

    @RequestMapping(value = "get/boards/upgradeCards/shop")
    public List<UpgradeCard> getUpgradeCards()
    {
        return upgradeCardRepository.findByPlayerIDIsNullAndGameID(1L);
    }

    @RequestMapping(value = "get/boards/upgradeCards/active")
    public List<UpgradeCard> getActiveUpgradeCards(Long gameID, Long playerID)
    {
        return upgradeCardRepository.findActiveUpgradeCardsByPlayerIDAndGameID(playerID, gameID);
    }

    @RequestMapping(value = "set/boards/upgradeCards/addToPlayer")
    public boolean getUpgradeCards(Long gameID, Long playerID, String upgradeCardName, int price)
    {
        UpgradeCard upgradeCard = new UpgradeCard();
        upgradeCard.setGameID(gameID);
        upgradeCard.setPlayerID(playerID);
        upgradeCard.setCardName(upgradeCardName);
        upgradeCard.setPrice(price);
        upgradeCardRepository.save(upgradeCard);
        return true;
    }
}
