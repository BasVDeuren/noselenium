package be.kdg.spacecrack.viewmodels;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

public class StatisticsViewModel {
    double winRatio;
    int amountOfGames;
    double averageAmountOfColoniesPerWin;
    double averageAmountOfShipsPerWin;

    public double getWinRatio() {
        return winRatio;
    }

    public void setWinRatio(double winRatio) {
        this.winRatio = winRatio;
    }

    public int getAmountOfGames() {
        return amountOfGames;
    }

    public void setAmountOfGames(int amountOfGames) {
        this.amountOfGames = amountOfGames;
    }

    public double getAverageAmountOfColoniesPerWin() {
        return averageAmountOfColoniesPerWin;
    }

    public void setAverageAmountOfColoniesPerWin(double averageAmountOfColoniesPerWin) {
        this.averageAmountOfColoniesPerWin = averageAmountOfColoniesPerWin;
    }

    public double getAverageAmountOfShipsPerWin() {
        return averageAmountOfShipsPerWin;
    }

    public void setAverageAmountOfShipsPerWin(double averageAmountOfShipsPerWin) {
        this.averageAmountOfShipsPerWin = averageAmountOfShipsPerWin;
    }
}
