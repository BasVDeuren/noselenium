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
}

//Translation
spaceApp.config(['$translateProvider', function ($translateProvider) {
    $translateProvider.translations('en_US', {
        HELLO_WORLD: 'Hello World!',
        LOGIN: 'Login',
        USERNAME: "Username",
        PASSWORD: "Password",
        REGISTER: "Register",
        FIRSTNAME: "Firstname",
        LASTNAME: "Lastname",
        REPEAT_PASSWORD:"Repeat Password",
        REQUIRED:"Required",
        ERR_PASSWORD_MISMATCH:"Passwords don't match",
        ERR_LOGIN_FAILED:"Login Failed"
    });
    $translateProvider.translations('nl_NL', {
        HELLO_WORLD: 'Hallo Wereld!',
        LOGIN: 'Aanmelden',
        USERNAME: "Gebruikersnaam",
        PASSWORD: "Wachtwoord",
        REGISTER: "Registreer",
        FIRSTNAME: "Voornaam",
        LASTNAME: "Achternaam",
        REPEAT_PASSWORD:"Herhaal Wachtwoord",
        REQUIRED:"Verplicht",
        ERR_PASSWORD_MISMATCH:"Wachtwoorden komen niet overeen",
        ERR_LOGIN_FAILED:"Aanmelden mislukt"
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




