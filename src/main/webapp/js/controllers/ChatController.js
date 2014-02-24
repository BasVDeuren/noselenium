/**
 * Created by Atheesan on 24/02/14.
 */
var spaceApp = angular.module('spaceApp');

spaceApp.controller("ChatController", function ($scope, $cookieStore, Profile, Contact, Spinner, $http, UserService,$firebase) {
//    if (!UserService.loggedIn) {
//        $scope.go('/login');
//    } else {
    var ref = new Firebase("https://amber-fire-3394.firebaseio.com/");
    $scope.messages = $firebase(ref);
    $scope.addMessage = function(e) {
        if (e.keyCode != 13) return;
        $scope.messages.$add({from: $scope.name, body: $scope.msg});
        $scope.msg = "";
    };
    //}
});
