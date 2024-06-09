package dk.dtu.compute.se.pisd.RoboSpring.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.Model.EnergyCube;
import dk.dtu.compute.se.pisd.RoboSpring.Repository.BoardRepository;
import dk.dtu.compute.se.pisd.RoboSpring.Repository.EnergyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//Base endpoint
@RequestMapping("/boards/cubes")
public class EnergyCubeController
{
    private EnergyRepository energyRepository;

    public EnergyCubeController(EnergyRepository energyRepository) {
        this.energyRepository = energyRepository;
    }

    @GetMapping
    //Specific endpoint for the method
    @RequestMapping(value = "")
    public ResponseEntity<List<EnergyCube>> getEnergyCubes(){
        List<EnergyCube> energyCubeList = energyRepository.findEnergyCubesByBoardID(2L);
        return ResponseEntity.ok(energyCubeList);
    }
}
