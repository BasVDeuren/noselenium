/**
 * Created by Atheesan on 3/02/14.
 */

//angular.module('myApp.services', ['ngResource']).
//    factory('CommentSvc', function ($resource) {
//        return $resource('/cakephp/demo_comments/:action/:id/:page/:limit', { id:'@ID', 'page' : '@page', 'limit': '@limit' }, {
//            'query' : { method: 'GET', params: { action : 'index' }, isArray: true },
//            'save': { method: 'PUT', params: { action: 'edit' } },
//            'add': { method: 'POST', params: { action: 'add' } },
//            'delete': { method: 'DELETE', params: { action: 'delete' } } // since delete is reserved word, don't fight it just use remove instead
//        });
//    });

angular.module('spaceServices', ['ngResource'])
    .factory('Login', function ($resource) {
        return $resource('/api/accesstokens')
    })
    .factory('Register', function($resource){
        return $resource('/api/user')
    })

    .factory('Profile',function($resource){
        return $resource('/api/user/auth')
    })
//    .factory('Room', function ($resource) {
//        return $resource('rooms/:roomName', {roomName: '@roomName'}, {'get': { method: 'GET', params: {roomName: '@roomName'}, format: 'json', isArray: false }})
//    })
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

