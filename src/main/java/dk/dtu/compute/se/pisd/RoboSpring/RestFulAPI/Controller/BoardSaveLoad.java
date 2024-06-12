package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.CompleteGame;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.EnergyCube;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Card;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.BoardRepository;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.CardsRepository;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.EnergyRepository;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.PlayerRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//Base endpoint
public class BoardSaveLoad
{
    private final BoardRepository boardRepository;
    private final EnergyRepository energyRepository;
    private final PlayerRepository playerRepository;
    private final CardsRepository cardsRepository;

    public BoardSaveLoad(BoardRepository boardRepository, EnergyRepository energyRepository,
                         PlayerRepository playerRepository, CardsRepository cardsRepository)
    {
        this.boardRepository = boardRepository;
        this.energyRepository = energyRepository;
        this.playerRepository = playerRepository;
        this.cardsRepository = cardsRepository;
    }

    @RequestMapping(value = "set/boards")
    public CompleteGame saveBoard(@RequestBody CompleteGame completeGame)
    {
        completeGame.getBoard().setGameID(completeGame.getGameID());
        Board boardToSave = boardRepository.save(completeGame.getBoard());
        List<EnergyCube> energyCubes = completeGame.getEnergyCubes();
        for (EnergyCube energyCube : energyCubes)
        {
            energyCube.setGameID(completeGame.getGameID());
            energyRepository.save(energyCube);
        }
        for (Player player : completeGame.getPlayerList())
        {
            player.setGameID(completeGame.getGameID());
            playerRepository.save(player);
        }
        for (Card card : completeGame.getCards())
        {
            card.setGameID(completeGame.getGameID());
            card.setPlayerID(completeGame.getSenderID());
            cardsRepository.save(card);
        }
        return completeGame;
    }

    @RequestMapping(value = "get/boards/single")
    public CompleteGame loadBoard(Long gameID, Long playerID)
    {
        CompleteGame completeGame = new CompleteGame();
        completeGame.setGameID(gameID);
        completeGame.setSenderID(playerID);
        completeGame.setBoard(boardRepository.findBoardByGameID(gameID));
        List<Player> playerList = playerRepository.findPlayersByGameID(gameID);
        completeGame.setPlayerList(playerList);
        completeGame.setEnergyCubes(energyRepository.findEnergyCubesByGameID(gameID));
        completeGame.setCards(cardsRepository.findAllByPlayerIDAndGameID(playerID, gameID));
        if (completeGame.getBoard() == null || completeGame.getPlayerList() == null || completeGame.getEnergyCubes() == null || completeGame.getCards() == null)
        {
            return null;
        }
        return completeGame;
    }

    @RequestMapping(value = "set/boards/single/delete")
    public boolean deleteBoard(Long gameID)
    {
        List<Player> playerList = playerRepository.findPlayersByGameID(gameID);
        List<Card> cardList = cardsRepository.findAllByGameID(gameID);
        cardsRepository.deleteAll(cardList);
        List<EnergyCube> energyCubeList = energyRepository.findEnergyCubesByGameID(gameID);
        playerRepository.deleteAll(playerList);
        energyRepository.deleteAll(energyCubeList);
        boardRepository.delete(boardRepository.findBoardByGameID(gameID));
        return true;
    }


}