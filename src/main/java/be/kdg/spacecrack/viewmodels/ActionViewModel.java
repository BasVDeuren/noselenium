package be.kdg.spacecrack.viewmodels;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

public class ActionViewModel {
    private String actionType;
    private ShipViewModel ship;
    private String destinationPlanetName;
    private ColonyViewModel colony;
    private Integer playerId;
    private Integer gameId;

    public ActionViewModel(String actionType, ShipViewModel ship, String destinationPlanetName, ColonyViewModel colony, Integer playerId, Integer gameId) {
        this.actionType = actionType;
        this.ship = ship;
        this.destinationPlanetName = destinationPlanetName;
        this.colony = colony;
        this.playerId = playerId;
        this.gameId = gameId;
    }

    public ActionViewModel() {
    }

    public String getActionType() {
        return actionType;
    }

    public ShipViewModel getShip() {
        return ship;
    }

    public String getDestinationPlanetName() {
        return destinationPlanetName;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setShip(ShipViewModel ship) {
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

    public ColonyViewModel getColony() {
        return colony;
    }

    public void setColony(ColonyViewModel colony) {
        this.colony = colony;
    }
}
