package dk.dtu.compute.se.pisd.RoboSpring.Controller;
import dk.dtu.compute.se.pisd.RoboSpring.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.Model.CompleteBoard;
import dk.dtu.compute.se.pisd.RoboSpring.Model.EnergyCube;
import dk.dtu.compute.se.pisd.RoboSpring.Repository.BoardRepository;
import dk.dtu.compute.se.pisd.RoboSpring.Repository.EnergyRepository;
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

    public BoardSaver(BoardRepository boardRepository, EnergyRepository energyRepository) {
        this.boardRepository = boardRepository;
        this.energyRepository = energyRepository;
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
        return completeBoard;
    }



}
