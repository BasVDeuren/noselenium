package be.kdg.spacecrack.utilities;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Colony;
import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Player;
import be.kdg.spacecrack.model.Ship;
import be.kdg.spacecrack.viewmodels.ColonyViewModel;
import be.kdg.spacecrack.viewmodels.GameViewModel;
import be.kdg.spacecrack.viewmodels.PlayerViewModel;
import be.kdg.spacecrack.viewmodels.ShipViewModel;

public interface IViewModelConverter {
    ColonyViewModel convertColonyToViewModel(Colony colony);

    GameViewModel convertGameToViewModel(Game game);

    PlayerViewModel convertPlayerToViewModel(Player player);

    ShipViewModel convertShipToViewModel(Ship ship);
}
