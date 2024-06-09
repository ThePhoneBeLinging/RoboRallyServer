package dk.dtu.compute.se.pisd.RoboSpring.Controller;
import dk.dtu.compute.se.pisd.RoboSpring.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.Model.CompleteBoard;
import dk.dtu.compute.se.pisd.RoboSpring.Model.EnergyCube;
import dk.dtu.compute.se.pisd.RoboSpring.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.Repository.BoardRepository;
import dk.dtu.compute.se.pisd.RoboSpring.Repository.EnergyRepository;
import dk.dtu.compute.se.pisd.RoboSpring.Repository.PlayerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//Base endpoint
public class BoardSaver
{
    private BoardRepository boardRepository;
    private EnergyRepository energyRepository;
    private PlayerRepository playerRepository;

    public BoardSaver(BoardRepository boardRepository, EnergyRepository energyRepository, PlayerRepository playerRepository) {
        this.boardRepository = boardRepository;
        this.energyRepository = energyRepository;
        this.playerRepository = playerRepository;
    }


    @GetMapping
    @RequestMapping(value = "boards/save")
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
        return completeBoard;
    }



}
