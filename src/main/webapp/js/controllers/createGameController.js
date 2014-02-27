/**
* Project Application Development
* Karel de Grote-Hogeschool
    * 2013-2014
    *
    */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("createGameController", function ($scope, $translate, Game,FindPlayer) {
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
    $scope.foundPlayers = ["aaa","a","ayuiyui","ajijo","ahuihuh","jajijia","jijaa","azaea","ayyièyi","aijoijoi","uiuioa","kokma"];
    $scope.findPlayers = function(searchString){
//        FindPlayer.get({username: searchString},function(data, headers){
//          $scope.foundPlayers = data;
//        });
    } ;

    $scope.getUserImage = function (image) {
        if (image == null) {
            return "../assets/userimage.png"
        } else {
            return image;
        }
    }
});
