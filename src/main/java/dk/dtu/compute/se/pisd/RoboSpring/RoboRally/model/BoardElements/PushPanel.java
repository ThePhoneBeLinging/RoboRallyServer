package dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.BoardElements;

import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Heading;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Space;
import javafx.scene.image.Image;

/**
 * @author Emil
 */
public class PushPanel extends BoardElement
{
    public PushPanel(Heading heading, Space space)
    {
        super(heading, true, space);
        space.board.addBoardElement(Board.PUSH_PANELS_INDEX, this);
    }

    /**
     * @author Emil
     */
    @Override
    public void activate()
    {
        Player playerToMove = this.getSpace().getPlayer();
        if (playerToMove != null)
        {
            Heading heading = this.getHeading().next().next();
            this.getSpace().getPlayer().moveController.movePlayerAmountOfTimesWithHeading(playerToMove, heading, 1);
        }
    }
}
