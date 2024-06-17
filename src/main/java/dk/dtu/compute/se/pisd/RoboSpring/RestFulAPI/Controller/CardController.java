package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Card;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.BoardRepository;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.CardsRepository;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.PlayerRegisters;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardController {

    private final CardsRepository cardsRepository;
    private final PlayerRepository playerRepository;
    private final BoardRepository boardRepository;

    public CardController(CardsRepository cardsRepository, PlayerRepository playerRepository, BoardRepository boardRepository) {
        this.cardsRepository = cardsRepository;
        this.playerRepository = playerRepository;
        this.boardRepository = boardRepository;
    }

    @PostMapping(value = "set/player/cards")
    public void setPlayerCards(@RequestBody PlayerRegisters cards)
    {
        List<Card> cardsOnHand = cardsRepository.findAllByPlayerIDAndGameIDAndLocation(cards.getPlayerID(), cards.getGameID(), "HAND");
        cardsRepository.deleteAll(cardsOnHand);

        for (int index : cards.getRegisterCards())
        {
            cardsOnHand.get(index).setLocation("REGISTER");
        }
        cardsRepository.saveAll(cardsOnHand);

        List<Player> players = playerRepository.findAllByGameID(cards.getGameID());
        for (Player player : players)
        {
            List<Card> registerCards = cardsRepository.findAllByPlayerIDAndGameIDAndLocation(player.getId(), cards.getGameID(), "REGISTER");
            if (registerCards.isEmpty())
            {
                return;
            }
        }
        List<Board> boards = boardRepository.findAllByGameID(cards.getGameID());
        boardRepository.deleteAllByGameID(cards.getGameID());
        boards.forEach(board -> { board.setPhase("ACTIVATION"); boardRepository.save(board); });

        //TODO check if this works and add functionality so that the server goes through the activation phase
    }
}