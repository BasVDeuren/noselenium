package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.model.Planet;
import be.kdg.spacecrack.model.SpaceCrackMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Tim on 11/02/14.
 */

@Controller
@RequestMapping("/map")
public class MapController {
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody SpaceCrackMap getMap()
    {

        Planet a = new Planet("a", 50, 250);

        Planet b = new Planet("b",100, 205);
        Planet b2 = new Planet("b2",100, 295);
        Planet c = new Planet("c",175,155);
        Planet c2 = new Planet("c2",175, 345);

        Planet d = new Planet("d",165,240);
        Planet d2 = new Planet("d2",165, 260);

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






        Planet[] planets = {
                a, b, b2, c, c2, d, d2
    };
         SpaceCrackMap spaceCrackMap = new SpaceCrackMap( planets);


        return spaceCrackMap;
    }

    private void connectPlanets(Planet leftStartPlanet, Planet planet1) {
        leftStartPlanet.getConnectedPlanets().add(planet1);
        planet1.getConnectedPlanets().add(leftStartPlanet);
    }
}
