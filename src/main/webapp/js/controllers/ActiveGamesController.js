var spaceApp = angular.module('spaceApp');
spaceApp.controller("ActiveGamesController", function ($scope, $translate, Game, UserService) {

    $scope.games = [
        {gameId: "",
            name: ""}
    ];

    Game.query(function (data) {
        $scope.games = data;
    });

});