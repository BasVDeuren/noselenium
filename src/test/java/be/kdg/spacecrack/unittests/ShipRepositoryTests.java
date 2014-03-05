package be.kdg.spacecrack.unittests;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Player;
import be.kdg.spacecrack.model.Ship;
import be.kdg.spacecrack.repositories.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ShipRepositoryTests {

    private IShipRepository shipRepository= new ShipRepository();
    IGameRepository gameRepository = new GameRepository();

    @Test
    public void saveGameWithPlayerAndShip_valid_shipandplayerCreatedIn1Query() {



        //region Arrange

        Ship ship = new Ship();
        Player player = new Player();
        Game game = new Game();
        //Create Player and add to game

        game.getPlayers().add(player);
        player.setGame(game);

        //Create Ship and add to player
        ship.setPlayer(player);
        player.getShips().add(ship);
        //Create the game in the database
        int gameId = gameRepository.createGame(game);
        Game gameDb = gameRepository.getGameByGameId(gameId);
        //endregion


        //region Act
         Ship result = shipRepository.getShipByShipId(gameDb.getPlayers().get(0).getShips().get(0).getShipId());
        //endregion


        //Assert
        assertNotNull(result);
        assertNotNull(result.getPlayer());
    }

    private Game createGameWithShip() {


        Ship ship = new Ship();
        Player player = new Player();
        Game game = new Game();
        //Create Player and add to game
        game.getPlayers().add(player);
        player.setGame(game);
        //Create Ship and add to player
        ship.setPlayer(player);
        player.getShips().add(ship);
        //Create the game in the database
        int gameId = gameRepository.createGame(game);
        return gameRepository.getGameByGameId(gameId);
    }

    @Test
    public void deleteShip_shipInDb_shipDeleted() {
        Game game = createGameWithShip();

        int playerId = game.getPlayers().get(0).getPlayerId();
        PlayerRepository playerRepository = new PlayerRepository();

        Ship ship = game.getPlayers().get(0).getShips().get(0);
        shipRepository.deleteShip(ship);
        Ship result = shipRepository.getShipByShipId(ship.getShipId());
        assertEquals("Ship should no longer be in db",null, result);
        Player playerByPlayerId = playerRepository.getPlayerByPlayerId(playerId);
        assertNotNull(playerByPlayerId);
        assertEquals("player should no longer have ships", 0,playerByPlayerId.getShips().size());
    }



}
