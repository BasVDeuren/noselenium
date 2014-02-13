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

    public SpaceCrackMap(Planet[] planets ) {


        this.planets = planets;
    }


    public Planet[] getPlanets() {
        return planets;
    }

    public void setPlanets(Planet[] planets) {
        this.planets = planets;
    }
}
