package be.kdg.spacecrack.services;

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnexpectedException;
import be.kdg.spacecrack.model.*;
import be.kdg.spacecrack.repositories.IGameRepository;
import be.kdg.spacecrack.repositories.IPlanetRepository;
import be.kdg.spacecrack.repositories.IPlayerRepository;
import be.kdg.spacecrack.repositories.IShipRepository;
import be.kdg.spacecrack.services.handlers.IMoveShipHandler;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.*;
import java.util.List;


/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Component(value = "gameService")
@Transactional
public class GameService implements IGameService {

   
    public static final int NEWCOLONYSTRENGTH = 1;
    @Autowired
    IPlanetRepository planetRepository;

    @Autowired
    IShipRepository shipRepository;

    @Autowired
    IColonyRepository colonyRepository;

    @Autowired
    IPlayerRepository playerRepository;

    @Autowired
    IGameRepository gameRepository;

    @Autowired
    IMoveShipHandler moveShipHandler;


    @Autowired
    IGraphService graphService;

    public GameService() {
    }

    public GameService(IPlanetRepository planetRepository, IColonyRepository colonyRepository, IShipRepository shipRepository, IPlayerRepository playerRepository, IGameRepository gameRepository, IMoveShipHandler moveShipHandler) {
//        this.mapService = mapService;
        this.planetRepository = planetRepository;
        this.shipRepository = shipRepository;
        this.colonyRepository = colonyRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.moveShipHandler = moveShipHandler;
    }

    @Override
    public int createGame(Profile userProfile, String gameName, Profile opponentProfile) {

        Game game = new Game();

        Player player1 = new Player(userProfile);
        Player player2 = new Player(opponentProfile);

        userProfile.getPlayers().add(player1);
        opponentProfile.getPlayers().add(player2);

        player1.setCommandPoints(START_COMMAND_POINTS);
        player2.setCommandPoints(START_COMMAND_POINTS);

        player1.setGame(game);
        player2.setGame(game);

        Planet planetA = planetRepository.getPlanetByName("a");
        Planet planetA3 = planetRepository.getPlanetByName("a3");

        Ship player1StartingShip = new Ship(planetA);
        Ship player2StartingShip = new Ship(planetA3);

        player1StartingShip.setStrength(NEWSHIPSTRENGTH);
        player2StartingShip.setStrength(NEWSHIPSTRENGTH);

        player1StartingShip.setPlayer(player1);
        player2StartingShip.setPlayer(player2);


        Colony player1StartingColony = new Colony(planetA);
        Colony player2StartingColony = new Colony(planetA3);

        player1StartingColony.setStrength(NEWCOLONYSTRENGTH);
        player2StartingColony.setStrength(NEWCOLONYSTRENGTH);

        player1StartingColony.setPlayer(player1);
        player2StartingColony.setPlayer(player2);

        game.setName(gameName);

        gameRepository.createGame(game);

        return game.getGameId();
    }

    @Override
    public void moveShip(Integer shipId, String planetName) {


        Ship ship = shipRepository.getShipByShipId(shipId);
        Game game = ship.getPlayer().getGame();
        Planet destinationPlanet = planetRepository.getPlanetByName(planetName);

        moveShipHandler.validateMove(ship, destinationPlanet);
        moveShipHandler.moveShip(ship, destinationPlanet);

        gameRepository.updateGame(game);
    }


    @Override
    public Planet getShipLocationByShipId(int shipId) {
        Ship shipDb = shipRepository.getShipByShipId(shipId);
        return shipDb.getPlanet();
    }

    @Override
    public void endTurn(Integer playerID) {
        Player player = playerRepository.getPlayerByPlayerId(playerID);
        Game game = player.getGame();
        if (!player.isTurnEnded()) {
            int commandPoints = player.getCommandPoints();
            player.setCommandPoints(commandPoints + COMMANDPOINTSPERTURN);
            player.setTurnEnded(true);

            boolean allTurnsEnded = true;
            List<Player> players = game.getPlayers();
            for (Player p : players) {
                if (!p.isTurnEnded()) {
                    allTurnsEnded = false;
                }

            }
            if (allTurnsEnded) {
                for (Player p : players) {
                    p.setTurnEnded(false);

                }
            }


        } else {
            throw new SpaceCrackNotAcceptableException("Turn is already ended");
        }
        gameRepository.updateGame(game);
    }

    @Override
    public List<Game> getGames(User user) {
        return gameRepository.getGamesByProfile(user.getProfile());
    }

    @Override
    public Game getGameByGameId(int gameId) {
        return gameRepository.getGameByGameId(gameId);
    }

    @Override
    public void checkVictory(Game gameByGameId) {
        /*if(gameByGameId.getTurnCounter()>MAXIMUMTURNSFORVICTORY){
            throw new SpaceCrackVictoryException();
        }*/
    }

    @Override
    public Player getActivePlayer(User user, Game game) {
        for (Player p : user.getProfile().getPlayers()) {
            for (Player gamePlayer : game.getPlayers()) {
                if (gamePlayer.getPlayerId() == p.getPlayerId()) {
                    return gamePlayer;
                }
            }

        }
        throw new SpaceCrackUnexpectedException("This user isn't playing this game");

    }

    @Override
    public void buildShip(Integer colonyId) {
        Ship shipOnPlanet = null;

        Colony colony = colonyRepository.getColonyById(colonyId);
        Player player = colony.getPlayer();

        Game game = player.getGame();
        if (player.getCommandPoints() < BUILDSHIPCOST || player.isTurnEnded()) {
            throw new SpaceCrackNotAcceptableException("Dear Sir or Lady, you have either run out of command points or your turn has ended, please wait for the other players to end their turn.");
        }
        for (Ship ship : player.getShips()) {
            if (ship.getPlanet().getName().equals(colony.getPlanet().getName())) {
                shipOnPlanet = ship;
            }
        }

        Ship ship;
        if (shipOnPlanet == null) {
            ship = new Ship();
            ship.setStrength(NEWSHIPSTRENGTH);
            ship.setPlayer(player);
            ship.setPlanet(colony.getPlanet());
        } else {
            shipOnPlanet.setStrength(shipOnPlanet.getStrength() + NEWSHIPSTRENGTH);
        }

        player.setCommandPoints(player.getCommandPoints() - BUILDSHIPCOST);
        gameRepository.updateGame(game);


    }

    // Call when a new colony has been captured, try to find if it is part of a new perimeter
    private List<Perimeter> detectPerimeter(Player player, Colony newColony) {
        // List of perimeters to return
        List<Perimeter> perimeters = new ArrayList<Perimeter>();
        // Get all colonies of this player (= the graph to find perimeters within)
        UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
        List<Colony> colonies = player.getColonies();
        Map<String, Planet> playerPlanetsMap = new HashMap<String, Planet>();
        List<Planet> playerPlanetsList = new ArrayList<Planet>();
        for(Colony colony : colonies) {
            Planet planet = colony.getPlanet();
            playerPlanetsList.add(planet);
            playerPlanetsMap.put(planet.getName(), planet);
            graph.addVertex(planet.getName());
            for(PlanetConnection connection : planet.getPlanetConnections()) {
                graph.addEdge(connection.getParentPlanet().getName(), connection.getChildPlanet().getName());
            }
        }

        // Get all the other planets of the map (all planets - player colonies)
        List<Planet> targetPlanets = new ArrayList<Planet>(Arrays.asList(planetRepository.getAll())); // all planets
        targetPlanets.removeAll(playerPlanetsList); // all planets without already captured planets

        // Find chordless cycles of the graph
        List<List<String>> cycles = graphService.calculateChordlessCyclesFromVertex(graph, newColony.getPlanet().getName());

        // For every cycles, make a possible perimeter
        for(List<String> cycle : cycles) {
            Perimeter perimeter = new Perimeter();
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

            Perimeter smallestPerimeter = perimetersForTarget.get(0);
            for(Perimeter perimeter : perimetersForTarget) {
                if(perimeter.getOutsidePlanets().size() < smallestPerimeter.getOutsidePlanets().size()) {
                    smallestPerimeter = perimeter;
                }
            }

            smallestPerimeter.getInsidePlanets().add(target);
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
