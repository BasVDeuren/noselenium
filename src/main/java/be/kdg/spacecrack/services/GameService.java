package be.kdg.spacecrack.services;

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.model.*;
import be.kdg.spacecrack.repositories.ColonyRepository;
import be.kdg.spacecrack.repositories.IPlanetRepository;
import be.kdg.spacecrack.repositories.IShipRepository;
import be.kdg.spacecrack.repositories.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public GameService() {
    }

    public GameService(IMapService mapService, IPlanetRepository planetRepository, ColonyRepository colonyRepository, ShipRepository shipRepository)
    {
        this.mapService = mapService;
        this.planetRepository = planetRepository;
        this.shipRepository = shipRepository;
        this.colonyRepository = colonyRepository;
    }

    @Override
    public Game createGame(Profile profile) {
        mapService.getSpaceCrackMap();
        Game game = new Game();
        Player player1 = new Player(profile);
        profile.addPlayer(player1);
        game.setPlayer1(player1);
        Planet planetA = planetRepository.getPlanetByName("a");
        Ship ship = new Ship(planetA);
        Colony colony = new Colony(planetA);

        shipRepository.createShip(ship);
        colonyRepository.createColony(colony);

        player1.getColonies().add(colony);

        player1.getShips().add(ship);

        return  game;
    }

    @Override
    public void moveShip(Ship ship, String planetName) {
        Ship shipDb = shipRepository.getShipByShipId(ship.getShipId());
        Planet sourcePlanet = shipDb.getPlanet();
        boolean connected = false;
        Set<PlanetConnection> planetConnections = sourcePlanet.getPlanetConnections();
        Planet destinationPlanet = null;
        for(PlanetConnection planetConnection : planetConnections)
        {
            System.out.println(planetConnection.toString());
            if(planetConnection.getChildPlanet().getName().equals( planetName) ){
               destinationPlanet = planetConnection.getChildPlanet();
                connected = true;
            }
        }
        if(connected)
        {
            shipDb.setPlanet(destinationPlanet);
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
}
