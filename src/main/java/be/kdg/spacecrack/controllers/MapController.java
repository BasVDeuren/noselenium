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

        Planet leftStartPlanet = new Planet(50, 250);
        Planet[] planets = {
                leftStartPlanet
    };
         SpaceCrackMap spaceCrackMap = new SpaceCrackMap( planets);


        return spaceCrackMap;
    }
}
