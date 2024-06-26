package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.CompleteGame;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.EnergyCube;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Card;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
//Base endpoint
@RequestMapping(value = "")
public class BoardController
{
    private final BoardRepository boardRepository;
    private final EnergyRepository energyRepository;
    private final PlayerRepository playerRepository;
    private final CardsRepository cardsRepository;
    private final UpgradeCardRepository upgradeCardRepository;

    public BoardController(BoardRepository boardRepository, EnergyRepository energyRepository,
                           PlayerRepository playerRepository, CardsRepository cardsRepository,
                           UpgradeCardRepository upgradeCardRepository)
    {
        this.boardRepository = boardRepository;
        this.energyRepository = energyRepository;
        this.playerRepository = playerRepository;
        this.cardsRepository = cardsRepository;
        this.upgradeCardRepository = upgradeCardRepository;
    }

    /**
     * @return returns a list of all boards that are found in our database
     * @author Elias
     */
    @GetMapping
    //Specific endpoint for the method
    @RequestMapping(value = "")
    public ResponseEntity<List<Board>> getBoards()
    {
        List<Board> boardList = boardRepository.findAll();
        return ResponseEntity.ok(boardList);
    }

    /**
     * Takes gameID, turnID and playerID and returns a completeGame
     * A complete game is everything you need to set up a board on the playerSide
     *
     * @param gameID, turnID and playerID
     * @return CompleteGame
     * @author Elias & Mads
     */
    @RequestMapping(value = "get/boards/single")
    public CompleteGame getBoard(Long gameID, int turnID, Long playerID)
    {
        BoardSaveLoad boardSaveLoad = new BoardSaveLoad(boardRepository, energyRepository, playerRepository,
                cardsRepository, upgradeCardRepository);
        CompleteGame completeGame = boardSaveLoad.loadBoard(gameID, turnID);
        if (completeGame == null)
        {
            return null;
        }
        List<Card> playerCards = new ArrayList<Card>();

        List<Player> players = playerRepository.findPlayersByGameIDAndTurnID(gameID, turnID);
        int amountOfBoards = boardRepository.findAllByGameID(gameID).size();
        int triggerDelete = players.size() * 5 - 1;
        if (turnID == 0 && amountOfBoards > triggerDelete)
        {

            boolean toDelete = true;
            for (Player player : players)
            {
                if (player.getPlayerID().equals(playerID))
                {
                    playerRepository.delete(player);
                    player.setHasRetrievedProgrammingPhase(true);
                    playerRepository.save(player);
                }
                if (!player.hasRetrievedProgrammingPhase)
                {
                    toDelete = false;
                }
            }
            if (toDelete)
            {
                for (int i = 1; i < players.size() * 5 + 1; i++)
                {
                    this.deleteBoard(gameID, i);
                }
            }
        }
        for (Card card : completeGame.getCards())
        {
            if (card.getPlayerID() == playerID)
            {
                playerCards.add(card);
            }
        }
        completeGame.setCards(playerCards);
        return completeGame;
    }

    /**
     * Deletes all instances regarding the given gameID and turnID from our database
     *
     * @param gameID and turnID
     * @return CompleteGame
     * @author Elias & Mads
     */
    @RequestMapping(value = "set/boards/single/delete")
    public boolean deleteBoard(Long gameID, int TurnID)
    {
        List<Player> playerList = playerRepository.findPlayersByGameIDAndTurnID(gameID, TurnID);
        List<EnergyCube> energyCubeList = energyRepository.findEnergyCubesByGameIDAndTurnID(gameID, TurnID);
        playerRepository.deleteAll(playerList);
        energyRepository.deleteAll(energyCubeList);
        Board boardToDelete = boardRepository.findBoardByGameIDAndTurnID(gameID, TurnID);
        if (boardToDelete != null)
        {
            boardRepository.delete(boardToDelete);
        }
        return true;
    }
}
