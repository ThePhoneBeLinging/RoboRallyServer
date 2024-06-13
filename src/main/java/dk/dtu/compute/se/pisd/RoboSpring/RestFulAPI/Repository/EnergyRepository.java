package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.EnergyCube;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnergyRepository extends JpaRepository<EnergyCube, Long>
{

    List<EnergyCube> findEnergyCubesByGameIDAndTurnID(Long gameID, int turnID);
}
