package be.kdg.spacecrack.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class Planet {
    private String name;
    private int x;
    private int y;

    @JsonIgnore
    private List<Planet> connectedPlanets;



    public Planet(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        connectedPlanets = new ArrayList<Planet>();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @JsonProperty("connectedPlanetNames")
    public List<String> getConnectedPlanetNames() {
        List<String> connectedPlanetNames1= new ArrayList<String>();
        for (Planet p : connectedPlanets) {
            connectedPlanetNames1.add(p.getName());
        }

        return connectedPlanetNames1;
    }


    public List<Planet> getConnectedPlanets() {
        return connectedPlanets;
    }


    public String getName() {
        return name;
    }
}
