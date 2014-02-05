/**
 * Created by Dimi on 3/02/14.
 */

angular.module('spaceServices', ['ngResource'])
    .factory('Login', function ($resource) {
        return $resource('rest/accesstoken')
    })
    .factory('Register', function ($resource) {
        return $resource('rest/account')
    })
    ;