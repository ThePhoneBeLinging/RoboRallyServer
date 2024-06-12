package dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.BoardElements;

import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Space;
import javafx.scene.image.Image;

/**
 * @author Frederik
 */
public class Gear extends NullBoardElement
{
    public final boolean isClockwise;

    /**
     * @param space
     * @param isClockwise
     * @author Frederik
     */
    public Gear(Space space, boolean isClockwise)
    {
        super(true, space);
        this.isClockwise = isClockwise;

        space.board.addBoardElement(Board.GEARS_INDEX, this);
    }

    /**
     * @author Frederik
     */
    public void activate()
    {
        if (this.getSpace() == null || this.getSpace().getPlayer() == null)
        {
            return;
        }
        if (isClockwise)
        {
            getSpace().getPlayer().setHeading(getSpace().getPlayer().getHeading().next());
        }
        else
        {
            getSpace().getPlayer().setHeading(getSpace().getPlayer().getHeading().prev());
        }
    }
}
