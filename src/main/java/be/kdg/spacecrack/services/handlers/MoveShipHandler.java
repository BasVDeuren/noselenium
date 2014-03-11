package be.kdg.spacecrack.services.handlers;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.model.*;
import be.kdg.spacecrack.services.GameService;
import be.kdg.spacecrack.repositories.IColonyRepository;
import be.kdg.spacecrack.services.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component("moveShipHandler")
public class MoveShipHandler implements IMoveShipHandler {

    @Autowired
    private IColonyRepository colonyRepository;


    public MoveShipHandler() {
    }

    public MoveShipHandler(IColonyRepository colonyRepository)
    {
        this.colonyRepository = colonyRepository;

    }


    @Override
    public void moveShip(Ship ship, Planet destinationPlanet) {
        Player player = ship.getPlayer();
        Game game = player.getGame();
        player.setCommandPoints(player.getCommandPoints() -GameService.MOVESHIPCOST);

        List<Colony> colonies = colonyRepository.getColoniesByGame(game);
        boolean planetIsColonized = false;
        for (Colony colony : colonies) {
            if (colonyIsOnPlanet(colony, destinationPlanet)) {
                if (colonyIsFromThisPlayer(colony, player)) {
                    moveShipToColony(ship, colony);
                } else {
                    attackEnemyColony(ship, colony);
                }
                planetIsColonized = true;
            }
        }
        if(!planetIsColonized){
            moveAndColonize(ship, destinationPlanet);
        }

    }

    private boolean colonyIsFromThisPlayer(Colony colony, Player player) {
        return colony.getPlayer().getPlayerId() == player.getPlayerId();
    }

    private boolean colonyIsOnPlanet(Colony colony, Planet destinationPlanet) {
        return colony.getPlanet().getName().equals(destinationPlanet.getName());
    }


    private Colony colonizePlanet(Planet planet, Player player) {
        Colony colony = new Colony();
        colony.setPlanet(planet);
        colony.setPlayer(player);
        colony.setStrength(GameService.NEWCOLONYSTRENGTH);
        return colony;
    }

    private void moveAndColonize(Ship ship, Planet destinationPlanet) {

        Player player = ship.getPlayer();
        if(player.getCommandPoints() < IGameService.CREATECOLONYCOST)
        {
            throw new SpaceCrackNotAcceptableException("Insufficient CommandPoints");
        }

        Colony colony = colonizePlanet(destinationPlanet, player);
        ship.setPlanet(colony.getPlanet());
        player.setCommandPoints(player.getCommandPoints() - IGameService.CREATECOLONYCOST);

    }


    private void attackEnemyColony(Ship actingShip, Colony colony) {


        Player enemyPlayer = colony.getPlayer();

        List<Ship> enemyShips = enemyPlayer.getShips();


        Planet planet = colony.getPlanet();
        for (Ship enemyShip : enemyShips) {
            //if enemyShip is on the same planet as the colony
            if (shipIsOnPlanet(enemyShip, planet)) {
                Piece winner = fightAndDetermineWinner(actingShip, enemyShip);
                if (winner == actingShip) {
                    attackEnemyColony((Ship) winner, colony);
                }
                return;

            }

        }

        Piece winner = fightAndDetermineWinner(actingShip, colony);
        if(winner == actingShip)
        {

            moveAndColonize(actingShip, planet);

        }



    }

    private boolean shipIsOnPlanet(Ship ship, Planet planet) {
        return ship.getPlanet().getName().equals(planet.getName());
    }

    private Piece fightAndDetermineWinner(Piece piece1, Piece piece2) {
        int strengthDifference = piece1.getStrength() - piece2.getStrength();
        if (strengthDifference < 0) {

            deletePiece(piece1);
            piece2.setStrength(-strengthDifference);

            return piece2;
        } else if (strengthDifference > 0) {
            deletePiece(piece2);
            piece1.setStrength(strengthDifference);
            return piece1;
        } else {
            deletePiece(piece1);
            deletePiece(piece2);
            return null;
        }
    }

   private void deletePiece(Piece piece) {
        if (piece instanceof Ship) {
            Ship ship =(Ship) piece;
            ship.getPlayer().removeShip(ship);
        } else {
            Colony colony = (Colony) piece;
            colony.getPlayer().removeColony(colony);
        }

    }

    /**
     * This method does an early check if the player is allowed to attempt to make a move.
     * If not the method will throw an unchecked exception which will translate to HttpStatusCode 406: NotAcceptable
     *
     * @param ship
     * @param destinationPlanet
     */
    @Override
    public void validateMove(Ship ship, Planet destinationPlanet) {
        if (ship.getPlayer().getCommandPoints() < IGameService.MOVESHIPCOST) {
            throw new SpaceCrackNotAcceptableException("The player cannot move because he has insufficient commandPoints!");
        }

        if (ship.getPlayer().isTurnEnded()) {
            throw new SpaceCrackNotAcceptableException("The player cannot execute the action because his turn has ended");
        }
        Planet sourcePlanet = ship.getPlanet();
        boolean connected = false;
        Set<PlanetConnection> planetConnections = sourcePlanet.getPlanetConnections();

        for (PlanetConnection planetConnection : planetConnections) {
            if (planetConnection.getChildPlanet().getName().equals(destinationPlanet.getName())) {
                connected = true;
            }
        }
        if (!connected) {
            throw new SpaceCrackNotAcceptableException("Invalid move, the planets are not connected!");
        }
    }


    private void moveShipToColony(Ship ship, Colony colony) {
        Player player = ship.getPlayer();
        Planet planet = colony.getPlanet();
        List<Ship> ships = player.getShips();

        Ship shipToMergeWith = null;
        for (Ship alliedShip : ships) {
            if (shipIsOnPlanet(alliedShip, planet)) {
              shipToMergeWith= alliedShip;

            }
        }

        if(shipToMergeWith == null)
        {
            ship.setPlanet(colony.getPlanet());
        }else{
            mergeAndGetShip(ship, shipToMergeWith);
        }

    }


    private Ship mergeAndGetShip(Ship shipToMerge, Ship shipToMergeWith) {
        shipToMergeWith.setStrength(shipToMergeWith.getStrength() + shipToMerge.getStrength());
        deletePiece(shipToMerge);
        return shipToMergeWith;
    }


}
