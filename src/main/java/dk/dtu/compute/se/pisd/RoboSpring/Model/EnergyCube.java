package dk.dtu.compute.se.pisd.RoboSpring.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "energycubes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnergyCube
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
    private int x;
    private int y;
}
