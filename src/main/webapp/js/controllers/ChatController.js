/**
 * Created by Atheesan on 24/02/14.
 */
var spaceApp = angular.module('spaceApp');

spaceApp.controller("ChatController", function ($scope, $cookieStore, Profile, Contact, Spinner, $http, UserService, $firebase) {
    if (!UserService.loggedIn) {
        $scope.go('/login');
    } else {
    Profile.get(function (data, headers) {
        $scope.username = data.username;
    }, function (data, headers) {
        $scope.username = "";
    });
    var ref = new Firebase("https://amber-fire-3394.firebaseio.com/");
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
    }
});
