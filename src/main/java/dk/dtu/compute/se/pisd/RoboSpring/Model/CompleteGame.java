package dk.dtu.compute.se.pisd.RoboSpring.Model;

import dk.dtu.compute.se.pisd.RoboSpring.Model.Player.Card;
import dk.dtu.compute.se.pisd.RoboSpring.Model.Player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompleteGame
{
    private Long gameID;
    private Long senderID;
    private Board board;
    private List<EnergyCube> energyCubes;
    private List<Player> playerList;
    private List<Card> cards;
}