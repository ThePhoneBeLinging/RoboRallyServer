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
                    EnergyRepository energyRepository, EnergyRepository energyRepository1,
                    CardsRepository cardsRepository, UpgradeCardRepository upgradeCardRepository)
    {
        this.lobbyRepository = lobbyRepository;
        this.boardRepository = boardRepository;
        this.playerRepository = playerRepository;
        this.boardController = boardController;
        this.energyRepository = energyRepository1;
        this.cardsRepository = cardsRepository;
        this.upgradeCardRepository = upgradeCardRepository;
    }

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

    @RequestMapping(value = "lobby/join")
    public Lobby joinLobby(Long gameID)
    {
        Lobby lobby = new Lobby();

        Player player = new Player();
        player.setGameID(gameID);
        player.setPlayerID(1L + lobbyRepository.countLobbyObjectsByGameID(gameID));

        player.setTurnID(0);
        player.setY(0);
        player.setX(0);
        player.setHeading("SOUTH");
        playerRepository.save(player);

        lobby.setGameID(gameID);
        lobby.setPlayerID(player.getPlayerID());
        lobby = lobbyRepository.save(lobby);
        return lobby;
    }

    @RequestMapping(value = "lobby/changeBoard")
    public boolean changeMap(Long gameID, String boardName)
    {
        Board board = boardRepository.findBoardByGameIDAndTurnID(gameID, 0);
        boardRepository.delete(board);
        board.setBoardname(boardName);
        boardRepository.save(board);
        return true;
    }

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
        List<Player> players = playerRepository.findPlayersByGameIDAndTurnID(gameID, 0);
        playerRepository.deleteAll(players);
        for (int i = 0; i < gameBoard.getPlayersNumber(); i++)
        {
            Space space = gameBoard.getAvailableSpawnPoint();
            players.get(i).setX(space.x);
            players.get(i).setY(space.y);
            gameBoard.getPlayer(i).setSpace(space);
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

    @RequestMapping(value = "lobby/delete")
    public boolean deleteLobby(Long gameID)
    {
        for (int i = 0; i < 31; i++)
        {
            boardController.deleteBoard(gameID, i);
        }
        lobbyRepository.deleteAll(lobbyRepository.findLobbiesByGameID(gameID));
        return true;
    }

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

    @RequestMapping(value = "lobby/getPlayers")
    public List<Long> getPlayers(Long gameID)
    {
        List<Long> players = new ArrayList<>();
        List<Lobby> lobbyToTakeFrom = lobbyRepository.findLobbiesByGameID(gameID);
        for (Lobby lobby : lobbyToTakeFrom)
        {
            players.add(lobby.getPlayerID());
        }
        return players;
    }
}
