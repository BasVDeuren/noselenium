package be.kdg.spacecrack.commands;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.services.IGameService;
import be.kdg.spacecrack.viewmodels.ShipViewModel;

public class MoveShipAction extends Action{
    private  ShipViewModel ship;
    private  String planetName;


    public MoveShipAction(IGameService gameService, Integer player, ShipViewModel ship, String destinationPlanetName) {
        super(gameService, player);
        this.ship = ship;
        this.planetName = destinationPlanetName;
    }

    @Override
    public void execute() {
        gameService.moveShip(ship.getShipId(), planetName);
    }

    public ShipViewModel getShip() {
        return ship;
    }

    public void setShip(ShipViewModel ship) {
        this.ship = ship;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }
}
