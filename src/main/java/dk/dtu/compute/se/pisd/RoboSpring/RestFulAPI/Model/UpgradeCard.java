package dk.dtu.compute.se.pisd.RoboSpring.RestFulAPI.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "upgradecards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpgradeCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long gameID;
    private String cardName;
    private int price;
    private Boolean isBought;
}
