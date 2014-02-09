/**
 * Created by Dimi on 3/02/14.
 */

var spaceApp = angular.module('spaceApp',['ngRoute', 'spaceServices', 'ngCookies', 'ngAnimate', 'pascalprecht.translate'])
    .config(appRouter);

//Navigation
function appRouter($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'partials/login.html',
            controller: 'LoginController'
        })
        .when('/spacecrack/game', {
            templateUrl: 'partials/game.html',
            controller: 'GameController'
        })
        .when('/spacecrack/register', {
            templateUrl: 'partials/register.html',
            controller: 'RegisterController'
        })
        .when('/spacecrack/home', {
            templateUrl: 'partials/navhome.html',
            controller: 'NavHomeController'
        })
        .when('/spacecrack/activegames', {
            templateUrl: 'partials/game.html',
            controller: 'GameController'
        })
}

//Translation
spaceApp.config(['$translateProvider', function ($translateProvider) {
    $translateProvider.useStaticFilesLoader({
        prefix: 'json/languages/',
        suffix: '.json'
    });
    $translateProvider.preferredLanguage('en_US');
}]);

spaceApp.controller("MainController", function ($scope, $cookies, $location, $timeout, $translate) {
    $scope.changeLanguage = function (key) {
        $translate.uses(key);
    };
    //site locatie wijzigen
    $scope.go = function (path) {
        $location.path(path);
    };

});




