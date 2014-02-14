package be.kdg.spacecrack.services;

import be.kdg.spacecrack.controllers.MapController;
import be.kdg.spacecrack.model.*;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class GameService {
    public Game createGame(Contact creator, Contact opponent) {
        Player player1 = new Player(creator);
        Player player2 = new Player(opponent);
        //TODO: CHANGE THIS ASAP THIS IS RLY RLY BAD
        MapController controller = new MapController();
        SpaceCrackMap map = controller.getMap();

        map.getPlanets()[0].setPlayer(player1);
        Planet player1StartingPlanet = map.getPlanets()[0];
        player1StartingPlanet.setPlayer(player1);

        map.setPlayer1StartingPlanet(player1StartingPlanet);
        Planet player2StartingPlanet = map.getPlanets()[2];
        player2StartingPlanet.setPlayer(player2);
        map.setPlayer2StartingPlanet(player2StartingPlanet);
        Ship player1StartingShip = new Ship();
        player1StartingShip.setPlanet(player1StartingPlanet);
        Ship player2StartingShip = new Ship();
        player2StartingShip.setPlanet(player2StartingPlanet);
        player1.getShips().add(player1StartingShip);
        player2.getShips().add(player2StartingShip);

        return new Game(player1, player2, map);
    }


//    public void moveShip(Player player, Ship ship, Planet targetPlanet) {
//        if(ship.getPlayer() == player){
//        if(ship.getPlanet().getConnectedPlanets().contains(targetPlanet))
//        {
//            ship.setPlanet(targetPlanet);
//        }else{
//            throw new SpaceCrackNotAcceptableException("Illegal move, ships can only move to connected planets");
//        }
//        }else{
//            throw new SpaceCrackNotAcceptableException("Illegal move, ship is from another player");
//        }
//    }
}
