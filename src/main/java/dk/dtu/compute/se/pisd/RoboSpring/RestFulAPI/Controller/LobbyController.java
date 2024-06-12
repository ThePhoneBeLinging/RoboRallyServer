package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board;
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

    LobbyController(LobbyRepository lobbyRepository, BoardRepository boardRepository, PlayerRepository playerRepository)
    {
        this.lobbyRepository = lobbyRepository;
        this.boardRepository = boardRepository;
        this.playerRepository = playerRepository;
    }

    @RequestMapping(value = "lobby/create")
    public Lobby createLobby()
    {
        Long gameID = 1L;
        while (true)
        {
            //TODO insert check for lobby as well
            Board board = boardRepository.findBoardByGameID(gameID);
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
        Board board = boardRepository.findBoardByGameID(gameID);
        boardRepository.delete(board);
        board.setBoardname(boardName);
        boardRepository.save(board);
        return true;
    }

    @RequestMapping(value = "lobby/startGame")
    public boolean startGame(Long gameID)
    {
        Board board = boardRepository.findBoardByGameID(gameID);
        boardRepository.delete(board);
        board.setPhase("PROGRAMMING");
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
            playerRepository.save(player);
            lobbyRepository.deleteAll(lobbyRepository.findLobbiesByGameID(gameID));
        }
        return true;
    }

    @RequestMapping(value = "lobby/delete")
    public boolean deleteLobby(Long gameID)
    {
        boardRepository.deleteById(gameID);
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
}