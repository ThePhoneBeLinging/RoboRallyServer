package dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.BoardElements.Conveyors;

import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Heading;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Space;

/**
 * @author Adel & Elias
 */
public class BlueConveyor extends Conveyor
{
    public BlueConveyor(Heading heading, Space space)
    {
        super(heading, 2, space);
        space.board.addBoardElement(Board.BLUE_CONVEYOR_INDEX, this);
    }
}
