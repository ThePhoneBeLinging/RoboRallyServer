package dk.dtu.compute.se.pisd.RoboSpring.Model.Player;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cardsOnHand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class discardedCards
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long boardID;
    private long playerID;
    private String command;
}
