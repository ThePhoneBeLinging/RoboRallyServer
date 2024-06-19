package dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.BoardElements;

import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Space;

public class Void extends Pit {

    public Void(Space space)
    {
        super(space);
        space.board.addBoardElement(Board.NOT_ACTIVATE_ABLE_INDEX, this);
    }

}