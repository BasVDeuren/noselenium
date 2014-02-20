/**
 * Created by Dimi on 3/02/14.
 */

var spaceApp = angular.module('spaceApp', ['ngRoute', 'spaceServices', 'ngCookies', 'ngAnimate', 'pascalprecht.translate','ui.bootstrap','imageupload'])
    .config(appRouter);

//Navigation
function appRouter($routeProvider  ,$httpProvider) {

    var interceptor = ['$rootScope', '$q', '$location', function ( $rootScope, $q,$location) {
        function success(response) {
            return response;
        }

        function error(response, $scope) {
            var status = response.status;
            if($location.path() !=="/"){
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
        .when('/spacecrack/newgame', {
            templateUrl: 'partials/game.html',
            controller: 'GameController'
        })
        .when('/notFound', {
            templateUrl: 'partials/notFound.html'
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

spaceApp.controller("MainController", function ($scope, $cookies, $location, $timeout, $translate, UserService, $cookieStore, Login) {
    $scope.changeLanguage = function (key) {
        $translate.uses(key);
    };
    //site locatie wijzigen
    $scope.go = function (path) {
        $location.path(path);
    };

    $scope.isUserLoggedIn = function () {
        if ($cookieStore.get('accessToken') == null) {
            return false;
        } else {
            return true;
        }
    };


    $scope.logout = function () {
        Login.delete(function () {
            $cookieStore.remove('accessToken');
        }, function () {

        });

    }

});





