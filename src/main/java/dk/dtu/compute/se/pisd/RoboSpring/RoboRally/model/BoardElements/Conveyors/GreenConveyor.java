package dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.BoardElements.Conveyors;

import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Heading;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Space;

/**
 * @author Elias & Adel
 */
public class GreenConveyor extends Conveyor
{
    public GreenConveyor(Heading orientation, Space space)
    {
        super(orientation, 1, space);
        space.board.addBoardElement(Board.GREEN_CONVEYOR_INDEX, this);
    }
}
