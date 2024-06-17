package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>
{

    Board findBoardByGameIDAndTurnID(Long id, int turnID);

    List<Board> findBoardByGameID(Long id);

    void deleteAllByGameID(Long id);

    List<Board> findAllByGameID(Long gameID);
}
