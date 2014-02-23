/**
* Project Application Development
* Karel de Grote-Hogeschool
    * 2013-2014
    *
    */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("createGameController", function ($scope, $translate, Game) {
    $scope.gameData = {
        gameName:"", opponent:""
    }
    $scope.gameId ="";

    $scope.createGame = function(){
        Game.save($scope.gameData, function(data){
            $scope.gameId = data;
        })
    };
});
