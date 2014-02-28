package be.kdg.spacecrack.commands;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Player;
import be.kdg.spacecrack.model.Ship;
import be.kdg.spacecrack.services.IGameService;

public class MoveShipAction extends Action{
    private  Ship ship;
    private  String planetName;


    public MoveShipAction(IGameService gameService, Player player, Ship ship, String destinationPlanetName) {
        super(gameService, player);
        this.ship = ship;
        this.planetName = destinationPlanetName;
    }

    @Override
    public void execute() {
        gameService.moveShip(ship, planetName);
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }
}
