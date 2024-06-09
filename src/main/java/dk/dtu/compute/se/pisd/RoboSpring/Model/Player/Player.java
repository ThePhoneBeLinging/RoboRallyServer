package dk.dtu.compute.se.pisd.RoboSpring.Model.Player;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long gameID;
    private int x;
    private int y;
    public int lastVisitedCheckpoint;
    public String name;
    public String color;
    public String heading;
    public int tabNumber;
    public boolean movedByConveyorThisTurn;
    public int energyCubes;
    public boolean playersTurn;
}
