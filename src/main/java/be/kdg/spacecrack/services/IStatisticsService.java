package be.kdg.spacecrack.services;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.viewmodels.StatisticsViewModel;

public interface IStatisticsService {
    public StatisticsViewModel getStatistics(int profileId);
}
