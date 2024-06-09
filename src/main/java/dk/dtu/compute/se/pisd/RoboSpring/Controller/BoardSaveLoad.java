package dk.dtu.compute.se.pisd.RoboSpring.Controller;
import dk.dtu.compute.se.pisd.RoboSpring.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.Model.CompleteBoard;
import dk.dtu.compute.se.pisd.RoboSpring.Model.EnergyCube;
import dk.dtu.compute.se.pisd.RoboSpring.Model.Player.Card;
import dk.dtu.compute.se.pisd.RoboSpring.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.Repository.BoardRepository;
import dk.dtu.compute.se.pisd.RoboSpring.Repository.CardsRepository;
import dk.dtu.compute.se.pisd.RoboSpring.Repository.EnergyRepository;
import dk.dtu.compute.se.pisd.RoboSpring.Repository.PlayerRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//Base endpoint
public class BoardSaveLoad
{
    private BoardRepository boardRepository;
    private EnergyRepository energyRepository;
    private PlayerRepository playerRepository;
    private CardsRepository cardsRepository;

    public BoardSaveLoad(BoardRepository boardRepository, EnergyRepository energyRepository, PlayerRepository playerRepository, CardsRepository cardsRepository)
    {
        this.boardRepository = boardRepository;
        this.energyRepository = energyRepository;
        this.playerRepository = playerRepository;
        this.cardsRepository = cardsRepository;
    }

    @RequestMapping(value = "set/boards")
    public CompleteBoard saveBoard(@RequestBody CompleteBoard completeBoard)
    {
        Board boardToSave = boardRepository.save(completeBoard.getBoard());
        List<EnergyCube> energyCubes = completeBoard.getEnergyCubes();
        for (EnergyCube energyCube : energyCubes)
        {
            energyCube.setBoardID(boardToSave.getId());
            energyRepository.save(energyCube);
        }
        for (Player player : completeBoard.getPlayerList())
        {
            player.setBoardID(boardToSave.getId());
            playerRepository.save(player);
        }
        for (Card card : completeBoard.getCards())
        {
            card.setBoardID(completeBoard.getBoardID());
            card.setPlayerID(completeBoard.getPlayerID());
            cardsRepository.save(card);
        }
        return completeBoard;
    }
    @RequestMapping(value = "get/boards/single")
    public CompleteBoard loadBoard(Long boardID, Long playerID)
    {
        CompleteBoard completeBoard = new CompleteBoard();
        completeBoard.setBoard(boardRepository.findBoardById(boardID));
        List<Player> playerList = playerRepository.findPlayersByBoardID(boardID);
        completeBoard.setPlayerList(playerList);
        completeBoard.setEnergyCubes(energyRepository.findEnergyCubesByBoardID(boardID));
        if (completeBoard.getBoard() == null || completeBoard.getPlayerList() == null || completeBoard.getEnergyCubes() == null || completeBoard.getCards() == null)
        {
            return null;
        }
        return completeBoard;
    }
    @RequestMapping(value = "set/boards/single/delete")
    public boolean deleteBoard(Long boardID)
    {
        List<Player> playerList = playerRepository.findPlayersByBoardID(boardID);
        for (Player player : playerList)
        {
            List<Card> cardList = cardsRepository.findAllByPlayerID(player.getId());
            cardsRepository.deleteAll(cardList);
        }
        List<EnergyCube> energyCubeList = energyRepository.findEnergyCubesByBoardID(boardID);
        playerRepository.deleteAll(playerList);
        energyRepository.deleteAll(energyCubeList);
        boardRepository.deleteById(boardID);
        return true;
    }



}
