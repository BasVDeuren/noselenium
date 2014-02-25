package be.kdg.spacecrack.viewmodels;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Ship;

public class ActionOutViewModel {
    private String actionType;
    private Ship ship;
    private String destinationPlanetName;
    private Integer playerId;
    private Integer gameId;

    //actionType, ship, destinationPlanetName
    public ActionOutViewModel(String actionType, Ship ship, String destinationPlanetName, Integer playerId, Integer gameId) {

        this.actionType = actionType;
        this.ship = ship;
        this.destinationPlanetName = destinationPlanetName;
        this.playerId = playerId;
        this.gameId = gameId;
    }

    public ActionOutViewModel() {
    }

    public String getActionType() {
        return actionType;
    }

    public Ship getShip() {
        return ship;
    }

    public String getDestinationPlanetName() {
        return destinationPlanetName;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void setDestinationPlanetName(String destinationPlanetName) {
        this.destinationPlanetName = destinationPlanetName;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }
}
