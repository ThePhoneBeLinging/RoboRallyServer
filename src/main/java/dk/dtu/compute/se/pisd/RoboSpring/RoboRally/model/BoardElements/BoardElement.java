package dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.BoardElements;

import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.fileaccess.model.ElementsEnum;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Heading;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Space;

/**
 * @author Elias & Frederik
 */
public abstract class BoardElement
{
    private final boolean isWalkable;
    private final Heading heading;
    private final Space space;
    private ElementsEnum type;


    protected BoardElement(Heading heading, boolean isWalkable, Space space)
    {
        this.isWalkable = isWalkable;
        this.heading = heading;
        this.space = space;
        this.space.setBoardElement(this);
    }

    /**
     * @param heading
     * @return
     * @author Elias
     */
    public boolean getCanWalkOutOf(Heading heading)
    {
        return this.isWalkable && this.heading != heading;
    }

    /**
     * @param heading
     * @return
     * @author Elias & Adel
     */
    public boolean getCanWalkInto(Heading heading)
    {
        Heading headingToCheck = heading.next().next();
        return this.isWalkable && this.heading != headingToCheck;
    }

    /**
     * @return
     * @author Elias
     */
    public Space getSpace()
    {
        return this.space;
    }

    /**
     * @author Elias
     */
    public void activate()
    {
        //
    }

    /**
     * @param player
     * @author Elias
     */
    public void onWalkOver(Player player)
    {

    }

    /**
     * @return
     * @author Frederik
     */
    public boolean getIsWalkable()
    {
        return this.isWalkable;
    }

    /**
     * @return
     * @author Frederik
     */
    public Heading getHeading()
    {
        return this.heading;
    }


}
