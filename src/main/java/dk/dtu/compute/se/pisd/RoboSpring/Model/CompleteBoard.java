package dk.dtu.compute.se.pisd.RoboSpring.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompleteBoard
{
    private Board board;
    private List<EnergyCube> energyCubes;


}
