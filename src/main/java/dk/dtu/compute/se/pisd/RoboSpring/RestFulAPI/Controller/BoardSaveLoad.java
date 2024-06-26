package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.CompleteGame;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.EnergyCube;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Card;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.UpgradeCard;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.*;

import java.util.ArrayList;
import java.util.List;

public class BoardSaveLoad
{
    private final BoardRepository boardRepository;
    private final EnergyRepository energyRepository;
    private final PlayerRepository playerRepository;
    private final CardsRepository cardsRepository;
    private final UpgradeCardRepository upgradeCardRepository;

    public BoardSaveLoad(BoardRepository boardRepository, EnergyRepository energyRepository,
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
     * @param completeGame CompleteGame that is to be saved in the database
     * @return returns the completeGame that is saved
     * @author Elias and Frederik
     */
    public CompleteGame saveBoard(CompleteGame completeGame)
    {
        // Deletes previous board with same gameID and turnID
        BoardController boardController = new BoardController(boardRepository, energyRepository, playerRepository,
                cardsRepository, upgradeCardRepository);
        boardController.deleteBoard(completeGame.getGameID(), completeGame.getTurnID());

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
            player.setHasRetrievedProgrammingPhase(false);
            playerRepository.save(player);
        }
        this.saveCards(completeGame);
        //fromServerBoardToGameBoard(completeGame);
        return completeGame;
    }

    /**
     * Takes a completeGame and updates the database to only contain cards that are in completeGame.
     * Uses optimized algorithm that allows us to reduce how many cards are updated in the database.
     *
     * @param completeGame that holds the cards that are to be saved.
     * @author Elias
     */
    public void saveCards(CompleteGame completeGame)
    {
        List<Card> cardsToSave = new ArrayList<>();
        List<Card> cardsToDelete = new ArrayList<>();
        List<Card> cardsOnServer = cardsRepository.findAllByGameID(completeGame.getGameID());
        List<Card> cardsInCompleteGame = completeGame.getCards();
        List<UpgradeCard> upgradeCardsToSave = new ArrayList<>();
        List<UpgradeCard> upgradeCardsToDelete = new ArrayList<>();
        List<UpgradeCard> upgradeCardsOnServer =
                upgradeCardRepository.findUpgradeCardsByGameID(completeGame.getGameID());
        upgradeCardsToSave.addAll(completeGame.getUpgradeCards());
        upgradeCardsToDelete.addAll(upgradeCardsOnServer);
        upgradeCardsToDelete.removeAll(completeGame.getUpgradeCards());
        upgradeCardsToSave.removeAll(upgradeCardsOnServer);
        upgradeCardRepository.deleteAll(upgradeCardsToDelete);
        upgradeCardRepository.saveAll(upgradeCardsToSave);
        cardsToDelete.addAll(cardsOnServer);
        cardsToDelete.removeAll(completeGame.getCards());
        cardsToSave.addAll(cardsInCompleteGame);
        cardsToSave.removeAll(cardsOnServer);
        cardsRepository.deleteAll(cardsToDelete);
        cardsRepository.saveAll(cardsToSave);
    }

    /**
     * @param gameID,turnID To use for assembling a completeGame based on data from our database
     * @return returns the completeGame assembled by looking up in our database using the gameID and turnID
     * @author Frederik and Elias
     */
    public CompleteGame loadBoard(Long gameID, int turnID)
    {
        CompleteGame completeGame = new CompleteGame();
        completeGame.setGameID(gameID);
        completeGame.setBoard(boardRepository.findBoardByGameIDAndTurnID(gameID, turnID));
        List<Player> playerList = playerRepository.findPlayersByGameIDAndTurnID(gameID, turnID);
        completeGame.setPlayerList(playerList);
        completeGame.setEnergyCubes(energyRepository.findEnergyCubesByGameIDAndTurnID(gameID, turnID));
        completeGame.setCards(cardsRepository.findAllByGameID(gameID));
        completeGame.setUpgradeCards(upgradeCardRepository.findUpgradeCardsByGameID(gameID));
        completeGame.setTurnID(turnID);
        if (completeGame.getBoard() == null || completeGame.getPlayerList() == null || completeGame.getEnergyCubes() == null || completeGame.getCards() == null || completeGame.getUpgradeCards() == null)
        {
            return null;
        }
        return completeGame;
    }
}
