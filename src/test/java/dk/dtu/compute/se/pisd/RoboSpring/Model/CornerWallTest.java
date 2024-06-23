package dk.dtu.compute.se.pisd.RoboSpring.Model;


import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.controller.GameController;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.controller.MoveController;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.BoardElements.Walls.CornerWall;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Heading;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the corner wall element.
 *
 * @Author Emil
 */
public class CornerWallTest
{
    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;
    private MoveController moveController;

    @BeforeEach
    void setUp()
    {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        moveController = new MoveController(gameController);

        for (int i = 0; i < 2; i++)
        {
            Player player = new Player(board, "Player " + i, moveController);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.SOUTH);
        }

        new CornerWall(Heading.NORTH, Heading.EAST, board.getSpace(1, 2));
        new CornerWall(Heading.WEST, Heading.SOUTH, board.getSpace(0, 1));
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown()
    {
        gameController = null;
    }

    @Test
    void walkInFromValidDirection()
    {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();
        player.moveController.moveForward(player);
        Assertions.assertEquals(board.getSpace(0, 1), player.getSpace());
    }

    @Test
    void walkOutFromValidDirection()
    {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();
        player.setSpace(board.getSpace(0, 1));
        player.moveController.moveBack(player);
        Assertions.assertEquals(board.getSpace(0, 0), player.getSpace());
    }

    @Test
    void walkInFromInvalidDirection()
    {
        Board board = gameController.board;
        board.setCurrentPlayer(board.getPlayer(1));
        Player player = board.getCurrentPlayer();
        player.moveController.moveForward(player);
        Assertions.assertEquals(board.getSpace(1, 1), player.getSpace());
    }

    @Test
    void walkOutFromInvalidDirection()
    {
        Board board = gameController.board;
        board.setCurrentPlayer(board.getPlayer(0));
        Player player = board.getCurrentPlayer();
        player.setSpace(board.getSpace(0, 1));
        player.moveController.moveForward(player);
        Assertions.assertEquals(board.getSpace(0, 1), player.getSpace());
    }
}
