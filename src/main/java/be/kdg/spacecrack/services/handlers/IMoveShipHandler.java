package be.kdg.spacecrack.services.handlers;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.*;
import be.kdg.spacecrack.services.GameService;

import java.util.List;

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

    // Call when a new colony has been captured, try to find if it is part of a new perimeter
    List<Perimeter> detectPerimeter(Player player, Colony newColony);
}
