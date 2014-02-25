/**
 * Created by Atheesan on 24/02/14.
 */
var spaceApp = angular.module('spaceApp');

spaceApp.controller("ChatController", function ($scope, $cookieStore, Profile, Contact, Spinner, $http, UserService, $firebase,$location) {
    if (!UserService.loggedIn) {
        $scope.go('/login');
    } else {
        $scope.scrollToBottom = function(){
            var objDiv = document.getElementById("chatBody");
            objDiv.scrollTop = objDiv.scrollHeight;
        };

        Profile.get(function (data, headers) {
            $scope.username = data.username;
        }, function (data, headers) {
            $scope.username = "";
        });

        //get gameId out of url
        $scope.currentGameId = $location.path().split("/").pop();
        var firebaseUrl = 'https://amber-fire-3394.firebaseio.com/'+$scope.currentGameId;
        var ref = new Firebase(firebaseUrl);
        $scope.msg = "";
        $scope.messages = $firebase(ref);
        $scope.autoSend = function (e) {
            if (e.keyCode != 13) return;
            $scope.addMessage();
        };
        $scope.addMessage = function () {
            if ($scope.msg != "") {
                $scope.messages.$add({from: $scope.username, body: $scope.msg});
                $scope.msg = "";
            }
        };

        $scope.removeAllMessages = function(){
            $scope.messages.$remove();
        }
    }
});
