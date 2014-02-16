package be.kdg.spacecrack.services;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Planet;
import be.kdg.spacecrack.model.SpaceCrackMap;

public interface IMapService {

    void connectPlanetsByRadius(Planet[] planets, int radius);

    SpaceCrackMap getSpaceCrackMap();
}
