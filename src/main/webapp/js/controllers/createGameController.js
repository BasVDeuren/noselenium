/**
* Project Application Development
* Karel de Grote-Hogeschool
    * 2013-2014
    *
    */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("createGameController", function ($scope, $translate, Game,FindPlayer,Profile) {

    //haal ingelogde gebruiker op
    Profile.get(function (data, headers) {
        console.log("get api/auth/user");
        $scope.loggedInProfileId = data.profile.profileId;
    }, function (data, headers) {
        $scope.loggedInProfileId = null;
    });


    $scope.gameData = {
        gameName:"", opponentProfileId:""
    };
    $scope.gameId ="";

    $scope.createGame = function(opponentId){
        $scope.gameData.opponentProfileId = opponentId;
        Game.save($scope.gameData, function(data){

            $scope.gameId = data[0];

            $scope.go('/spacecrack/game/'+ $scope.gameId);

        })
    };
    $scope.foundPlayers = [];
    $scope.findPlayers = function(searchString){
        FindPlayer.get({username: searchString},function(data, headers){
          $scope.foundPlayers = data;
        });
    } ;

    $scope.hideFoundPlayer = function(profileId){
         return (profileId == $scope.loggedInProfileId);
    };

    $scope.getUserImage = function (image) {
        if (image == null) {
            return "../assets/userimage.png"
        } else {
            return image;
        }
    }
});
