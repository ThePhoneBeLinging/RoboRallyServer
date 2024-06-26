package dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.BoardElements;

import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Space;


/**
 * @Author Emil
 */
public class Pit extends NullBoardElement
{
    public Pit(Space space)
    {
        super(space);
        space.board.addBoardElement(Board.NOT_ACTIVATE_ABLE_INDEX, this);
    }

    @Override
    public void activate()
    {
        Player player = this.getSpace().getPlayer();
        if (player != null)
        {
            player.die();
        }
    }


    @Override
    public void onWalkOver(Player player)
    {
        if (player == null)
        {
            return;
        }
        if (!player.checkIfOwnsUpgradeCard("HOVER UNIT"))
        {
            player.die();
        }
    }
}
