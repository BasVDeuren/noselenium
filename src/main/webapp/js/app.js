/**
 * Created by Dimi on 3/02/14.
 */

var spaceApp = angular.module('spaceApp', ['ngRoute', 'spaceServices', 'ngCookies', 'ngAnimate', 'pascalprecht.translate', 'ui.bootstrap', 'imageupload', 'firebase'])
    .config(appRouter);

//Navigation
function appRouter($routeProvider, $httpProvider) {

    var interceptor = ['$rootScope', '$q', '$location', function ($rootScope, $q, $location) {
        function success(response) {
            return response;
        }

        function error(response, $scope) {
            var status = response.status;
            if ($location.path() !== "/login") {
                if (status == 401) {

                    console.info("unauthorized");
                    console.info($location.path());
                    console.info("back to loginpage");
                    $location.path("/");

                    return;
                }
            }
            // otherwise
            return $q.reject(response);

        }

        return function (promise) {
            return promise.then(success, error);
        }

    }];
    $httpProvider.responseInterceptors.push(interceptor);

    $routeProvider
        .when('/', {
            templateUrl: 'partials/navhome.html',
            controller: 'NavHomeController'
        })
        .when('/login', {
            templateUrl: 'partials/login.html',
            controller: 'LoginController'
        })
        .when('/spacecrack/game', {
            templateUrl: 'partials/game.html',
            controller: 'BetterGameController'
        })
        .when('/spacecrack/register', {
            templateUrl: 'partials/register.html',
            controller: 'RegisterController'
        })
        .when('/spacecrack/editProfile', {
            templateUrl: 'partials/editProfile.html',
            controller: 'ProfileController'
        })
        .when('/spacecrack/home', {
            templateUrl: 'partials/navhome.html',
            controller: 'NavHomeController'
        })
        .when('/spacecrack/game/:gameId', {
            templateUrl: 'partials/game.html',
            controller: 'BetterGameController'
        })
        .when('/spacecrack/activegames', {
            templateUrl: 'partials/activeGames.html',
            controller: 'ActiveGamesController'
        })
        .when('/help', {
            templateUrl: 'partials/help.html'
        })
        .when('/notFound', {
            templateUrl: 'partials/notFound.html'
        })
        .when('/spacecrack/createGame', {
            templateUrl: 'partials/createGame.html',
            controller: 'createGameController'
        }).when('/spacecrack/chat', {
            templateUrl: 'partials/chat.html',
            controller: 'ChatController'
        });

    $routeProvider.otherwise({redirectTo: '/notFound'});
}

//Translation
spaceApp.config(['$translateProvider', function ($translateProvider) {
    $translateProvider.useStaticFilesLoader({
        prefix: 'json/languages/',
        suffix: '.json'
    });
    $translateProvider.preferredLanguage('en_US');
}]);

spaceApp.controller("MainController", function ($scope, $cookies, $location, $timeout, $translate, UserService, $cookieStore, Login, Profile) {
    $scope.changeLanguage = function (key) {
        $translate.uses(key);
    };
    //site locatie wijzigen
    $scope.go = function (path) {
        $location.path(path);
    };
    UserService.loggedIn = false;
    Profile.get(function () {
        UserService.loggedIn = true;
        return true;
    }, function () {
        UserService.loggedIn = false;
        return false;
    });
    $scope.isUserLoggedIn = function () {
//        if ($cookieStore.get('accessToken') == null) {
//            return false;
//        } else {
//            return true;
//        }
        return UserService.loggedIn;
    };


    $scope.logout = function () {
        Login.delete(function () {
            UserService.loggedIn = false;
            $cookieStore.remove('accessToken');
            $scope.go('/login');
        }, function () {

        });

    }

});





