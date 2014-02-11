/**
 * Created by Atheesan on 3/02/14.
 */

angular.module('spaceServices', ['ngResource'])
    .factory('Login', function ($resource) {
        return $resource('/api/accesstokens')
    })
    .factory('Profile', function($resource){
        return $resource('/api/user')
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
    });

