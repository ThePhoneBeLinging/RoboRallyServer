package dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.BoardElements.Conveyors;

import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.BoardElements.NullBoardElement;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Heading;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Space;

/**
 * @author Elias
 */
public abstract class Conveyor extends NullBoardElement
{
    private final int moveAmount;

    Conveyor(Heading heading, int moveAmount, Space space)
    {
        super(heading, space);
        this.moveAmount = moveAmount;
    }

    /**
     * @author Elias
     */
    @Override
    public void activate()
    {
        Player playerToMove = this.getSpace().getPlayer();
        if (playerToMove != null && !playerToMove.getMovedByConveyorThisTurn())
        {
            playerToMove.moveController.movePlayerAmountOfTimesWithHeading(playerToMove, this.getHeading(), moveAmount);
            playerToMove.setMovedByConveyorThisTurn(true);
        }
    }
}
