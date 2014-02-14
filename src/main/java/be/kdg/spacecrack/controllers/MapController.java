package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.model.Planet;
import be.kdg.spacecrack.model.SpaceCrackMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

@Controller
@RequestMapping("/map")
public class MapController {
    public static final int MAP_HEIGHT = 500;
    public static final int MAP_LENGTH = 800;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody SpaceCrackMap getMap()
    {

        Planet a = new Planet("a", 50, 250);
        Planet a3 = new Planet("a3", 750, 250);

        Planet b = new Planet("b", 100, 205);
        Planet b2 = new Planet("b2",100, 295);
        Planet b3 = new Planet("b3", MAP_LENGTH - 100, 205);
        Planet b4 = new Planet("b4", MAP_LENGTH - 100, MAP_HEIGHT - 205);

        Planet c = new Planet("c", 175, 155);
        Planet c2 = new Planet("c2", 175, 345);
        Planet c3 = new Planet("c3", MAP_LENGTH - 175, 155);
        Planet c4 = new Planet("c4", MAP_LENGTH - 175, MAP_HEIGHT - 155);

        Planet d = new Planet("d", 165, 240);
        Planet d2 = new Planet("d2", 165, 260);
        Planet d3 = new Planet("d3", MAP_LENGTH - 165, 240);
        Planet d4 = new Planet("d4", MAP_LENGTH - 165, MAP_HEIGHT - 240);

        Planet e = new Planet("e", 165, 90);
        Planet e2 = new Planet("e2", 165, MAP_HEIGHT - 90);
        Planet e3 = new Planet("e3", MAP_LENGTH - 165, 90);
        Planet e4 = new Planet("e4", MAP_LENGTH - 165, MAP_HEIGHT - 90);

        Planet f = new Planet("f", 200, 40);
        Planet f2 = new Planet("f2", 200, MAP_HEIGHT - 40);
        Planet f3 = new Planet("f3", MAP_LENGTH - 200, 40);
        Planet f4 = new Planet("f4", MAP_LENGTH - 200, MAP_HEIGHT - 40);

        Planet g = new Planet("g", 205, 230);
        Planet g2 = new Planet("g2", 205, MAP_HEIGHT - 230);
        Planet g3 = new Planet("g3", MAP_LENGTH - 205, 230);
        Planet g4 = new Planet("g4", MAP_LENGTH - 205, MAP_HEIGHT - 230);

        Planet h = new Planet("h", 250, 75);
        Planet h2 = new Planet("h2", 250, MAP_HEIGHT - 75);
        Planet h3 = new Planet("h3", MAP_LENGTH - 250, 75);
        Planet h4 = new Planet("h4", MAP_LENGTH - 250, MAP_HEIGHT - 75);

        Planet i = new Planet("i", 235, 270);
        Planet i2 = new Planet("i2", 235, MAP_HEIGHT - 270);
        Planet i3 = new Planet("i3", MAP_LENGTH - 235, 270);
        Planet i4 = new Planet("i4", MAP_LENGTH - 235, MAP_HEIGHT - 270);

        Planet j = new Planet("j", 240, 235);
        Planet j2 = new Planet("j2", 240, MAP_HEIGHT - 235);
        Planet j3 = new Planet("j3", MAP_LENGTH - 240, 235);
        Planet j4 = new Planet("j4", MAP_LENGTH - 240, MAP_HEIGHT - 235);

        Planet k = new Planet("k", 300, 15);
        Planet k2 = new Planet("k2", 300, MAP_HEIGHT - 15);
        Planet k3 = new Planet("k3", MAP_LENGTH - 300, 15);
        Planet k4 = new Planet("k4", MAP_LENGTH - 300, MAP_HEIGHT - 15);

        Planet l = new Planet("l", 325, 70);
        Planet l2 = new Planet("l2", 325, MAP_HEIGHT - 70);
        Planet l3 = new Planet("l3", MAP_LENGTH - 325, 70);
        Planet l4 = new Planet("l4", MAP_LENGTH - 325, MAP_HEIGHT - 70);

        Planet m = new Planet("m", 325, 155);
        Planet m2 = new Planet("m2", 325, MAP_HEIGHT - 155);
        Planet m3 = new Planet("m3", MAP_LENGTH - 325, 155);
        Planet m4 = new Planet("m4", MAP_LENGTH - 325, MAP_HEIGHT - 155);

        Planet n = new Planet("n", 310, 195);
        Planet n2 = new Planet("n2", 310, MAP_HEIGHT - 195);
        Planet n3 = new Planet("n3", MAP_LENGTH - 310, 195);
        Planet n4 = new Planet("n4", MAP_LENGTH - 310, MAP_HEIGHT - 195);

        Planet o = new Planet("o", 365, 130);
        Planet o2 = new Planet("o2", 365, MAP_HEIGHT - 130);
        Planet o3 = new Planet("o3", MAP_LENGTH - 365, 130);
        Planet o4 = new Planet("o4", MAP_LENGTH - 365, MAP_HEIGHT - 130);

        Planet p = new Planet("p", 340, 220);
        Planet p2 = new Planet("p2", 340, MAP_HEIGHT - 220);
        Planet p3 = new Planet("p3", MAP_LENGTH - 340, 220);
        Planet p4 = new Planet("p4", MAP_LENGTH - 340, MAP_HEIGHT - 220);

        Planet q = new Planet("q", 380, 200);
        Planet q2 = new Planet("q2", 380, MAP_HEIGHT - 200);
        Planet q3 = new Planet("q3", MAP_LENGTH - 380, 200);
        Planet q4 = new Planet("q4", MAP_LENGTH - 380, MAP_HEIGHT - 200);
/*
        connectPlanets(a, b);
        connectPlanets(a, b2);
        connectPlanets(b, b2);
        connectPlanets(b, c);
        connectPlanets(d, b);
        connectPlanets(c, d);
        connectPlanets(b2, d);
        connectPlanets(c2, b2);
        connectPlanets(d2, d);
        connectPlanets(d2, c2);
*/


        Planet[] planets = {
                a, a3,
                b, b2, b3, b4,
                c, c2, c3, c4,
                d, d2, d3, d4,
                e, e2, e3, e4,
                f, f2, f3, f4,
                g, g2, g3, g4,
                h, h2, h3, h4,
                i, i2, i3, i4,
                j, j2, j3, j4,
                k, k2, k3, k4,
                l, l2, l3, l4,
                m, m2, m3, m4,
                n, n2, n3, n4,
                o, o2, o3, o4,
                p, p2, p3, p4,
                q, q2, q3, q4
        };

        connectPlanetsByRadius(planets, 105);

        SpaceCrackMap spaceCrackMap = new SpaceCrackMap(planets);


        return spaceCrackMap;
    }

    private void connectPlanetsByRadius(Planet[] planets, int radius) {
        for(Planet checkPlanet : planets) {
            for(Planet planet : planets) {
                // Point coords
                int pX = planet.getX();
                int pY = planet.getY();
                // Circle centre coords
                int cX = checkPlanet.getX();
                int cY = checkPlanet.getY();

                double distance = Math.sqrt((pX - cX) * (pX - cX) + (pY - cY) * (pY - cY));
                if(distance < radius) { // In in circle
                    checkPlanet.addConnection(planet);
                }
            }
        }
    }
    /* Deprecated, use Planet.addConnection instead
    private void connectPlanets(Planet leftStartPlanet, Planet planet1) {
        leftStartPlanet.getConnectedPlanets().add(planet1);
        planet1.getConnectedPlanets().add(leftStartPlanet);
    }
    */
}
