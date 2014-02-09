/**
 * Created by Atheesan on 3/02/14.
 */

angular.module('spaceServices', ['ngResource'])
    .factory('Login', function ($resource) {
        return $resource('api/api/accesstokens')
    })
    .factory('Register', function ($resource) {
        return $resource('api/api/register')
    })
    .factory('UserService', function () {
        return {
            username: '',
            email: '',
            password: '',
            accessToken: null
        };
    });
;
