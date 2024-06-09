package dk.dtu.compute.se.pisd.RoboSpring.Model.Player;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "activeCards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActiveCards
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long boardID;
    private long playerID;
    private String command;
}
