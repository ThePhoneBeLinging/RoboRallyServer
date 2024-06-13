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

import java.util.List;

import static dk.dtu.compute.se.pisd.RoboSpring.Util.fromServerBoardToGameBoard;

public class BoardSaveLoad
{
    private static BoardRepository boardRepository;
    private static EnergyRepository energyRepository;
    private static PlayerRepository playerRepository;
    private static CardsRepository cardsRepository;

    public BoardSaveLoad(BoardRepository boardRepository, EnergyRepository energyRepository,
                         PlayerRepository playerRepository, CardsRepository cardsRepository)
    {
        BoardSaveLoad.boardRepository = boardRepository;
        BoardSaveLoad.energyRepository = energyRepository;
        BoardSaveLoad.playerRepository = playerRepository;
        BoardSaveLoad.cardsRepository = cardsRepository;
    }

    public static CompleteGame saveBoard(CompleteGame completeGame)
    {
        completeGame.getBoard().setGameID(completeGame.getGameID());
        completeGame.getBoard().setTurnID(completeGame.getTurnID());
        Board boardToSave = boardRepository.save(completeGame.getBoard());
        List<EnergyCube> energyCubes = completeGame.getEnergyCubes();
        for (EnergyCube energyCube : energyCubes)
        {
            energyCube.setGameID(completeGame.getGameID());
            energyCube.setTurnID(completeGame.getTurnID());
            energyRepository.save(energyCube);
        }
        for (Player player : completeGame.getPlayerList())
        {
            player.setGameID(completeGame.getGameID());
            player.setTurnID(completeGame.getTurnID());
            playerRepository.save(player);
        }
        for (Card card : completeGame.getCards())
        {
            card.setGameID(completeGame.getGameID());
            cardsRepository.save(card);
        }
        fromServerBoardToGameBoard(completeGame);
        return completeGame;
    }

    public static CompleteGame loadBoard(Long gameID, int turnID)
    {
        CompleteGame completeGame = new CompleteGame();
        completeGame.setGameID(gameID);
        completeGame.setBoard(boardRepository.findBoardByGameIDAndTurnID(gameID, turnID));
        List<Player> playerList = playerRepository.findPlayersByGameIDAndTurnID(gameID, turnID);
        completeGame.setPlayerList(playerList);
        completeGame.setEnergyCubes(energyRepository.findEnergyCubesByGameIDAndTurnID(gameID, turnID));
        completeGame.setCards(cardsRepository.findAllByGameID(gameID));
        if (completeGame.getBoard() == null || completeGame.getPlayerList() == null || completeGame.getEnergyCubes() == null || completeGame.getCards() == null)
        {
            return null;
        }
        return completeGame;
    }
}
