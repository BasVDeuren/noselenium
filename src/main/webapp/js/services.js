/**
 * Created by Atheesan on 3/02/14.
 */

angular.module('spaceServices', ['ngResource'])
    .factory('Login', function ($resource) {
        return $resource('/api/accesstokens')
    })
    .factory('Register', function ($resource) {
        return $resource('/api/user')
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
