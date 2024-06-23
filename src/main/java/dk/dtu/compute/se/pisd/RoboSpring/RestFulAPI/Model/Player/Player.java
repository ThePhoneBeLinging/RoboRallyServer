package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player
{

    public int lastVisitedCheckpoint;
    public String name;
    public String color;
    public String heading;
    public int tabNumber;
    public boolean movedByConveyorThisTurn;
    public int energyCubes;
    public boolean playersTurn;
    public boolean hasRetrievedProgrammingPhase;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long playerID;
    private long gameID;
    private int turnID;
    private int x;
    private int y;
}
