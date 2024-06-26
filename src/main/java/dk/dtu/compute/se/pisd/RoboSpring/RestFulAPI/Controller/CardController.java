package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.CompleteGame;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Card;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.PlayerRegisters;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.*;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.controller.GameController;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Command;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * Based on the gameID, turnID, playerID and choice in the form of an int, you use this endpoint
     * for choosing an option during interactive phase.
     *
     * @param gameID, turnID, playerID, choice
     * @author Frederik, Emil and Elias
     */
    @GetMapping(value = "set/interactive/choice")
    public void setInteractiveChoice(Long gameID, int turnID, Long playerID, int choice)
    {
        List<Card> chosenOption = cardsRepository.findAllByPlayerIDAndGameIDAndLocation(playerID, gameID, "OPTION");
        cardsRepository.deleteAll(chosenOption);
        Card card = chosenOption.get(choice);

        BoardSaveLoad boardSaveLoad = new BoardSaveLoad(boardRepository, energyRepository, playerRepository,
                cardsRepository, upgradeCardRepository);

        dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board gameBoard =
                fromServerBoardToGameBoard(boardSaveLoad.loadBoard(gameID, turnID));
        GameController gameController = new GameController(gameBoard, boardRepository, energyRepository,
                playerRepository, cardsRepository, upgradeCardRepository);
        gameController.executeCommandOptionAndContinue(Command.valueOf(card.getCommand()));
    }

    /**
     * Takes an entity of PlayerRegisters, this is in short a list of integers representing the chosen cards, the
     * game id, and the playerID.
     * This method takes POST in json format.
     *
     * @param cards
     * @author Frederik, Emil and Elias
     */
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
        for (int i = 0; i < gameBoard.getPlayersNumber(); i++)
        {
            gameBoard.getPlayer(i).activeCardsPile.playerCards.clear();
            List<Card> serverCards =
                    cardsRepository.findAllByPlayerIDAndGameIDAndLocation(gameBoard.getPlayer(i).getPlayerID(),
                            gameBoard.getGameID(), "ACTIVE");
            for (Card serverCard : serverCards)
            {
                dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Card card =
                        new dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Card(Command.valueOf(serverCard.getCommand()));
                gameBoard.getPlayer(i).activeCardsPile.playerCards.add(card);
            }
        }
        GameController gameController = new GameController(gameBoard, boardRepository, energyRepository,
                playerRepository, cardsRepository, upgradeCardRepository);
        gameBoard.setTurnID(1);
        gameBoard.activateBoardElements();
        gameBoard.setCurrentPlayer(gameBoard.getPlayer(0));
        gameController.executePrograms();

        //TODO check if this works and add functionality so that the server goes through the activation phase
    }
}