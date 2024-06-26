package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.EnergyCube;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.EnergyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//Base endpoint
@RequestMapping("get/boards/cubes")
public class EnergyCubeController
{
    private final EnergyRepository energyRepository;

    public EnergyCubeController(EnergyRepository energyRepository)
    {
        this.energyRepository = energyRepository;
    }

    /**
     * @author Elias
     */
    @GetMapping
    //Specific endpoint for the method
    @RequestMapping(value = "")
    public ResponseEntity<List<EnergyCube>> getEnergyCubes()
    {
        List<EnergyCube> energyCubeList = energyRepository.findAll();
        return ResponseEntity.ok(energyCubeList);
    }
}
