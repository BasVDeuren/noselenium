/**
 * Created by Atheesan on 3/02/14.
 */


angular.module('spaceServices', ['ngResource'])
    .factory('Login', function ($resource) {
        return $resource('/api/accesstokens')
    })
    .factory('Register', function($resource){
        return $resource('/api/user')
    })
    .factory('Profile',function($resource){
        return $resource('/api/auth/user')
    })
    .factory('Contact',function($resource){
        return $resource('/api/auth/profile')
    })
    .factory('UserService', function () {
        return {
            username: '',
            email: '',
            password: '',
            accessToken: null
        };
    }).factory('Map', function($resource)
    {
        return $resource('/api/map')
    }).factory('Game', function($resource)
    {
        return $resource('/api/auth/game')
    }).factory('Action', function($resource)
    {
        return $resource('/api/auth/action')
    });

