package dk.dtu.compute.se.pisd.RoboSpring;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.CompleteGame;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.EnergyCube;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Card;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.UpgradeCard;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.controller.GameController;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Board;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Command;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Heading;
import dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Phase;

import java.util.ArrayList;
import java.util.Objects;

public class Util
{

    public static Board fromServerBoardToGameBoard(CompleteGame serverBoard)
    {
        Board gameBoard = LoadBoard.loadBoard(serverBoard.getBoard().getBoardname());
        GameController gameController = new GameController(gameBoard);
        gameBoard.setStep(serverBoard.getBoard().getStep());
        gameBoard.setPhase(Phase.valueOf(serverBoard.getBoard().getPhase()));
        gameBoard.setGameID(serverBoard.getGameID());
        gameBoard.setTurnID(serverBoard.getTurnID());
        serverBoard.setUpgradeCards(new ArrayList<>());
        serverBoard.setEnergyCubes(new ArrayList<>());

        for (Player player : serverBoard.getPlayerList())
        {
            dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player gameBoardPlayer =
                    gameBoard.getPlayer(Math.toIntExact(player.getPlayerID()));
            for (int i = 0; i < gameBoard.getPlayersNumber(); i++)
            {
                if (gameBoard.getPlayer(i) != null && gameBoard.getPlayer(i).getPlayerID() == player.getPlayerID())
                {
                    gameBoardPlayer = gameBoard.getPlayer(i);
                }
            }
            if (gameBoardPlayer == null)
            {
                gameBoardPlayer = new dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player(gameBoard,
                        player.getName(), gameController.moveController);
            }
            gameBoard.addPlayer(gameBoardPlayer);
            gameBoardPlayer.setPlayerID(player.getPlayerID());
            gameBoardPlayer.setSpace(gameBoard.getSpace(player.getX(), player.getY()));
            gameBoardPlayer.setLastVisitedCheckPoint(player.getLastVisitedCheckpoint());
            gameBoardPlayer.setHeading(Heading.valueOf(player.getHeading()));
            gameBoardPlayer.setMovedByConveyorThisTurn(player.isMovedByConveyorThisTurn());
            gameBoardPlayer.setEnergyCubes(player.getEnergyCubes());
            gameBoardPlayer.setThisPlayerTurn(player.isPlayersTurn());

            if (gameBoardPlayer.isThisPlayerTurn())
            {
                gameBoard.setCurrentPlayer(gameBoardPlayer);
            }
        }
        for (UpgradeCard upgradeCard : serverBoard.getUpgradeCards())
        {
            dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.UpgradeCard upgradeCardToAdd =
                    new dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.UpgradeCard(upgradeCard.getCardName(),
                            upgradeCard.getPrice());
            for (int i = 0; i < gameBoard.getPlayersNumber(); i++)
            {
                if (Objects.equals(gameBoard.getPlayer(i), upgradeCard.getPlayerID()))
                {
                    gameBoard.getPlayer(i).addUpgradeCard(upgradeCardToAdd);
                }
            }
            if (upgradeCard.getPlayerID() == null)
            {
                gameBoard.getUpgradeCards().add(upgradeCardToAdd);
            }
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
                            while (gamePlayer.getProgramField(k).getProgrammingCard() != null)
                            {
                                k++;
                                if (k == dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player.NO_REGISTERS - 1)
                                {
                                    break;
                                }
                            }
                            gamePlayer.getProgramField(k).setCard(cardToAdd);
                            break;
                        case "HAND":
                            int j = 0;
                            while (gamePlayer.getCardField(j).getProgrammingCard() != null)
                            {
                                j++;
                                if (j == dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player.NO_CARDS - 1)
                                {
                                    break;
                                }
                            }
                            gamePlayer.getCardField(j).setCard(cardToAdd);
                            break;
                        case "ACTIVE":
                            gamePlayer.activeCardsPile.playerCards.add(cardToAdd);
                            break;
                        case "DISCARD":
                            gamePlayer.discardedCardsPile.playerCards.add(cardToAdd);
                            break;
                    }
                }
            }
        }
        for (dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.EnergyCube energyCube : serverBoard.getEnergyCubes())
        {
            gameBoard.addEnergyCube(gameBoard.getSpace(energyCube.getX(), energyCube.getY()));
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
        serverBoard.setTurnID(gameBoard.getTurnID());
        completeServerBoard.setBoard(serverBoard);
        completeServerBoard.setPlayerList(new ArrayList<>());
        completeServerBoard.setGameID(gameBoard.getGameID());
        completeServerBoard.setEnergyCubes(new ArrayList<>());
        completeServerBoard.setCards(new ArrayList<>());
        completeServerBoard.setUpgradeCards(new ArrayList<>());
        completeServerBoard.setTurnID(gameBoard.getTurnID());
        completeServerBoard.setEnergyCubes(new ArrayList<>());

        for (int i = 0; i < gameBoard.getPlayersNumber(); i++)
        {
            dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player player = gameBoard.getPlayer(i);
            for (dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.UpgradeCard upgradeCard : player.getUpgradeCards())
            {
                UpgradeCard serverUpgradeCard = new UpgradeCard();
                serverUpgradeCard.setCardName(upgradeCard.getName());
                serverUpgradeCard.setPrice(upgradeCard.getPrice());
                serverUpgradeCard.setPlayerID(player.getPlayerID());
                serverUpgradeCard.setGameID(gameBoard.getGameID());
                completeServerBoard.getUpgradeCards().add(serverUpgradeCard);
            }
        }
        for (dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.UpgradeCard upgradeCard : gameBoard.getUpgradeCards())
        {
            UpgradeCard serverUpgradeCard = new UpgradeCard();
            serverUpgradeCard.setCardName(upgradeCard.getName());
            serverUpgradeCard.setGameID(gameBoard.getGameID());
            serverUpgradeCard.setPrice(upgradeCard.getPrice());
            serverUpgradeCard.setPlayerID(null);
            completeServerBoard.getUpgradeCards().add(serverUpgradeCard);
        }

        int i = 0;
        while (gameBoard.getPlayer(i) != null)
        {
            dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player gameBoardPlayer = gameBoard.getPlayer(i);
            Player serverPlayer = new Player();
            serverPlayer.setName(gameBoardPlayer.getName());
            serverPlayer.setGameID(gameBoardPlayer.board.getGameID());
            serverPlayer.setPlayerID(gameBoardPlayer.getPlayerID());
            serverPlayer.setTurnID(gameBoard.getTurnID());
            serverPlayer.setX(gameBoardPlayer.getSpace().x);
            serverPlayer.setY(gameBoardPlayer.getSpace().y);
            serverPlayer.setLastVisitedCheckpoint(gameBoardPlayer.getLastVisitedCheckPoint());
            serverPlayer.setHeading(gameBoardPlayer.getHeading().toString());
            serverPlayer.setMovedByConveyorThisTurn(gameBoardPlayer.getMovedByConveyorThisTurn());
            serverPlayer.setEnergyCubes(gameBoardPlayer.getEnergyCubes());
            serverPlayer.setPlayersTurn(gameBoardPlayer.isThisPlayerTurn());
            completeServerBoard.getPlayerList().add(serverPlayer);

            for (int j = 0; j < dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player.NO_REGISTERS; j++)
            {
                dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Card card =
                        gameBoardPlayer.getProgramField(j).getProgrammingCard();
                if (card != null)
                {
                    Card serverCard = new Card();
                    serverCard.setCommand(card.getCommand().toString());
                    serverCard.setPlayerID(gameBoardPlayer.getPlayerID());
                    serverCard.setLocation("REGISTER");
                    serverCard.setGameID(gameBoard.getGameID());

                    completeServerBoard.getCards().add(serverCard);
                }

            }
            if (gameBoard.getPhase() == Phase.PLAYER_INTERACTION && gameBoard.getOptions() != null && gameBoard.getCurrentPlayer() == gameBoardPlayer)
            {
                for (String command : gameBoard.getOptions())
                {
                    Card serverCard = new Card();
                    serverCard.setCommand(command);
                    serverCard.setPlayerID(gameBoardPlayer.getPlayerID());
                    serverCard.setLocation("OPTION");
                    serverCard.setGameID(gameBoard.getGameID());
                    completeServerBoard.getCards().add(serverCard);
                }
            }
            for (int k = 0; k < dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Player.NO_CARDS; k++)
            {
                dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Card card =
                        gameBoardPlayer.getCardField(k).getProgrammingCard();
                if (card != null)
                {
                    Card serverCard = new Card();
                    serverCard.setCommand(card.getCommand().toString());
                    serverCard.setPlayerID(gameBoardPlayer.getPlayerID());
                    serverCard.setLocation("HAND");
                    serverCard.setGameID(gameBoard.getGameID());
                    completeServerBoard.getCards().add(serverCard);
                }
            }
            for (dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Card card :
                    gameBoard.getPlayer(i).activeCardsPile.playerCards)
            {
                Card serverCard = new Card();
                serverCard.setCommand(card.getCommand().toString());
                serverCard.setPlayerID(gameBoardPlayer.getPlayerID());
                serverCard.setLocation("ACTIVE");
                serverCard.setGameID(gameBoard.getGameID());
                completeServerBoard.getCards().add(serverCard);
            }
            for (dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.Card card :
                    gameBoard.getPlayer(i).discardedCardsPile.playerCards)
            {
                Card serverCard = new Card();
                serverCard.setCommand(card.getCommand().toString());
                serverCard.setPlayerID(gameBoardPlayer.getPlayerID());
                serverCard.setLocation("DISCARD");
                serverCard.setGameID(gameBoard.getGameID());
                completeServerBoard.getCards().add(serverCard);
            }
            i++;
        }
        if (gameBoard.getOptions() != null && !gameBoard.getOptions().isEmpty())
        {
            serverBoard.setPlayerID(gameBoard.getCurrentPlayer().getPlayerID());
            completeServerBoard.setCommandsToChooseBetween(gameBoard.getOptions());
        }
        for (dk.dtu.compute.se.pisd.RoboSpring.RoboRally.model.BoardElements.EnergyCube energyCube :
                gameBoard.getEnergyCubes())
        {
            EnergyCube serverEnergyCube = new EnergyCube();
            serverEnergyCube.setX(energyCube.getSpace().x);
            serverEnergyCube.setY(energyCube.getSpace().y);
            serverEnergyCube.setGameID(gameBoard.getGameID());
            serverEnergyCube.setTurnID(gameBoard.getTurnID());
            completeServerBoard.getEnergyCubes().add(serverEnergyCube);
        }
        return completeServerBoard;
    }
}
