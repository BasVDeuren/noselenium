package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Ship;

public interface IShipRepository {
    void createShip(Ship ship);

    Ship getShipByShipId(int shipId);

    void updateShip(Ship ship);

}
