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
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Card extends Subject
{

    public Command command;

    /**
     * @param command the command represented by this card
     * @author Elias & Mustafa
     */
    public Card(@NotNull Command command)
    {

        this.command = command;
    }

    /**
     * @param command
     * @return
     * @author Elias, Mustafa & Frederik
     */


    /**
     * @param command
     * @param image
     * @author Mustafa
     */
    public Card(@NotNull Command command, Image image)
    {
        this.command = command;
    }

    /**
     * @return the name of the command represented by this card
     * @author Elias
     */
    public String getName()
    {
        return command.displayName;
    }

    /**
     * @return
     * @author Elias
     */

}
