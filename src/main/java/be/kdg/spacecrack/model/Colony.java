package be.kdg.spacecrack.model;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import javax.persistence.*;

@Entity
@Table(name = "T_Colony")
public class Colony {
    @Id
    @GeneratedValue
    private int colonyId;

    @ManyToOne
    @JoinColumn(name="planetId")
    private Planet planet;

    public Colony() {
    }

    public Colony(Planet planet) {

        this.planet = planet;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }
}
