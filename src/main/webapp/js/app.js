/**
 * Created by Dimi on 3/02/14.
 */

var spaceApp = angular.module('spaceApp', ['ngRoute', 'spaceServices', 'ngCookies', 'ngAnimate', 'pascalprecht.translate', 'ui.bootstrap', 'imageupload', 'firebase'])
    .config(appRouter);

//Navigation
function appRouter($routeProvider, $httpProvider) {

    var interceptor = ['$rootScope', '$q', '$location', function ($rootScope, $q, $location) {
        function success(response) {
            if (response.config.url.indexOf("/api/auth") > -1) {
                $rootScope.loggedInBool = true;
                $rootScope.$apply();
            }
            return response;
        }

        function error(response) {
            var status = response.status;
            if ($location.path() !== "/login") {
                if (status == 401) {
                    $rootScope.loggedInBool = false;
                    $rootScope.$apply();
                    console.info("unauthorized");
                    console.info($location.path());
                    console.info("back to loginpage");
                    $location.path("/login");

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
            controller: 'GameController'
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
            controller: 'GameController'
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
        }).when('/spacecrack/oldgame/:gameId', {
            templateUrl: 'partials/oldgame.html',
            controller: 'ReplayGameController'
        }).when('/spacecrack/matchhistory', {
            templateUrl: 'partials/matchhistory.html',
            controller: 'ActiveGamesController'
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

spaceApp.controller("MainController", function ($scope, $cookies, $location, $timeout, $translate, $cookieStore, Login, $rootScope) {
    $scope.changeLanguage = function (key) {
        $translate.uses(key);
    };
    //site locatie wijzigen
    $scope.go = function (path) {
        $location.path(path);
    };

    $scope.logout = function () {
        Login.delete(function () {
            $rootScope.loggedInBool = false;
            $cookieStore.remove('accessToken');
            $scope.go('/login');
        }, function () {

        });

    }

});

(function (d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s);
    js.id = id;
    js.src = "//connect.facebook.net/nl_NL/all.js#xfbml=1";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));




