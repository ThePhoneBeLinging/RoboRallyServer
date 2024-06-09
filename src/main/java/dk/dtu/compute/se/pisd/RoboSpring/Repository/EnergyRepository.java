package dk.dtu.compute.se.pisd.RoboSpring.Repository;

import dk.dtu.compute.se.pisd.RoboSpring.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.Model.EnergyCube;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnergyRepository extends JpaRepository<EnergyCube, Long> {

    public EnergyCube findEnergyCubesByBoard(Board board);
}
