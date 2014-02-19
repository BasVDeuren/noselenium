package be.kdg.spacecrack.services;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Colony;

public interface IColonyRepository {
    void createColony(Colony colony);

    void updateColony(Colony colony);
}
