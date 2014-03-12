package be.kdg.spacecrack.services.handlers;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.config.AsyncConfig;
import be.kdg.spacecrack.model.*;
import be.kdg.spacecrack.repositories.IGameRepository;
import be.kdg.spacecrack.repositories.IPlanetRepository;
import be.kdg.spacecrack.services.GameService;
import be.kdg.spacecrack.repositories.IColonyRepository;
import be.kdg.spacecrack.services.GraphAlgorithm;
import be.kdg.spacecrack.services.IGameService;
import be.kdg.spacecrack.services.IGameSynchronizer;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.ejb.AsyncResult;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component("moveShipHandler")
public class MoveShipHandler implements IMoveShipHandler {

    @Autowired
    private IColonyRepository colonyRepository;

    @Autowired
    private IPlanetRepository planetRepository;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private IGameSynchronizer gameSynchronizer;


    public MoveShipHandler() {
    }

    public MoveShipHandler(IColonyRepository colonyRepository, IPlanetRepository planetRepository, IGameSynchronizer gameSynchronizer)
    {
        this.colonyRepository = colonyRepository;
        this.planetRepository = planetRepository;
        this.gameSynchronizer = gameSynchronizer;
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


    private Colony colonizePlanet(Planet planet, final Player player) {
        final Colony colony = new Colony();
        colony.setPlanet(planet);
        colony.setPlayer(player);
        colony.setStrength(GameService.NEWCOLONYSTRENGTH);

       ExecutorService executorService = Executors.newSingleThreadExecutor();
       executorService.submit(new Callable<List<Perimeter>>() {
           @Override
           public List<Perimeter> call() throws Exception {
               HibernateTransactionManager transactionManager = (HibernateTransactionManager) applicationContext.getBean("transactionManager");
               TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));

               List<Perimeter> perimeters = detectPerimeter(player, colony);
               for (Perimeter perimeter : perimeters) {
                   List<Planet> insidePlanets = perimeter.getInsidePlanets();
                   for(Planet insidePlanet: insidePlanets){
                       Colony colony1 = new Colony();
                       colony1.setStrength(1);
                       colony1.setPlanet(insidePlanet);
                       colony1.setPlayer(player);
                   }
               }

               gameSynchronizer.updateGame(player.getGame());
               transactionManager.commit(status);
               return perimeters;
           }
       });


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
    public Future<List<Perimeter>> detectPerimeterAsync(Player player, Colony newColony)
    {
        return new AsyncResult<List<Perimeter>>(detectPerimeter(player, newColony));
    }

    // Call when a new colony has been captured, try to find if it is part of a new perimeter
    @Override
    public List<Perimeter> detectPerimeter(Player player, Colony newColony) {
        // List of perimeters to return
        List<Perimeter> perimeters = new ArrayList<Perimeter>();
        // Get all colonies of this player (= the graph to find perimeters within)
        UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
        List<Colony> colonies = player.getColonies();
        Map<String, Planet> playerPlanetsMap = new HashMap<String, Planet>();
        List<Planet> playerPlanetsList = new ArrayList<Planet>();
        // add colonies to the graph
        for(Colony colony : colonies) {
            Planet planet = colony.getPlanet();
            playerPlanetsList.add(planet);
            playerPlanetsMap.put(planet.getName(), planet);
            graph.addVertex(planet.getName());
        }
        // add the new colony as well
        Planet newPlanet = newColony.getPlanet();
        playerPlanetsList.add(newPlanet);
        playerPlanetsMap.put(newPlanet.getName(), newPlanet);
        graph.addVertex(newPlanet.getName());
        // add connections between colonies to the graph
        for(Planet planet : playerPlanetsList) {
            for(PlanetConnection connection : planet.getPlanetConnections()) {
                if(playerPlanetsList.contains(connection.getChildPlanet())) {
                    graph.addEdge(connection.getParentPlanet().getName(), connection.getChildPlanet().getName());
                }
            }
        }

        // Get all the other planets of the map (all planets - player colonies)
        List<Planet> targetPlanets = new ArrayList<Planet>(Arrays.asList(planetRepository.getAll())); // all planets
        targetPlanets.removeAll(playerPlanetsList); // all planets without already captured planets

        // Find chordless cycles of the graph
        List<List<String>> cycles = GraphAlgorithm.calculateChordlessCyclesFromVertex(graph, newColony.getPlanet().getName());

        // For every cycles, make a possible perimeter
        for(List<String> cycle : cycles) {
            Perimeter perimeter = new Perimeter(new ArrayList<Planet>(), new ArrayList<Planet>());
            for(String vertex : cycle) {
                Planet planet = playerPlanetsMap.get(vertex);
                perimeter.getOutsidePlanets().add(planet);
            }
            perimeters.add(perimeter);
        }

        // For every polygon (=cycle) test if it contains a target planet
        for(Planet target : targetPlanets) {
            List<Perimeter> perimetersForTarget = new ArrayList<Perimeter>();
            for(Perimeter perimeter : perimeters) {
                Polygon polygon = new Polygon();
                for(Planet planet : perimeter.getOutsidePlanets()) {
                    polygon.addPoint(planet.getX(), planet.getY());
                }

                if(polygon.contains(target.getX(), target.getY())) {
                    // This is a perimeter for this target planet (but check if it is the smallest)
                    perimetersForTarget.add(perimeter);
                }
            }

            if(!perimetersForTarget.isEmpty()) {
                Perimeter smallestPerimeter = perimetersForTarget.get(0);
                for(Perimeter perimeter : perimetersForTarget) {
                    if(perimeter.getOutsidePlanets().size() < smallestPerimeter.getOutsidePlanets().size()) {
                        smallestPerimeter = perimeter;
                    }
                }

                smallestPerimeter.getInsidePlanets().add(target);
            }
        }

        // Remove all the perimeters without inside planets
        for(Iterator<Perimeter> i = perimeters.iterator(); i.hasNext(); ) {
            Perimeter perimeter = i.next();
            if(perimeter.getInsidePlanets().size() == 0) {
                i.remove();
            }
        }

        return perimeters;
    }
}
