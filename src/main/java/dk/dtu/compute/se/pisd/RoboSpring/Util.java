package dk.dtu.compute.se.pisd.RoboSpring;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.CompleteGame;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.controller.GameController;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.controller.MoveController;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.*;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Heading;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Phase;

public class Util {

    public static Board fromServerBoardToGameBoard(CompleteGame serverBoard) {
        Board gameBoard = LoadBoard.loadBoard(serverBoard.getBoard().getBoardname());
        GameController gameController = new GameController(gameBoard);
        gameBoard.setStep(serverBoard.getBoard().getStep());
        gameBoard.setPhase(Phase.valueOf(serverBoard.getBoard().getPhase()));

        for (Player player : serverBoard.getPlayerList()) {
            dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player gameBoardPlayer = new dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player(gameBoard, player.getName(), gameController.moveController);
            gameBoard.addPlayer(gameBoardPlayer);
            gameBoardPlayer.setSpace(gameBoard.getSpace(player.getX(), player.getY()));
            gameBoardPlayer.setLastVisitedCheckPoint(player.getLastVisitedCheckpoint());
            gameBoardPlayer.setHeading(Heading.valueOf(player.getHeading()));
            gameBoardPlayer.setMovedByConveyorThisTurn(player.isMovedByConveyorThisTurn());
            gameBoardPlayer.setEnergyCubes(player.getEnergyCubes());
            gameBoardPlayer.setThisPlayerTurn(player.isPlayersTurn());
        }
        return gameBoard;
    }

    public static CompleteGame fromGameBoardToServerBoard(Board gameBoard) {
        CompleteGame completeServerBoard = new CompleteGame();
        dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board serverBoard = new dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Board();
        serverBoard.setStep(gameBoard.getStep());
        serverBoard.setPhase(gameBoard.getPhase().toString());
        serverBoard.setBoardname(gameBoard.boardName);
        completeServerBoard.setBoard(serverBoard);

        int i = 0;
        while (gameBoard.getPlayer(i) != null) {
            dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player gameBoardPlayer = gameBoard.getPlayer(i);
            Player serverPlayer = new Player();
            serverPlayer.setName(gameBoardPlayer.getName());
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
