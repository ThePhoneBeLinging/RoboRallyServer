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
package dk.dtu.compute.se.pisd.RoboSpring.RoboRally.view;

import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.controller.GameController;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Phase;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Space;
import dk.dtu.compute.se.pisd.RoboSpring.observer.Subject;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class BoardView extends VBox implements ViewObserver
{

    private final Board board;

    private final GridPane mainBoardPane;
    private final SpaceView[][] spaces;

    private final PlayersView playersView;

    private final Label statusLabel;


    /**
     * @param gameController
     * @author Elias
     */
    public BoardView(@NotNull GameController gameController)
    {
        board = gameController.board;

        mainBoardPane = new GridPane();
        playersView = new PlayersView(gameController);
        statusLabel = new Label("<no status>");

        this.getChildren().add(mainBoardPane);
        this.getChildren().add(playersView);
        this.getChildren().add(statusLabel);

        spaces = new SpaceView[board.width][board.height];
        for (int x = 0; x < board.width; x++)
        {
            for (int y = 0; y < board.height; y++)
            {
                Space space = board.getSpace(x, y);
                SpaceView spaceView = new SpaceView(space);
                spaces[x][y] = spaceView;
                mainBoardPane.add(spaceView, x, y);
            }
        }

        board.attach(this);
        update(board);
    }

    /**
     * @param subject
     * @author Elias
     */
    @Override
    public void updateView(Subject subject)
    {
        if (subject == board)
        {
            Phase phase = board.getPhase();
            statusLabel.setText(board.getStatusMessage());
        }
    }

    // XXX this handler and its uses should eventually be deleted! This is just to help test the
    //     behaviour of the game by being able to explicitly move the players on the board!

    /**
     * @author Frederik
     */
    // one line is currently commented out as our moveCurrentPlayerToSpace is in MoveController
    private class SpaceEventHandler implements EventHandler<MouseEvent>
    {

        final public GameController gameController;

        /**
         * @param gameController
         * @author
         */
        public SpaceEventHandler(@NotNull GameController gameController)
        {
            this.gameController = gameController;
        }

        /**
         * @param event
         * @author Frederik
         */
        @Override
        public void handle(MouseEvent event)
        {
            Object source = event.getSource();
            if (source instanceof SpaceView spaceView)
            {
                Space space = spaceView.space;
                Board board = space.board;

                if (board == gameController.board)
                {
                    // gameController.moveCurrentPlayerToSpace(space);
                    event.consume();
                }
            }
        }

    }

}
