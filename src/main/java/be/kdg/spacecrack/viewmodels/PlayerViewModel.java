package be.kdg.spacecrack.viewmodels;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import java.util.List;

public class PlayerViewModel {

    private int playerId;

    private List<ColonyViewModel> colonies;

    private List<ShipViewModel> ships;

    private int commandPoints;

    private boolean turnEnded;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public List<ColonyViewModel> getColonies() {
        return colonies;
    }

    public void setColonies(List<ColonyViewModel> colonies) {
        this.colonies = colonies;
    }

    public List<ShipViewModel> getShips() {
        return ships;
    }

    public void setShips(List<ShipViewModel> ships) {
        this.ships = ships;
    }

    public int getCommandPoints() {
        return commandPoints;
    }

    public void setCommandPoints(int commandPoints) {
        this.commandPoints = commandPoints;
    }

    public boolean isTurnEnded() {
        return turnEnded;
    }

    public void setTurnEnded(boolean turnEnded) {
        this.turnEnded = turnEnded;
    }
}
