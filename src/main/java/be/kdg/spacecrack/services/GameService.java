package be.kdg.spacecrack.services;

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
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


    public static final int START_COMMAND_POINTS = 5;
    public static final int COMMANDPOINTSPERTURN = START_COMMAND_POINTS;
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

    public GameService() {
    }

    public GameService(IMapService mapService, IPlanetRepository planetRepository, IColonyRepository colonyRepository, IShipRepository shipRepository, IPlayerRepository playerRepository, IGameRepository gameRepository) {
        this.mapService = mapService;
        this.planetRepository = planetRepository;
        this.shipRepository = shipRepository;
        this.colonyRepository = colonyRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public int createGame(Profile userProfile, String gameName, Profile opponentProfile) {
        mapService.getSpaceCrackMap();
        Game game = new Game();

        Player player1 = new Player(userProfile);
        Player player2 = new Player(opponentProfile);
        player1.setCommandPoints(START_COMMAND_POINTS);
        player2.setCommandPoints(START_COMMAND_POINTS);

        playerRepository.createPlayer(player1);
        playerRepository.createPlayer(player2);

        //  opponentProfile.addPlayer(player2);

        game.setPlayer1(player1);
        game.setPlayer2(player2);

        Planet planetA = planetRepository.getPlanetByName("a");
        Planet planetA3 = planetRepository.getPlanetByName("a3");

        Ship player1StartingShip = new Ship(planetA);
        Ship player2StartingShip = new Ship(planetA3);

        player1StartingShip.setPlayer(player1);
        player2StartingShip.setPlayer(player2);

        shipRepository.createShip(player1StartingShip);
        shipRepository.createShip(player2StartingShip);

        Colony player1StartingColony = new Colony(planetA);
        Colony player2StartingColony = new Colony(planetA3);

        colonyRepository.createColony(player1StartingColony);
        colonyRepository.createColony(player2StartingColony);

        player1.getColonies().add(player1StartingColony);
        player2.getColonies().add(player2StartingColony);

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
    public void moveShip(Ship ship, String planetName) {
        Ship shipDb = shipRepository.getShipByShipId(ship.getShipId());
        if (shipDb.getPlayer().getCommandPoints() < 1) {
            throw new SpaceCrackNotAcceptableException("Insufficient command points");
        } else if (shipDb.getPlayer().isTurnEnded()) {
            throw new SpaceCrackNotAcceptableException("Player's turn is ended, not allowed to move!");
        }
        Planet sourcePlanet = shipDb.getPlanet();
        boolean connected = false;
        Set<PlanetConnection> planetConnections = sourcePlanet.getPlanetConnections();
        Planet destinationPlanet = null;
        for (PlanetConnection planetConnection : planetConnections) {
            if (planetConnection.getChildPlanet().getName().equals(planetName)) {
                destinationPlanet = planetConnection.getChildPlanet();
                connected = true;
            }
        }
        if (connected) {
            shipDb.setPlanet(destinationPlanet);
            Player player = shipDb.getPlayer();
            Colony colony = new Colony(destinationPlanet);
            colonyRepository.createColony(colony);
            player.getColonies().add(colony);
            player.setCommandPoints(player.getCommandPoints() - 1);
            playerRepository.updatePlayer(player);
            shipRepository.updateShip(shipDb);
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
    public void endTurn(Player player) {
        if (!player.isTurnEnded()) {
            int commandPoints = player.getCommandPoints();
            player.setCommandPoints(commandPoints + COMMANDPOINTSPERTURN);
            player.setTurnEnded(true);
            playerRepository.updatePlayer(player);
            Game game = gameRepository.getGameByPlayer(player);
            Player player1;
            Player player2;
            if (game.getPlayer1().isTurnEnded() && game.getPlayer2().isTurnEnded()) {
                player1 = game.getPlayer1();
                player1.setTurnEnded(false);
                player2 = game.getPlayer2();
                player2.setTurnEnded(false);
                playerRepository.updatePlayer(player1);
                playerRepository.updatePlayer(player2);
            }
        }else{
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
            if (p.getPlayerId() == game.getPlayer1().getPlayerId()) {
                return p;
            }
            if (p.getPlayerId() == game.getPlayer2().getPlayerId()) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Player getOpponentPlayer(User user, Game game) {
        for (Player p : user.getProfile().getPlayers()) {
            if (p.getPlayerId() == game.getPlayer1().getPlayerId()) {
                return game.getPlayer2();
            }
            if (p.getPlayerId() == game.getPlayer2().getPlayerId()) {
                return game.getPlayer1();
            }
        }
        return null;
    }
}
