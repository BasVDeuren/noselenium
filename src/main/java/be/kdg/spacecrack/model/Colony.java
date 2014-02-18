package be.kdg.spacecrack.model;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "T_Colony")
public class Colony {
    @Id
    @GeneratedValue
    private int colonyId;

    @ManyToOne
    @JoinColumn(name="planetId")
    @JsonIgnore
    private Planet planet;

    @Transient
    private String planetName;
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


    public String getPlanetName(){
      return planet.getName();
    }

    public void setPlanetName(String planetName){
        this.planetName = planetName;
    }




}
