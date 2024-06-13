/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model;

import dk.dtu.compute.se.pisd.RoboSpring.observer.Subject;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class CardField extends Subject
{

    final public Player player;

    private Card card;

    private boolean visible;

    /**
     * @param player the player to which this field belongs
     * @author Elias & Frederik
     */
    public CardField(Player player)
    {
        this.player = player;
        this.card = null;
        this.visible = true;
    }

    /**
     * @return the card in this field
     * @author Frederik
     */
    public Card getProgrammingCard()
    {
        return card;
    }

    /**
     * @param card the card to be placed in this field
     * @author Elias
     */
    public void setCard(Card card)
    {
        if (card != this.card)
        {
            this.card = card;
            notifyChange();
        }
    }

    /**
     * @return whether the card in this field is visible
     * @author Elias
     */
    public boolean isVisible()
    {
        return visible;
    }

    /**
     * @param visible whether the card in this field should be visible
     * @author Elias
     */
    public void setVisible(boolean visible)
    {
        if (visible != this.visible)
        {
            this.visible = visible;
            notifyChange();
        }
    }
}
