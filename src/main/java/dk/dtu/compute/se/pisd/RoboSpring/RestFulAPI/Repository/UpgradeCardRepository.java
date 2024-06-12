package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.UpgradeCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UpgradeCardRepository extends JpaRepository<UpgradeCard, Long> {

    List<UpgradeCard> findByPlayerIDIsNullAndGameID(Long gameID);

    List<UpgradeCard> findActiveUpgradeCardsByPlayerIDAndGameID(Long playerID, Long gameID);

}
