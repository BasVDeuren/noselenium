package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Player;

public interface IPlayerRepository {
    void createPlayer(Player player);

    void updatePlayer(Player player);

    Player getPlayerByPlayerId(int playerId);
}
