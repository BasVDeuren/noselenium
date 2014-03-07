package be.kdg.spacecrack.services.handlers;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.*;

public interface IMoveShipHandler {
    void moveShip(Ship ship, Planet destinationPlanet);

    /**
     * This method does an early check if the player is allowed to attempt to make a move.
     * If not the method will throw an unchecked exception which will translate to HttpStatusCode 406: NotAcceptable
     *
     * @param ship
     * @param destinationPlanet
     */
    void validateMove(Ship ship, Planet destinationPlanet);

}
