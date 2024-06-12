package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "boards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board
{

    @Id
    private Long gameID;
    private String boardname;
    private int step;
    private String phase;


}