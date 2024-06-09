package dk.dtu.compute.se.pisd.RoboSpring.Repository;

import dk.dtu.compute.se.pisd.RoboSpring.Model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    public Board findBoardById(Long id);
}
