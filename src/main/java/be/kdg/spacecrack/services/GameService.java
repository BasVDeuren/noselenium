package be.kdg.spacecrack.services;

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnexpectedException;
import be.kdg.spacecrack.model.*;
import be.kdg.spacecrack.repositories.*;
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

        player1.setGame(game);
        player2.setGame(game);
        playerRepository.createPlayer(player1);
        playerRepository.createPlayer(player2);

        //  opponentProfile.addPlayer(player2);


        game.getPlayers().add(player1);
        game.getPlayers().add(player2);

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

        player1StartingColony.setPlayer(player1);
        player2StartingColony.setPlayer(player2);
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
    public void moveShip(Integer shipId, String planetName) {
        Ship ship = shipRepository.getShipByShipId(shipId);

       if (ship.getPlayer().isTurnEnded()) {
            throw new SpaceCrackNotAcceptableException("Player's turn is ended, not allowed to move!");
        }
        Planet sourcePlanet = ship.getPlanet();
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

            Player player = ship.getPlayer();



            Game game = gameRepository.getGameByPlayer(player);
            List<Colony> coloniesByGame = colonyRepository.getColoniesByGame(game);
            boolean allowCreateColony = true;
            boolean allowMove = true;
            int cost = 2;
            for (Colony c : coloniesByGame) {
                if (c.getPlanet().getPlanetId() == destinationPlanet.getPlanetId()) {
                    if (c.getPlayer().getPlayerId() == player.getPlayerId()) {

                        allowMove= true;
                        allowCreateColony =false;
                        cost = 1;
                    }else{
                        allowMove = false;
                        allowCreateColony= false;
                        cost = 0;
                    }

                }
            }
            if (ship.getPlayer().getCommandPoints() - cost  < 0) {
                throw new SpaceCrackNotAcceptableException("Insufficient command points");
            }
            if(allowMove){
                ship.setPlanet(destinationPlanet);
                playerRepository.updatePlayer(player);
                shipRepository.updateShip(ship);
                player.setCommandPoints(player.getCommandPoints() - 1);
                if(allowCreateColony){

                    Colony colony = new Colony(destinationPlanet);
                    colony.setPlayer(player);
                    colonyRepository.createColony(colony);
                    player.getColonies().add(colony);
                    player.setCommandPoints(player.getCommandPoints() - 1);
                }
            }
            playerRepository.updatePlayer(player);



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
            for(Player p : players)
             {
               if(!p.isTurnEnded())
               {
                    allTurnsEnded = false;
               }

            }
            if(allTurnsEnded){
               for(Player p: players)
               {
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
           for(Player gamePlayer : game.getPlayers())
           {
               if(gamePlayer.getPlayerId() == p.getPlayerId())
               {
                   return gamePlayer;
               }
           }

        }
        throw new SpaceCrackUnexpectedException("This user isn't playing this game");

    }

    @Override
    public Player getOpponentPlayer(User user, Game game) {
        for (Player p : user.getProfile().getPlayers()) {
            for(Player gamePlayer : game.getPlayers())
            {
                if(gamePlayer.getPlayerId() != p.getPlayerId())
                {
                    return gamePlayer;
                }
            }

        }
        throw new SpaceCrackUnexpectedException("Unexpected error, no opponent found!");
    }
}
