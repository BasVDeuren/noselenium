package be.kdg.spacecrack.services;

import be.kdg.spacecrack.exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.exceptions.SpaceCrackUnexpectedException;
import be.kdg.spacecrack.model.*;
import be.kdg.spacecrack.repositories.*;
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
    IGraphService graphService;

    public GameService() {
    }

    public GameService(IPlanetRepository planetRepository, IColonyRepository colonyRepository, IShipRepository shipRepository, IPlayerRepository playerRepository, IGameRepository gameRepository) {
        this.planetRepository = planetRepository;
        this.shipRepository = shipRepository;
        this.colonyRepository = colonyRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
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
        playerRepository.createPlayer(player1);
        playerRepository.createPlayer(player2);

        game.getPlayers().add(player1);
        game.getPlayers().add(player2);

        Planet planetA = planetRepository.getPlanetByName("a");
        Planet planetA3 = planetRepository.getPlanetByName("a3");

        Ship player1StartingShip = new Ship(planetA);
        Ship player2StartingShip = new Ship(planetA3);
        player1StartingShip.setStrength(NEWSHIPSTRENGTH);
        player2StartingShip.setStrength(NEWSHIPSTRENGTH);

        player1StartingShip.setPlayer(player1);
        player2StartingShip.setPlayer(player2);

        shipRepository.createShip(player1StartingShip);
        shipRepository.createShip(player2StartingShip);

        Colony player1StartingColony = new Colony(planetA);
        Colony player2StartingColony = new Colony(planetA3);

        player1StartingColony.setPlayer(player1);
        player2StartingColony.setPlayer(player2);

        player1.getColonies().add(player1StartingColony);
        player2.getColonies().add(player2StartingColony);

        colonyRepository.createColony(player1StartingColony);
        colonyRepository.createColony(player2StartingColony);



        player1.getShips().add(player1StartingShip);
        player2.getShips().add(player2StartingShip);

        player1StartingShip.setPlayer(player1);
        player2StartingShip.setPlayer(player2);

        playerRepository.updatePlayer(player1);
        playerRepository.updatePlayer(player2);

        shipRepository.updateShip(player1StartingShip);
        shipRepository.updateShip(player2StartingShip);

        colonyRepository.updateColony(player1StartingColony);
        colonyRepository.updateColony(player2StartingColony);

        game.setName(gameName);

        int id = gameRepository.createGame(game);

        return game.getGameId();
    }

    @Override
    public void moveShip(Integer shipId, String destinationPlanetName) {
        Ship ship = shipRepository.getShipByShipId(shipId);

        if (ship.getPlayer().isTurnEnded()) {
            throw new SpaceCrackNotAcceptableException("Player's turn is ended, not allowed to move!");
        }
        Planet sourcePlanet = ship.getPlanet();
        boolean connected = false;
        Set<PlanetConnection> planetConnections = sourcePlanet.getPlanetConnections();
        Planet destinationPlanet = null;
        for (PlanetConnection planetConnection : planetConnections) {
            if (planetConnection.getChildPlanet().getName().equals(destinationPlanetName)) {
                destinationPlanet = planetConnection.getChildPlanet();
                connected = true;
            }
        }
        if (connected) {
            Player player = ship.getPlayer();

            Game game = ship.getPlayer().getGame();
            List<Colony> coloniesByGame = colonyRepository.getColoniesByGame(game);
            boolean allowCreateColony = true;
            boolean allowMove = true;

            for (Colony c : coloniesByGame) {
                if (c.getPlanet().getPlanetId() == destinationPlanet.getPlanetId()) {
                    if (c.getPlayer().getPlayerId() == player.getPlayerId()) {

                        allowMove = true;
                        allowCreateColony = false;

                    } else {
                        throw new SpaceCrackNotAcceptableException("Taking enemy colonies not supported yet!");
                    }

                }
            }
            int cost = 0;
            if (allowMove) {
                cost += MOVESHIPCOST;
                if (allowCreateColony) {
                    cost += CREATECOLONYCOST;
                }
            }
            if (ship.getPlayer().getCommandPoints() - cost < 0) {
                throw new SpaceCrackNotAcceptableException("Insufficient command points");
            }
            if (allowMove) {
                player.setCommandPoints(player.getCommandPoints() - MOVESHIPCOST);
                Ship shipAlreadyOnPlanet = null;
                for (Ship s : player.getShips()) {
                    if (s.getPlanet().getName().equals(destinationPlanetName)) {
                        shipAlreadyOnPlanet = s;
                    }
                }
                if (shipAlreadyOnPlanet == null) {
                    ship.setPlanet(destinationPlanet);


                    if (allowCreateColony) {

                        Colony colony = new Colony(destinationPlanet);
                        colony.setPlayer(player);
                        colonyRepository.createColony(colony);
                        player.getColonies().add(colony);
                        player.setCommandPoints(player.getCommandPoints() - CREATECOLONYCOST);
                    }
                }else{
                    shipAlreadyOnPlanet.setStrength(shipAlreadyOnPlanet.getStrength() + ship.getStrength());
                    //HELP ONS VOCHTEN
                    shipRepository.deleteShip(ship);
                    player.getShips().remove(ship);
                    ship = shipAlreadyOnPlanet;
                }

            }

            playerRepository.updatePlayer(player);
            shipRepository.updateShip(ship);



        } else {
            throw new SpaceCrackNotAcceptableException("Ship cannot be moved to that planet!");
        }
    }

    @Override
    public Planet getShipLocationByShipId(int shipId) {
        Ship shipDb = shipRepository.getShipByShipId(shipId);
        return shipDb.getPlanet();
    }

    @Override
    public void endTurn(Integer playerID) {
        Player player = playerRepository.getPlayerByPlayerId(playerID);
        if (!player.isTurnEnded()) {
            int commandPoints = player.getCommandPoints();
            player.setCommandPoints(commandPoints + COMMANDPOINTSPERTURN);
            player.setTurnEnded(true);
            playerRepository.updatePlayer(player);
            Game game = gameRepository.getGameByPlayer(player);

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
                    playerRepository.updatePlayer(p);
                }
            }


        } else {
            throw new SpaceCrackNotAcceptableException("Turn is already ended");
        }
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
            player.getShips().add(ship);
        } else {
            shipOnPlanet.setStrength(shipOnPlanet.getStrength() + NEWSHIPSTRENGTH);
            ship = shipOnPlanet;
        }
        shipRepository.updateShip(ship);
        player.setCommandPoints(player.getCommandPoints() - BUILDSHIPCOST);
        playerRepository.updatePlayer(player);


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
