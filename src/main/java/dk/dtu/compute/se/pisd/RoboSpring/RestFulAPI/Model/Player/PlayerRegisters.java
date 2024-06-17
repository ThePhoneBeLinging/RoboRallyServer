package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRegisters {
    private ArrayList<Integer> registerCards;
    private Long playerID;
    private Long gameID;
}
