/**
 * Created by Tim on 11/03/14.
 */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("StatisticsController", function ($scope, $translate, StatisticsService) {
    $scope.statistics = {
        winRatio: 0,
        amountOfGames: 0,
        averageAmountOfColoniesPerWin: 0,
        averageAmountOfShipsPerWin: 0
    }

    StatisticsService.get(function(data){
        $scope.statistics = data;
    })
});
