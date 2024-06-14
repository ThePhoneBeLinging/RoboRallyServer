package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Controller;

import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Card;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player.Player;
import dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Repository.CardsRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class CardsController {
    private final CardsRepository cardsRepository;

    CardsController(CardsRepository cardsRepository)
    {
        this.cardsRepository = cardsRepository;
    }

    public ArrayList<Card> initializeDeck(Player player) {
        int amountOfForwards = 5;
        int amountOfRight = 5;
        int amountOfLeft = 5;
        int amountOfFastForward = 5;
        int amountOfMoveThree = 5;
        int amountOfUTurn = 5;
        int amountOfMoveBack = 5;
        int amountOfAgain = 5;
        int amountOfPowerUp = 5;
        int amountOfOptionLeftOrRight = 5;

        ArrayList<Card> playerCards = new ArrayList<>();
        for (int i = 0; i < amountOfForwards; i++)
        {
            Card card = new Card();
            card.setCommand("FORWARD");
            card.setPlayerID(player.getPlayerID());
            card.setGameID(player.getGameID());
            playerCards.add(card);
        }

        for (int i = 0; i < amountOfRight; i++)
        {
            Card card = new Card();
            card.setCommand("RIGHT");
            card.setPlayerID(player.getPlayerID());
            card.setGameID(player.getGameID());
            playerCards.add(card);
        }

        for (int i = 0; i < amountOfLeft; i++)
        {
            Card card = new Card();
            card.setCommand("LEFT");
            card.setPlayerID(player.getPlayerID());
            card.setGameID(player.getGameID());
            playerCards.add(card);
        }

        for (int i = 0; i < amountOfFastForward; i++)
        {
            Card card = new Card();
            card.setCommand("FAST_FORWARD");
            card.setPlayerID(player.getPlayerID());
            card.setGameID(player.getGameID());
            playerCards.add(card);
        }

        for (int i = 0; i < amountOfMoveThree; i++)
        {
            Card card = new Card();
            card.setCommand("MOVE_THREE");
            card.setPlayerID(player.getPlayerID());
            card.setGameID(player.getGameID());
            playerCards.add(card);
        }

        for (int i = 0; i < amountOfUTurn; i++)
        {
            Card card = new Card();
            card.setCommand("U_TURN");
            card.setPlayerID(player.getPlayerID());
            card.setGameID(player.getGameID());
            playerCards.add(card);
        }

        for (int i = 0; i < amountOfMoveBack; i++)
        {
            Card card = new Card();
            card.setCommand("MOVE_BACK");
            card.setPlayerID(player.getPlayerID());
            card.setGameID(player.getGameID());
            playerCards.add(card);
        }

        for (int i = 0; i < amountOfAgain; i++)
        {
            Card card = new Card();
            card.setCommand("AGAIN");
            card.setPlayerID(player.getPlayerID());
            card.setGameID(player.getGameID());
            playerCards.add(card);
        }

        for (int i = 0; i < amountOfPowerUp; i++)
        {
            Card card = new Card();
            card.setCommand("POWER_UP");
            card.setPlayerID(player.getPlayerID());
            card.setGameID(player.getGameID());
            playerCards.add(card);
        }

        for (int i = 0; i < amountOfOptionLeftOrRight; i++)
        {
            Card card = new Card();
            card.setCommand("OPTION_LEFT_OR_RIGHT");
            card.setPlayerID(player.getPlayerID());
            card.setGameID(player.getGameID());
            playerCards.add(card);
        }
        shuffleDeck(playerCards);

        return playerCards;
    }

    private void shuffleDeck(ArrayList<Card> playerCards)
    {
        for (int i = 0; i < playerCards.size(); i++)
        {
            int randomIndex = (int) (Math.random() * playerCards.size());
            Card tempCard = playerCards.get(randomIndex);
            playerCards.set(randomIndex, playerCards.get(i));
            playerCards.set(i, tempCard);
        }
    }
}
