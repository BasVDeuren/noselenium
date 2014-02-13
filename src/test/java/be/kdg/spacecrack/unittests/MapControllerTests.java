package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.controllers.MapController;
import be.kdg.spacecrack.model.Planet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Tim on 12/02/14.
 */
public class MapControllerTests {
    @Test
    public void getMap_valid_AllPlanetsConnected() throws Exception {
        MapController mapController = new MapController();
        Planet[] planets = mapController.getMap().getPlanets();
        Planet startPlanet = planets[0];

        ArrayList<Planet> connectedPlanets = new ArrayList<Planet>();
        getConnectedPlanets(startPlanet, connectedPlanets);
        assertTrue(connectedPlanets.containsAll(Arrays.asList(planets)));


    }

    private void getConnectedPlanets(Planet startPlanet, ArrayList<Planet> out) {
        out.add(startPlanet);
        for(Planet planet : startPlanet.getConnectedPlanets())
        {
            if(planet!= null){
                if(!out.contains(planet)){

                    getConnectedPlanets(planet, out);
                }
            }
        }

    }

    @Test
    public void testGetConnectedPlanets() throws Exception {
        Planet planet1 = new Planet("a",0, 0);
        Planet planet2 = new Planet("b",0, 0);
        Planet planet3 = new Planet("c", 0, 0);

        List<Planet> connectedPlanets1 = planet1.getConnectedPlanets();
        connectedPlanets1.add(planet2);
        connectedPlanets1.add(planet3);

        List<Planet> connectedPlanets2 = planet2.getConnectedPlanets();
        connectedPlanets1.add(planet1);
        connectedPlanets1.add(planet3);

        List<Planet> connectedPlanets3 = planet3.getConnectedPlanets();
        connectedPlanets1.add(planet2);
        connectedPlanets1.add(planet1);


        ArrayList<Planet> out = new ArrayList<Planet>();
        getConnectedPlanets(planet1, out);
        assertTrue(out.size() == 3);


    }
}
