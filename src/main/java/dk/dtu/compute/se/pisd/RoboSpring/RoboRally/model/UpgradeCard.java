package dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model;

public class UpgradeCard
{

    private final String name;
    private final int price;
    private Long playerID;

    /**
     * @param name
     * @param price
     * @author Mads
     */
    public UpgradeCard(String name, int price)
    {
        this.name = name;
        this.price = price;
    }

    /**
     * @return
     * @author Mads
     */
    public String getName()
    {
        return this.name;
    }


    /**
     * @return
     * @author Mads
     */
    public int getPrice()
    {
        return this.price;
    }

    public Long getPlayerID()
    {
        return playerID;
    }

    public void setPlayerID(Long playerID)
    {
        this.playerID = playerID;
    }
}
