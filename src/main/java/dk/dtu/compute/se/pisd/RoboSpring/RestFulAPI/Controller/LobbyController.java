package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.CompleteGame;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Lobby;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.BoardRepository;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.LobbyRepository;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.PlayerRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LobbyController
{
    private final LobbyRepository lobbyRepository;
    private final BoardRepository boardRepository;
    private final PlayerRepository playerRepository;
    private final BoardController boardController;

    LobbyController(LobbyRepository lobbyRepository, BoardRepository boardRepository,
                    PlayerRepository playerRepository, BoardController boardController)
    {
        this.lobbyRepository = lobbyRepository;
        this.boardRepository = boardRepository;
        this.playerRepository = playerRepository;
        this.boardController = boardController;
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
        return joinLobby(gameID);
    }

    @RequestMapping(value = "lobby/join")
    public Lobby joinLobby(Long gameID)
    {
        Lobby lobby = new Lobby();
        lobby.setPlayerID(1L + lobbyRepository.countLobbyObjectsByGameID(gameID));
        lobby.setGameID(gameID);
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
        Board board = boardRepository.findBoardByGameIDAndTurnID(gameID, 0);
        boardRepository.delete(board);
        board.setPhase("PROGRAMMING");
        board.setTurnID(0);
        if (board.getBoardname() == null)
        {
            board.setBoardname("default");
        }
        boardRepository.save(board);
        List<Lobby> lobbies = lobbyRepository.findLobbiesByGameID(gameID);
        for (Lobby lobby : lobbies)
        {
            Player player = new Player();
            player.setGameID(gameID);
            player.setPlayerID(lobby.getPlayerID());
            player.setTurnID(0);
            player.setY(0);
            player.setX(0);
            player.setHeading("SOUTH");
            playerRepository.save(player);
            lobbyRepository.deleteAll(lobbyRepository.findLobbiesByGameID(gameID));
        }
        newGame.setBoard(board);
        newGame.setGameID(gameID);
        newGame.setPlayerList(playerRepository.findPlayersByGameIDAndTurnID(gameID, 0));
        for (Player player : newGame.getPlayerList())
        {
            player.setName("Player" + player.getPlayerID());
            player.setHeading("SOUTH");
        }
        newGame.setCards(new ArrayList<>());
        newGame.setEnergyCubes(new ArrayList<>());
        dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board gameBoard =
                dk.dtu.compute.se.pisd.RoboSpring.Util.fromServerBoardToGameBoard(newGame);

        for (int i = 0; i < gameBoard.getPlayersNumber(); i++)
        {
            gameBoard.getPlayer(i).setSpace(gameBoard.getAvailableSpawnPoint());

        }
        //newGame = fromGameBoardToServerBoard(gameBoard);
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
