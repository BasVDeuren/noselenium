var spaceApp = angular.module('spaceApp');
spaceApp.controller("ActiveGamesController", function ($scope, $translate, Game,UserService) {
    if (!UserService.loggedIn) {
        $scope.go('/login');
    } else {
        $scope.games = [
            {gameId: ""}
        ];

        Game.query(function (data) {
            $scope.games = data;
        });
    }
});