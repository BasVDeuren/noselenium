package be.kdg.spacecrack.services;

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.Exceptions.SpaceCrackVictoryException;
import be.kdg.spacecrack.model.*;
import be.kdg.spacecrack.repositories.IGameRepository;
import be.kdg.spacecrack.repositories.IPlanetRepository;
import be.kdg.spacecrack.repositories.IPlayerRepository;
import be.kdg.spacecrack.repositories.IShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Component(value = "gameService")
public class GameService implements IGameService {


    @Autowired
    IPlanetRepository planetRepository;
    @Autowired
    IMapService mapService;

    @Autowired
    IShipRepository shipRepository;

    @Autowired
    IColonyRepository colonyRepository;

    @Autowired
    IPlayerRepository playerRepository;

    @Autowired
    IGameRepository gameRepository;

    private static final int MAXIMUMTURNSFORVICTORY = 5;

    private static final int NEWCOMMANDPOINTS = 5;

    public GameService() {
    }

    public GameService(IMapService mapService, IPlanetRepository planetRepository, IColonyRepository colonyRepository, IShipRepository shipRepository, IPlayerRepository playerRepository, IGameRepository gameRepository)
    {
        this.mapService = mapService;
        this.planetRepository = planetRepository;
        this.shipRepository = shipRepository;
        this.colonyRepository = colonyRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public Game createGame(Profile profile) {
        mapService.getSpaceCrackMap();
        Game game = new Game();

        Player player1 = new Player(profile);

        playerRepository.createPlayer(player1);

        profile.addPlayer(player1);
        game.setPlayer1(player1);
        Planet planetA = planetRepository.getPlanetByName("a");
        Ship ship = new Ship(planetA);
        ship.setPlayer(player1);
        shipRepository.createShip(ship);
        Colony colony = new Colony(planetA);
        colonyRepository.createColony(colony);

        player1.getColonies().add(colony);
        player1.getShips().add(ship);
        ship.setPlayer(player1);

        playerRepository.updatePlayer(player1);
        shipRepository.updateShip(ship);
        colonyRepository.updateColony(colony);
        int id = gameRepository.createGame(game);

        game.setName("Game " + id);
        gameRepository.updateGame(game);

        return  game;
    }

    @Override
    public void moveShip(Ship ship, String planetName) {
        Ship shipDb = shipRepository.getShipByShipId(ship.getShipId());
        if(shipDb.getPlayer().getCommandPoints() < 1){
            throw new SpaceCrackNotAcceptableException("Insufficient command points");
        }
        Planet sourcePlanet = shipDb.getPlanet();
        boolean connected = false;
        Set<PlanetConnection> planetConnections = sourcePlanet.getPlanetConnections();
        Planet destinationPlanet = null;
        for(PlanetConnection planetConnection : planetConnections)
        {
            if(planetConnection.getChildPlanet().getName().equals( planetName) ){
               destinationPlanet = planetConnection.getChildPlanet();
                connected = true;
            }
        }
        if(connected)
        {
            shipDb.setPlanet(destinationPlanet);
            Player player = shipDb.getPlayer();
            Colony colony = new Colony(destinationPlanet);
            colonyRepository.createColony(colony);
            player.getColonies().add(colony);
            player.setCommandPoints(player.getCommandPoints() - 1);
            playerRepository.updatePlayer(player);
            shipRepository.updateShip(shipDb);
        }else{
            throw new SpaceCrackNotAcceptableException("Ship cannot be moved to that planet!");
        }
    }

    @Override
    public Planet getShipLocationByShipId(int shipId) {
        Ship shipDb = shipRepository.getShipByShipId(shipId);
        return shipDb.getPlanet();
    }

    @Override
    public void endTurn(int playerId) {
        Player player = playerRepository.getPlayerByPlayerId(playerId);
        int commandPoints = player.getCommandPoints();
        player.setCommandPoints(commandPoints +NEWCOMMANDPOINTS);
        playerRepository.updatePlayer(player);
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
        if(gameByGameId.getTurnCounter()>MAXIMUMTURNSFORVICTORY){
            throw new SpaceCrackVictoryException();
        }
    }


}
