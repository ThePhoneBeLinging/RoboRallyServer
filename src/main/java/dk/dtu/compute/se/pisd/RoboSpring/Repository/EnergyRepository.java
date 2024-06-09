package dk.dtu.compute.se.pisd.RoboSpring.Repository;

import dk.dtu.compute.se.pisd.RoboSpring.Model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.Model.EnergyCube;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnergyRepository extends JpaRepository<EnergyCube, Long> {

    public List<EnergyCube> findEnergyCubesByBoardID(Long boardID);
}
