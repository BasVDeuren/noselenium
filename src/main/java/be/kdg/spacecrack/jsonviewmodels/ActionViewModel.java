package be.kdg.spacecrack.jsonviewmodels;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Ship;

public class ActionViewModel {
    private String actionType;
    private Ship ship;
    private String destinationPlanet;
    private Integer playerId;
    private int gameId;

    //actionType, ship, destinationPlanet
    public ActionViewModel(String actionType, Ship ship, String destinationPlanet, Integer playerId, int gameId) {

        this.actionType = actionType;
        this.ship = ship;
        this.destinationPlanet = destinationPlanet;
        this.playerId = playerId;
        this.gameId = gameId;
    }

    public ActionViewModel() {
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

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
