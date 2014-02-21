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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
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

//todo: fix this!
    public String getPlanetName(){
        if(planet != null){
            return planet.getName();
        }else{
            return "";
        }
    }

    public void setPlanetName(String planetName){
        this.planetName = planetName;
    }




}
