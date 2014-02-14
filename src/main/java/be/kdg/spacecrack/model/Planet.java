package be.kdg.spacecrack.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Player player;

    @JsonIgnore
    private Set<Planet> connectedPlanets;

    public Planet(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        connectedPlanets = new HashSet<Planet>();
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

    public void addConnection(Planet planet) {
        if(planet != this) {
            connectedPlanets.add(planet);
            planet.getConnectedPlanets().add(this);
        }
    }

    @JsonProperty("connectedPlanets")
    public List<Planet> getConnectedPlanetWraps() {
        List<Planet> connectedPlanetWraps= new ArrayList<Planet>();
        for (Planet p : connectedPlanets) {
            connectedPlanetWraps.add(new Planet(p.name, p.x, p.y));
        }

        return connectedPlanetWraps;
    }


    public Set<Planet> getConnectedPlanets() {
        return connectedPlanets;
    }


    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
