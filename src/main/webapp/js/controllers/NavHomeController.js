/**
 * Created by Janne on 7/02/14.
 */
var spaceApp = angular.module('spaceApp');

spaceApp.controller("NavHomeController", function ($scope,UserService) {
    //DIT LATEN STAAN AUB!!!!
    if (!UserService.loggedIn) {
        $scope.go('/login');
    } else {
    }
});