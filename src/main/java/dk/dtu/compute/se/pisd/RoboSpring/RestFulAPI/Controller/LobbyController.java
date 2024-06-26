package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.CompleteGame;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Lobby;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Card;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.*;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Space;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.RoboSpring.Util.fromGameBoardToServerBoard;

@RestController
public class LobbyController
{
    private final LobbyRepository lobbyRepository;
    private final BoardRepository boardRepository;
    private final PlayerRepository playerRepository;
    private final BoardController boardController;
    private final EnergyRepository energyRepository;
    private final CardsRepository cardsRepository;
    private final UpgradeCardRepository upgradeCardRepository;

    LobbyController(LobbyRepository lobbyRepository, BoardRepository boardRepository,
                    PlayerRepository playerRepository, BoardController boardController,
                    EnergyRepository energyRepository1, CardsRepository cardsRepository,
                    UpgradeCardRepository upgradeCardRepository)
    {
        this.lobbyRepository = lobbyRepository;
        this.boardRepository = boardRepository;
        this.playerRepository = playerRepository;
        this.boardController = boardController;
        this.energyRepository = energyRepository1;
        this.cardsRepository = cardsRepository;
        this.upgradeCardRepository = upgradeCardRepository;
    }

    /**
     * A GET endpoint for creating a lobby,
     *
     * @return A lobby with a gameID as well as the playerID that is assigned to the player
     * (as it is the creator it will be 1)
     * @author Elias
     */
    @RequestMapping(value = "lobby/create")
    public Lobby createLobby()
    {
        Long gameID = 1L;
        while (true)
        {
            //TODO insert check for lobby as well
            Board board = boardRepository.findBoardByGameIDAndTurnID(gameID, 0);
            if (board == null)
            {
                break;
            }
            gameID++;

        }
        Board board = new Board();
        board.setGameID(gameID);
        boardRepository.save(board);
        board.setPhase("LOBBY");
        return joinLobby(gameID);
    }

    /**
     * @param gameID which gameID you want to join.
     * @return gameID for the joined lobby and playerID that you have been assigned
     * @author Elias, Adel, Mustafa and Emil
     */
    @RequestMapping(value = "lobby/join")
    public Lobby joinLobby(Long gameID)
    {
        Long lobbySize = lobbyRepository.countLobbyObjectsByGameID(gameID);
        if (lobbySize >= 6)
        {
            return null;
        }
        Lobby lobby = new Lobby();

        Player player = new Player();
        player.setGameID(gameID);
        // Retrieve all players in the lobby
        List<Player> playersInLobby = playerRepository.findPlayersByGameIDAndTurnID(gameID, 0);


        Long maxPlayerID = playersInLobby.stream().map(Player::getPlayerID).max(Long::compare).orElse(0L);

        player.setPlayerID(maxPlayerID + 1);
        player.setTurnID(0);
        player.setY(0);
        player.setX(0);
        player.setHeading("SOUTH");
        playerRepository.save(player);

        lobby.setGameID(gameID);
        lobby.setPlayerID(player.getPlayerID());
        lobby = lobbyRepository.save(lobby);
        boardRepository.findBoardByGameIDAndTurnID(gameID, 0).setPhase("LOBBY");
        return lobby;
    }

    /**
     * @param gameID,boardName boardName is the name of the json file you want to switch to for the given gameID
     * @author Elias and Bastrup
     */
    @RequestMapping(value = "lobby/changeBoard")
    public boolean changeMap(Long gameID, String boardName)
    {
        Board board = boardRepository.findBoardByGameIDAndTurnID(gameID, 0);
        boardRepository.delete(board);
        board.setBoardname(boardName);
        boardRepository.save(board);
        return true;
    }

    /**
     * @param gameID for the game you want to start
     * @return boolean whether the game was created or not
     * @author Elias, Adel, Frederik and Emil
     */
    @RequestMapping(value = "lobby/startGame")
    public boolean startGame(Long gameID)
    {
        CompleteGame newGame = new CompleteGame();
        newGame.setUpgradeCards(new ArrayList<>());
        newGame.setTurnID(0);
        Board board = boardRepository.findBoardByGameIDAndTurnID(gameID, 0);
        boardRepository.delete(board);
        board.setPhase("PROGRAMMING");
        board.setTurnID(0);
        if (board.getBoardname() == null)
        {
            board.setBoardname("dizzyHighway");
        }
        boardRepository.save(board);

        newGame.setBoard(board);
        newGame.setGameID(gameID);
        System.out.println(gameID);
        newGame.setPlayerList(playerRepository.findPlayersByGameIDAndTurnID(gameID, 0));
        for (Player player : newGame.getPlayerList())
        {
            player.setName("Player" + player.getPlayerID());
            player.setHeading("SOUTH");
        }
        newGame.setCards(new ArrayList<Card>());
        newGame.setEnergyCubes(new ArrayList<>());
        dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board gameBoard =
                dk.dtu.compute.se.pisd.RoboSpring.Util.fromServerBoardToGameBoard(newGame);
        gameBoard.createUpgradeCards();
        List<Player> players = playerRepository.findPlayersByGameIDAndTurnID(gameID, 0);
        playerRepository.deleteAll(players);
        for (int i = 0; i < gameBoard.getPlayersNumber(); i++)
        {
            Space space = gameBoard.getAvailableSpawnPoint();
            players.get(i).setX(space.x);
            players.get(i).setY(space.y);
            gameBoard.getPlayer(i).setSpace(space);
            gameBoard.getPlayer(i).activeCardsPile.initializeAPlayerDeck();
            gameBoard.getPlayer(i).activeCardsPile.shuffleDeck();
            gameBoard.getPlayer(i).drawCards();
        }
        playerRepository.saveAll(players);
        newGame = fromGameBoardToServerBoard(gameBoard);
        BoardSaveLoad boardSaveLoad = new BoardSaveLoad(boardRepository, energyRepository, playerRepository,
                cardsRepository, upgradeCardRepository);
        newGame.setTurnID(0);
        boardSaveLoad.saveBoard(newGame);
        List<Lobby> lobbies = lobbyRepository.findLobbiesByGameID(gameID);
        lobbyRepository.deleteAll(lobbies);
        return true;
    }

    /**
     * @return returns a list of all lobbies
     * @author Elias
     */
    @RequestMapping(value = "lobby/getAll")
    public List<Long> getLobbies()
    {
        List<Long> lobbyToReturn = new ArrayList<>();
        List<Lobby> lobbyToTakeFrom = lobbyRepository.findAll();
        for (Lobby lobby : lobbyToTakeFrom)
        {
            if (!lobbyToReturn.contains(lobby.getGameID()))
            {
                lobbyToReturn.add(lobby.getGameID());
            }
        }
        return lobbyToReturn;
    }

    /**
     * @param gameID to check size for
     * @return amount of players in lobby.
     * @author Mustafa
     */
    @RequestMapping(value = "lobby/size")
    public Long getLobbySize(Long gameID)
    {
        return lobbyRepository.countLobbyObjectsByGameID(gameID);
    }

    /**
     * @param gameID,playerID to delete
     * @return boolean specifying whether the lobby was deleted.
     * @author Adel
     */
    @RequestMapping(value = "lobby/leave")
    public boolean leaveLobby(Long gameID, Long playerID)
    {
        List<Lobby> lobbies = lobbyRepository.findLobbiesByGameID(gameID);
        for (Lobby lobby : lobbies)
        {
            if (lobby.getPlayerID().equals(playerID))
            {
                lobbyRepository.delete(lobby);
                break;
            }
        }
        return true;
    }

}
