package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.CompleteGame;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Card;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.PlayerRegisters;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.*;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.controller.GameController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.RoboSpring.Util.fromServerBoardToGameBoard;

@RestController
public class CardController
{

    private final CardsRepository cardsRepository;
    private final PlayerRepository playerRepository;
    private final BoardRepository boardRepository;
    private final UpgradeCardRepository upgradeCardRepository;
    private final EnergyRepository energyRepository;

    public CardController(CardsRepository cardsRepository, PlayerRepository playerRepository,
                          BoardRepository boardRepository, UpgradeCardRepository upgradeCardRepository,
                          EnergyRepository energyRepository)
    {
        this.cardsRepository = cardsRepository;
        this.playerRepository = playerRepository;
        this.boardRepository = boardRepository;
        this.upgradeCardRepository = upgradeCardRepository;
        this.energyRepository = energyRepository;
    }

    @PostMapping(value = "set/player/cards")
    public void setPlayerCards(@RequestBody PlayerRegisters cards)
    {
        List<Card> cardsOnHand = cardsRepository.findAllByPlayerIDAndGameIDAndLocation(cards.getPlayerID(),
                cards.getGameID(), "HAND");
        cardsRepository.deleteAll(cardsOnHand);
        List<Card> register = new ArrayList<>();
        for (int index : cards.getRegisterCards())
        {
            cardsOnHand.get(index).setLocation("REGISTER");
            register.add(cardsOnHand.get(index));
        }
        cardsOnHand.removeAll(register);
        cardsRepository.saveAll(cardsOnHand);
        cardsRepository.saveAll(register);

        List<Player> players = playerRepository.findAllByGameID(cards.getGameID());
        for (Player player : players)
        {
            List<Card> registerCards = cardsRepository.findAllByPlayerIDAndGameIDAndLocation(player.getPlayerID(),
                    cards.getGameID(), "REGISTER");
            if (registerCards.isEmpty())
            {
                return;
            }
        }
        Board board = boardRepository.findBoardByGameIDAndTurnID(cards.getGameID(), 0);
        boardRepository.delete(board);
        board.setPhase("ACTIVATION");

        boardRepository.save(board);

        CompleteGame completeGame = new CompleteGame();
        completeGame.setBoard(boardRepository.findBoardByGameIDAndTurnID(cards.getGameID(), 0));
        completeGame.setEnergyCubes(null);
        completeGame.setPlayerList(players);
        completeGame.setCards(cardsRepository.findAllByGameID(cards.getGameID()));
        completeGame.setUpgradeCards(upgradeCardRepository.findActiveUpgradeCardsByGameID(cards.getGameID()));
        completeGame.setTurnID(0);
        completeGame.setGameID(cards.getGameID());


        dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board gameBoard = fromServerBoardToGameBoard(completeGame);

        GameController gameController = new GameController(gameBoard, boardRepository, energyRepository,
                playerRepository, cardsRepository, upgradeCardRepository);
        gameBoard.setTurnID(0);
        gameBoard.setCurrentPlayer(gameBoard.getPlayer(0));
        gameController.executePrograms();

        //TODO check if this works and add functionality so that the server goes through the activation phase
    }
}