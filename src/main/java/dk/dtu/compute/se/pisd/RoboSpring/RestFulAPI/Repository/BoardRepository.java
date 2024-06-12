package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>
{

    Board findBoardByGameID(Long id);
}
