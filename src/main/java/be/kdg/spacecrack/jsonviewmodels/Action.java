package be.kdg.spacecrack.jsonviewmodels;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Ship;

public class Action {
    private String actionType;
    private Ship ship;
    private String destinationPlanet;
//actionType, ship, destinationPlanet
    public Action(String actionType, Ship ship, String destinationPlanet) {

        this.actionType = actionType;
        this.ship = ship;
        this.destinationPlanet = destinationPlanet;
    }

    public Action() {
    }

    public String getActionType() {
        return actionType;
    }

    public Ship getShip() {
        return ship;
    }

    public String getDestinationPlanet() {
        return destinationPlanet;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void setDestinationPlanet(String destinationPlanet) {
        this.destinationPlanet = destinationPlanet;
    }
}
