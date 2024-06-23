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
package dk.dtu.compute.se.pisd.RoboSpring.RoboRally.controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller.BoardSaveLoad;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.*;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.*;
import dk.dtu.compute.se.pisd.RoboSpring.Util;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static dk.dtu.compute.se.pisd.RoboSpring.Util.fromGameBoardToServerBoard;

/**
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController
{
    final public Board board;
    final public MoveController moveController;
    final public BoardRepository boardRepository;
    final public EnergyRepository energyRepository;
    final public PlayerRepository playerRepository;
    final public CardsRepository cardsRepository;
    final public UpgradeCardRepository upgradeCardRepository;
    final private BoardSaveLoad boardSaveLoad;


    /**
     * @author Elias
     */
    public GameController(@NotNull Board board)
    {
        this(board, null, null, null, null, null);
    }

    public GameController(@NotNull Board board, BoardRepository boardRepository, EnergyRepository energyRepository,
                          PlayerRepository playerRepository, CardsRepository cardsRepository,
                          UpgradeCardRepository upgradeCardRepository)
    {
        this.board = board;
        this.moveController = new MoveController(this);
        this.boardRepository = boardRepository;
        this.energyRepository = energyRepository;
        this.playerRepository = playerRepository;
        this.cardsRepository = cardsRepository;
        this.upgradeCardRepository = upgradeCardRepository;
        this.boardSaveLoad = new BoardSaveLoad(boardRepository, energyRepository, playerRepository, cardsRepository,
                upgradeCardRepository);
    }

    /**
     * Opens the upgrade shop for the current player. This method should be called when the player has pressed the
     * upgrade button.
     *
     * @Author Emil
     */
    // XXX: implemented in the current version

    /**
     * Method to finish the programming phase, used after the players have used programming cards.
     *
     * @author Elias
     */
    public void finishProgrammingPhase()
    {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.activateBoardElementsOfIndex(Board.ANTENNA_INDEX);
        board.setStep(0);
    }

    /**
     * Makes program fields invisible, used for the finishProgrammingPhase
     *
     * @author Elias & Frederik
     */
    // XXX: implemented in the current version
    private void makeProgramFieldsInvisible()
    {
        for (int i = 0; i < board.getPlayersNumber(); i++)
        {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++)
            {
                CardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /**
     * Makes program fields visible, used to revert makeProgramFieldsInvisble. Usage under
     * programming and executing next step
     *
     * @param register
     * @author Elias & Frederik
     */
    // XXX: implemented in the current version
    private void makeProgramFieldsVisible(int register)
    {
        if (register >= 0 && register < Player.NO_REGISTERS)
        {
            for (int i = 0; i < board.getPlayersNumber(); i++)
            {
                Player player = board.getPlayer(i);
                CardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    /**
     * Executes the registers of the players. This method should be called when the players have pressed the execute
     * registers button.
     *
     * @author Elias
     */
    // XXX: implemented in the current version
    public void executePrograms()
    {
        board.setStepMode(false);
        continuePrograms();
    }

    /**
     * Continues the execution of the programs of the players. This method should be called when the
     *
     * @author Elias
     */
    // XXX: implemented in the current version
    public void continuePrograms()
    {
        do
        {
            executeNextStep();
        }
        while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    /**
     * Executes the next step in the programming deck. Used for single steps or executing the whole deck
     *
     * @author Elias, Frederik, Emil & Adel
     */
    // XXX: implemented in the current version
    private void executeNextStep()
    {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null)
        {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS)
            {
                Card card = currentPlayer.getProgramField(step).getProgrammingCard();
                if (card != null)
                {
                    if (card.command.isInteractive())
                    {
                        startInteractivePhase(currentPlayer);
                        dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.CompleteGame serverCompleteGame =
                                Util.fromGameBoardToServerBoard(board);
                        boardSaveLoad.saveBoard(serverCompleteGame);
                        return;
                    }
                    Command command = card.command;
                    moveController.executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber())
                {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                }
                else
                {
                    step++;
                    board.activateBoardElements();
                    if (step < Player.NO_REGISTERS)
                    {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    }
                    else
                    {
                        startProgrammingPhase();
                    }
                }

                this.boardSaveLoad.saveBoard(fromGameBoardToServerBoard(board));
                board.setTurnID(board.getTurnID() + 1);
            }
            else
            {
                // this should not happen
                assert false;
            }
        }
        else
        {
            // this should not happen
            assert false;
        }
    }

    public void startInteractivePhase(Player currentPlayer)
    {
        board.setPhase(Phase.PLAYER_INTERACTION);
        board.setCurrentPlayer(currentPlayer);
        board.setOptions(new ArrayList<>());
        for (Command option : currentPlayer.getProgramField(board.getStep()).getProgrammingCard().command.getOptions())
        {
            board.getOptions().add(option.toString());
        }
    }

    /**
     * Starts the programming phase of the game. This method should be called when the game has begun
     *
     * @author Elias, Adel & Frederik
     */
    // XXX: implemented in the current version
    public void startProgrammingPhase()
    {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++)
        {
            Player player = board.getPlayer(i);
            if (player != null)
            {
                for (int j = 0; j < Player.NO_REGISTERS; j++)
                {
                    CardField field = player.getProgramField(j);
                    player.addCardToDiscardPile(field.getProgrammingCard());
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++)
                {
                    CardField field = player.getCardField(j);
                    if (player.getCardField(j).getProgrammingCard() != null)
                    {
                        player.addDamageCardToPile(player.getCardField(j).getProgrammingCard().command, 1);
                    }
                    field.setCard(player.drawTopCard());
                    field.setVisible(true);
                }
            }
        }
        board.setTurnID(0);
    }

    /**
     * Executes the command option and continues the program. This method should be called when the player has chosen
     * an option for an interactive command.
     *
     * @param commandOption the command option to be executed
     * @author Emil
     */

    public void executeCommandOptionAndContinue(Command commandOption)
    {
        Player currentPlayer = board.getCurrentPlayer();
        moveController.executeCommand(currentPlayer, commandOption);
        int step = board.getStep();
        board.setPhase(Phase.ACTIVATION);
        int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
        if (nextPlayerNumber < board.getPlayersNumber())
        {
            board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
        }
        else
        {
            step++;
            board.activateBoardElements();
            if (step < Player.NO_REGISTERS)
            {
                makeProgramFieldsVisible(step);
                board.setStep(step);
                board.setCurrentPlayer(board.getPlayer(0));
            }
            else
            {
                startProgrammingPhase();
            }
        }

        this.boardSaveLoad.saveBoard(fromGameBoardToServerBoard(board));
        board.setTurnID(board.getTurnID() + 1);
        continuePrograms();
    }

    /**
     * @return new Card with random commands
     * @author Elias & Frederik
     */
    // XXX: implemented in the current version
    private Card generateRandomCommandCard()
    {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new Card(commands[random]);
    }

    /**
     * Executes next step
     *
     * @author Elias
     */
    // XXX: implemented in the current version
    public void executeStep()
    {
        board.setStepMode(true);
        continuePrograms();
    }

    /**
     * @param source
     * @param target
     * @return true if sourceCard is not null and targetCard is null, false otherwise
     * @author Frederik & Elias
     */
    public boolean moveCards(@NotNull CardField source, @NotNull CardField target)
    {
        Card sourceCard = source.getProgrammingCard();
        Card targetCard = target.getProgrammingCard();
        if (sourceCard != null && targetCard == null)
        {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented()
    {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

}
