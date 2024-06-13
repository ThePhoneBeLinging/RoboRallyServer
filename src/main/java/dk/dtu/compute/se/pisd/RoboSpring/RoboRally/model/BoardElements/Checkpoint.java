package dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.BoardElements;

import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Space;

/**
 * @author Elias & Adel
 */
public class Checkpoint extends NullBoardElement
{
    public Checkpoint(Space space)
    {
        super(space);
        space.board.addBoardElement(Board.CHECKPOINTS_INDEX, this);
        int index = space.board.getIndexOfCheckPoint(this);
    }

    /**
     * @author Elias
     */
    @Override
    public void activate()
    {

        // Method should already check if checkpoint is visited in the correct order.
        Player playerOnBoardElement = this.getSpace().getPlayer();
        if (playerOnBoardElement != null)
        {
            playerOnBoardElement.addCheckPointAsVisited(this);
        }
    }
}
