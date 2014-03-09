/**
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("createGameController", function ($scope, $translate, Game, FindPlayer, Profile) {

    //haal ingelogde gebruiker op
    Profile.get(function (data, headers) {
        console.log("get api/auth/user");
        $scope.loggedInProfileId = data.profile.profileId;
    }, function (data, headers) {
        $scope.loggedInProfileId = null;
    });


    $scope.gameData = {
        gameName: "", opponentProfileId: ""
    };
    $scope.gameId = "";

    $scope.createGame = function (opponentId) {
        $scope.gameData.opponentProfileId = opponentId;
        Game.save($scope.gameData, function (data) {

            $scope.gameId = data[0];

            $scope.go('/spacecrack/game/' + $scope.gameId);

        })
    };

    $scope.selectRandomPlayer = function(){
        FindPlayer.findUserByUserId().get({userId: $scope.loggedInProfileId}, function (data){
            $scope.gameData.opponentProfileId = data.profile.profileId;
            Game.save($scope.gameData, function (data) {
                $scope.gameId = data[0];
                $scope.go('/spacecrack/game/' + $scope.gameId);
            })
        })
    };

    $scope.foundPlayers = [];

    $scope.searchCriteria = 'email';

    $scope.findPlayers = function (searchString) {
        if ($scope.searchCriteria == "email") {
            FindPlayer.findUsersByEmail().get({email: searchString}, function (data, headers) {
                $scope.foundPlayers = data;
            });
        } else {
            FindPlayer.findUsersByUsername().get({username: searchString}, function (data, headers) {
                $scope.foundPlayers = data;
            });
        }
    };

    $scope.hideFoundPlayer = function (profileId) {
        return (profileId == $scope.loggedInProfileId);
    };

    $scope.getUserImage = function (image) {
        if (image == null) {
            return "../assets/userimage.png"
        } else {
            return image;
        }
    }
})
;
