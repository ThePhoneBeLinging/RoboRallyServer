package dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.BoardElements;

import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Space;

/**
 * This class represents an energy cube on the board.
 * When a player lands on a space with an energy cube, the player picks up the energy cube.
 * The energy cube should then be removed from the space.
 *
 * @author Emil
 */

public class EnergyCube extends NullBoardElement
{
    public EnergyCube(Space space)
    {
        super(space);
        space.board.addBoardElement(Board.ENERGY_SPACE_INDEX, this);
    }

    /**
     * @author Elias & Emil
     */
    @Override
    public void activate()
    {
        Player playerOnSquare = this.getSpace().getPlayer();
        if (playerOnSquare != null)
        {
            this.getSpace().getPlayer().pickUpEnergyCube();
            this.getSpace().setBoardElement(new NullBoardElement(this.getSpace()));
        }

    }
}
