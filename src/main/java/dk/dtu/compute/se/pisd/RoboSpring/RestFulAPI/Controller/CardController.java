package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Card;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.CardsRepository;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.PlayerRegisters;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardController
{

    private final CardsRepository cardsRepository;

    public CardController(CardsRepository cardsRepository)
    {
        this.cardsRepository = cardsRepository;
    }

    @PostMapping(value = "set/player/cards")
    public void setPlayerCards(@RequestBody PlayerRegisters cards)
    {
        List<Card> cardsOnHand = cardsRepository.findAllByPlayerIDAndGameIDAndLocation(cards.getPlayerID(), cards.getGameID(), "HAND");
        cardsRepository.deleteAll(cardsOnHand);

        for (int index : cards.getRegisterCards()) {
            cardsOnHand.get(index).setLocation("REGISTER");
        }
        cardsRepository.saveAll(cardsOnHand);
    }
}