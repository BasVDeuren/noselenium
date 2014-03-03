package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.controllers.MapController;
import be.kdg.spacecrack.model.Planet;
import be.kdg.spacecrack.model.PlanetConnection;
import be.kdg.spacecrack.repositories.MapFactory;
import be.kdg.spacecrack.repositories.PlanetRepository;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class MapControllerTests {
    @Test
    public void getMap_valid_AllPlanetsConnected() throws Exception {
        MapController mapController = new MapController(new MapFactory(new PlanetRepository()));
        Planet[] planets = mapController.getMap().getPlanets();
        Planet startPlanet = planets[0];

        ArrayList<Planet> connectedPlanets = new ArrayList<Planet>();


        writeConnectedPlanetstoList(startPlanet, connectedPlanets);

        List<Planet> planetList = Arrays.asList(planets);
        assertEquals("All planets should be connected",planetList.size(), connectedPlanets.size());
    }

    private synchronized void writeConnectedPlanetstoList(Planet startPlanet, ArrayList<Planet> out) {
        out.add(startPlanet);

        for(PlanetConnection planetConnection : startPlanet.getPlanetConnections())
        {
            if(planetConnection != null){
                if(!out.contains(planetConnection.getChildPlanet())){
                    writeConnectedPlanetstoList(planetConnection.getChildPlanet(), out);
                }
            }
        }

      //  HibernateUtil.close(session);

    }

//    @Test
//    public void testGetConnectedPlanets() throws Exception {
//        Planet planet1 = new Planet("a", 0, 0);
//        Planet planet2 = new Planet("b", 0, 0);
//        Planet planet3 = new Planet("c", 0, 0);
//
//        Set<Planet> connectedPlanets1 = planet1.writeConnectedPlanetstoList();
//        connectedPlanets1.add(planet2);
//        connectedPlanets1.add(planet3);
//
//        Set<Planet> connectedPlanets2 = planet2.writeConnectedPlanetstoList();
//        connectedPlanets1.add(planet1);
//        connectedPlanets1.add(planet3);
//
//        Set<Planet> connectedPlanets3 = planet3.writeConnectedPlanetstoList();
//        connectedPlanets1.add(planet2);
//        connectedPlanets1.add(planet1);
//
//
//        ArrayList<Planet> out = new ArrayList<Planet>();
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        Transaction tx = session.beginTransaction();
//
//        writeConnectedPlanetstoList(planet1, out);
//        assertTrue(out.size() == 3);
//tx.commit();
//
//    }


}
