package be.kdg.spacecrack.model;

/**
 * Created by Tim on 11/02/14.
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
