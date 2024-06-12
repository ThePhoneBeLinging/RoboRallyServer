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
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.*;
import dk.dtu.compute.se.pisd.RoboSpring.observer.Subject;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class PlayerView extends Tab implements ViewObserver
{

    private final Player player;

    private final VBox top;
    private final HBox horizontal;
    private final VBox rightPanel;
    private final Label upgradeCardsLabel;
    private final Label energyCubesLabel;

    private final Label programLabel;
    private final GridPane programPane;
    private final Label cardsLabel;
    private final GridPane cardsPane;

    private final CardFieldView[] programCardViews;
    private final CardFieldView[] cardViews;

    private final VBox buttonPanel;

    private final Button finishButton;
    private final Button executeButton;
    private final Button stepButton;
    private final Button shopButton;

    private final VBox playerInteractionPanel;

    private final GameController gameController;

    /**
     * @param gameController
     * @param player
     * @author Elias, Frederik, Emil, Adel & Mads
     */
    public PlayerView(@NotNull GameController gameController, @NotNull Player player)
    {
        super(player.getName());
        this.setStyle("-fx-text-base-color: " + player.getColor() + ";");

        top = new VBox();
        top.setSpacing(3.0);

        horizontal = new HBox();

        rightPanel = new VBox();
        rightPanel.setAlignment(Pos.TOP_LEFT);
        rightPanel.setSpacing(3.0);

        upgradeCardsLabel = new Label("Active Upgrade Cards");

        energyCubesLabel = new Label("Energy Cubes: " + player.getEnergyCubes());
        rightPanel.getChildren().addAll(energyCubesLabel, upgradeCardsLabel);
        horizontal.getChildren().addAll(top, rightPanel);

        this.setContent(horizontal);

        this.gameController = gameController;
        this.player = player;

        programLabel = new Label("Program");

        programPane = new GridPane();
        programPane.setVgap(2.0);
        programPane.setHgap(2.0);
        programCardViews = new CardFieldView[Player.NO_REGISTERS];
        for (int i = 0; i < Player.NO_REGISTERS; i++)
        {
            CardField cardField = player.getProgramField(i);
            if (cardField != null)
            {
                programCardViews[i] = new CardFieldView(gameController, cardField);
                programPane.add(programCardViews[i], i, 0);
            }
        }

        // XXX  the following buttons should actually not be on the tabs of the individual
        //      players, but on the PlayersView (view for all players). This should be
        //      refactored.

        finishButton = new Button("Finish Programming");
        finishButton.setOnAction(e -> gameController.finishProgrammingPhase());

        executeButton = new Button("Execute Program");
        executeButton.setOnAction(e -> gameController.executePrograms());

        stepButton = new Button("Execute Current Register");
        stepButton.setOnAction(e -> gameController.executeStep());

        shopButton = new Button("Upgrade Shop");
        shopButton.setOnAction(e -> gameController.openShop());

        buttonPanel = new VBox(finishButton, executeButton, stepButton, shopButton);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setSpacing(3.0);
        // programPane.add(buttonPanel, Player.NO_REGISTERS, 0); done in update now

        playerInteractionPanel = new VBox();
        playerInteractionPanel.setAlignment(Pos.CENTER_LEFT);
        playerInteractionPanel.setSpacing(3.0);

        cardsLabel = new Label("Command Cards");
        cardsPane = new GridPane();
        cardsPane.setVgap(2.0);
        cardsPane.setHgap(2.0);
        cardViews = new CardFieldView[Player.NO_CARDS];
        for (int i = 0; i < Player.NO_CARDS; i++)
        {
            CardField cardField = player.getCardField(i);
            if (cardField != null)
            {
                cardViews[i] = new CardFieldView(gameController, cardField);
                cardsPane.add(cardViews[i], i, 0);
            }
        }

        top.getChildren().add(programLabel);
        top.getChildren().add(programPane);
        top.getChildren().add(cardsLabel);
        top.getChildren().add(cardsPane);

        if (player.board != null)
        {
            player.board.attach(this);
            update(player.board);
        }
    }

    /**
     * @param subject
     * @author Elias, Mads & Emil
     */
    @Override
    public void updateView(Subject subject)
    {
        if (subject == player.board)
        {
            for (int i = 0; i < Player.NO_REGISTERS; i++)
            {
                CardFieldView cardFieldView = programCardViews[i];
                if (cardFieldView != null)
                {
                    if (player.board.getPhase() == Phase.PROGRAMMING)
                    {
                        cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                        cardFieldView.setBorder(CardFieldView.BORDER_DEFAULT);
                    }
                    else
                    {
                        if (i < player.board.getStep())
                        {
                            cardFieldView.setBackground(CardFieldView.BG_DONE);
                            cardFieldView.setBorder(CardFieldView.BORDER_DONE);
                        }
                        else if (i == player.board.getStep())
                        {
                            if (player.board.getCurrentPlayer() == player)
                            {
                                cardFieldView.setBackground(CardFieldView.BG_ACTIVE);
                                cardFieldView.setBorder(CardFieldView.BORDER_ACTIVE);
                            }
                            else if (player.board.getPlayerNumber(player.board.getCurrentPlayer()) > player.board.getPlayerNumber(player))
                            {
                                cardFieldView.setBackground(CardFieldView.BG_DONE);
                                cardFieldView.setBorder(CardFieldView.BORDER_DONE);
                            }
                            else
                            {
                                cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                                cardFieldView.setBorder(CardFieldView.BORDER_DEFAULT);
                            }
                        }
                        else
                        {
                            cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                        }
                    }
                }
            }

            updateUpgradeCardsLabel();
            updateEnergyCubesLabel();

            if (player.board.getPhase() != Phase.PLAYER_INTERACTION)
            {
                if (!programPane.getChildren().contains(buttonPanel))
                {
                    programPane.getChildren().remove(playerInteractionPanel);
                    programPane.add(buttonPanel, Player.NO_REGISTERS, 0);
                }
                switch (player.board.getPhase())
                {
                    case INITIALISATION:
                        finishButton.setDisable(true);
                        // XXX just to make sure that there is a way for the player to get
                        //     from the initialization phase to the programming phase somehow!
                        executeButton.setDisable(false);
                        stepButton.setDisable(true);
                        break;

                    case PROGRAMMING:
                        finishButton.setDisable(false);
                        executeButton.setDisable(true);
                        stepButton.setDisable(true);
                        shopButton.setDisable(false);

                        break;

                    case ACTIVATION:
                        finishButton.setDisable(true);
                        executeButton.setDisable(false);
                        stepButton.setDisable(false);
                        shopButton.setDisable(true);
                        break;

                    default:
                        finishButton.setDisable(true);
                        executeButton.setDisable(true);
                        stepButton.setDisable(true);
                        shopButton.setDisable(true);
                }


            }
            else
            {
                if (!programPane.getChildren().contains(playerInteractionPanel))
                {
                    programPane.getChildren().remove(buttonPanel);
                    programPane.add(playerInteractionPanel, Player.NO_REGISTERS, 0);
                }
                playerInteractionPanel.getChildren().clear();

                if (player.board.getCurrentPlayer() == player)
                {
                    if (player.getProgramField(player.board.getStep()).getCard().command.isInteractive())
                    {
                        for (int i = 0; i < player.getProgramField(player.board.getStep()).getCard().command.getOptions().size(); i++)
                        {
                            Command oneCommandToChoseBetween =
                                    player.getProgramField(player.board.getStep()).getCard().command.getOptions().get(i);
                            Button optionButton = new Button(oneCommandToChoseBetween.displayName);
                            optionButton.setOnAction(e -> gameController.executeCommandOptionAndContinue(oneCommandToChoseBetween));
                            playerInteractionPanel.getChildren().add(optionButton);
                            optionButton.setDisable(false);
                        }

                    }

                }
            }
        }
    }

    private void updateUpgradeCardsLabel()
    {
        StringBuilder upgrades = new StringBuilder("Active Upgrade Cards:\n");

        for (UpgradeCard upgrade : player.getUpgradeCards())
        {
            upgrades.append(upgrade.getName()).append("\n");
        }
        upgradeCardsLabel.setText(upgrades.toString());
    }

    private void updateEnergyCubesLabel()
    {
        energyCubesLabel.setText("Energy Cubes: " + player.getEnergyCubes());
    }
}
