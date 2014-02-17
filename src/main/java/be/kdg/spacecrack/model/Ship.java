package be.kdg.spacecrack.model;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import javax.persistence.*;

@Entity
@Table(name= "T_Ship")
public class Ship {
    @Id
    @GeneratedValue
    private int shipId;

    @ManyToOne
    @JoinColumn(name="playerId")
    private Player player;

    @ManyToOne
    @JoinColumn(name="planetId")
    private Planet planet;

    public Ship(Planet planet) {
        this.planet = planet;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public Planet getPlanet() {
        return planet;
    }
}
