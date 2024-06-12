package dk.dtu.compute.se.pisd.RoboSpring;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.CompleteGame;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Card;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.controller.GameController;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Command;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Heading;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Phase;

public class Util
{

    public static Board fromServerBoardToGameBoard(CompleteGame serverBoard)
    {
        Board gameBoard = LoadBoard.loadBoard(serverBoard.getBoard().getBoardname());
        GameController gameController = new GameController(gameBoard);
        gameBoard.setStep(serverBoard.getBoard().getStep());
        gameBoard.setPhase(Phase.valueOf(serverBoard.getBoard().getPhase()));

        for (Player player : serverBoard.getPlayerList())
        {
            dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player gameBoardPlayer =
                    new dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player(gameBoard, player.getName(),
                            gameController.moveController);
            gameBoard.addPlayer(gameBoardPlayer);
            gameBoardPlayer.setPlayerID(player.getPlayerID());
            gameBoardPlayer.setSpace(gameBoard.getSpace(player.getX(), player.getY()));
            gameBoardPlayer.setLastVisitedCheckPoint(player.getLastVisitedCheckpoint());
            gameBoardPlayer.setHeading(Heading.valueOf(player.getHeading()));
            gameBoardPlayer.setMovedByConveyorThisTurn(player.isMovedByConveyorThisTurn());
            gameBoardPlayer.setEnergyCubes(player.getEnergyCubes());
            gameBoardPlayer.setThisPlayerTurn(player.isPlayersTurn());
        }
        for (Card card : serverBoard.getCards())
        {
            dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Card cardToAdd =
                    new dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Card(Command.valueOf(card.getCommand()));
            for (int i = 0; i < gameBoard.getPlayersNumber(); i++)
            {
                dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player gamePlayer = gameBoard.getPlayer(i);
                if (gamePlayer.getPlayerID() == card.getPlayerID())
                {
                    switch (card.getLocation())
                    {
                        case "REGISTER":
                            int k = 0;
                            while (gamePlayer.getProgramField(k) == null)
                            {
                                k++;
                            }
                            gamePlayer.getProgramField(k).setCard(cardToAdd);
                            break;
                        case "HAND":
                            int j = 0;
                            while (gamePlayer.getCardField(j) == null)
                            {
                                j++;
                            }
                            gamePlayer.getCardField(j).setCard(cardToAdd);
                            break;
                        case "ACTIVE":
                            gamePlayer.activeCardsPile.playerCards.add(cardToAdd);
                        case "DISCARD":
                            gamePlayer.discardedCardsPile.playerCards.add(cardToAdd);
                    }
                }
            }
        }
        return gameBoard;
    }

    public static CompleteGame fromGameBoardToServerBoard(Board gameBoard)
    {
        CompleteGame completeServerBoard = new CompleteGame();
        dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board serverBoard =
                new dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board();
        serverBoard.setStep(gameBoard.getStep());
        serverBoard.setPhase(gameBoard.getPhase().toString());
        serverBoard.setBoardname(gameBoard.boardName);
        completeServerBoard.setBoard(serverBoard);

        int i = 0;
        while (gameBoard.getPlayer(i) != null)
        {
            dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player gameBoardPlayer = gameBoard.getPlayer(i);
            Player serverPlayer = new Player();
            serverPlayer.setName(gameBoardPlayer.getName());
            serverPlayer.setGameID(gameBoardPlayer.getPlayerID());
            serverPlayer.setX(gameBoardPlayer.getSpace().x);
            serverPlayer.setY(gameBoardPlayer.getSpace().y);
            serverPlayer.setLastVisitedCheckpoint(gameBoardPlayer.getLastVisitedCheckPoint());
            serverPlayer.setHeading(gameBoardPlayer.getHeading().toString());
            serverPlayer.setMovedByConveyorThisTurn(gameBoardPlayer.getMovedByConveyorThisTurn());
            serverPlayer.setEnergyCubes(gameBoardPlayer.getEnergyCubes());
            serverPlayer.setPlayersTurn(gameBoardPlayer.isThisPlayerTurn());
            completeServerBoard.getPlayerList().add(serverPlayer);
        }
        return completeServerBoard;
    }
}
