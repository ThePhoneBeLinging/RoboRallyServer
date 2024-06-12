package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.UpgradeCard;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.UpgradeCardRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("get/boards/upgradeCards")
public class UpgradeCardController {

    private UpgradeCardRepository upgradeCardRepository;

    public UpgradeCardController(UpgradeCardRepository upgradeCardRepository) {
        this.upgradeCardRepository = upgradeCardRepository;
    }

    @RequestMapping(value = "/shop")
    public List<UpgradeCard> getUpgradeCards() {
        return upgradeCardRepository.findByPlayerIDIsNullAndGameID(1L);
    }

    @RequestMapping(value = "/active")
    public List<UpgradeCard> getActiveUpgradeCards() {
        return upgradeCardRepository.findActiveUpgradeCardsByPlayerIDAndGameID(1L, 1L);
    }
}
