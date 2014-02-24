/**
* Project Application Development
* Karel de Grote-Hogeschool
    * 2013-2014
    *
    */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("createGameController", function ($scope, $translate, Game) {
    $scope.gameData = {
        gameName:"", opponentProfileId:""
    };
    $scope.gameId ="";

    $scope.createGame = function(){
        Game.save($scope.gameData, function(data){

            $scope.gameId = data[0];

            $scope.go('/spacecrack/game/'+ $scope.gameId);

        })
    };
});
