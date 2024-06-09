package dk.dtu.compute.se.pisd.RoboSpring.Model;

import dk.dtu.compute.se.pisd.RoboSpring.Model.Player.Player;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lobby
{
    private Long gameID;
    private List<Long> playerIDs;
}
