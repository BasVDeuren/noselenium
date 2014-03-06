package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Planet;

public interface IPlanetRepository {
    Planet getPlanetByName(String planetName);

    Planet[] getAll();

    void createPlanets(Planet[] planets);
}
