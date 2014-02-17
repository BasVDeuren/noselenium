package be.kdg.spacecrack.model;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class SpaceCrackMap {

    private Planet[] planets;
    private Planet player1StartingPlanet;
    private Planet player2StartingPlanet;

    public SpaceCrackMap(Planet[] planets ) {
        this.planets = planets;
    }


    public Planet[] getPlanets() {
        return planets;
    }

    public void setPlanets(Planet[] planets) {
        this.planets = planets;
    }

    public Planet getPlayer1StartingPlanet() {
        return player1StartingPlanet;
    }

    public Planet getPlayer2StartingPlanet() {
        return player2StartingPlanet;
    }

    public void setPlayer1StartingPlanet(Planet player1StartingPlanet) {
        this.player1StartingPlanet = player1StartingPlanet;
    }

    public void setPlayer2StartingPlanet(Planet player2StartingPlanet) {
        this.player2StartingPlanet = player2StartingPlanet;
    }
}
