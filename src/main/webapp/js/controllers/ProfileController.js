/**
 * Created by Atheesan on 4/02/14.
 */
var spaceApp = angular.module('spaceApp');

spaceApp.controller("ProfileController", function ($scope, $http, Profile, UserService) {
    $scope.hasRegistrationFailed = false;
    $scope.editUser = function () {
       Profile.save($scope.userData, function (data) {
            UserService.email = data.email;
            UserService.username = data.username;
            UserService.password = data.password;
            UserService.passwordRepeated = data.password;
            UserService.accessToken = data.key;
            $scope.go('/spacecrack/game');
            $scope.hasRegistrationFailed = false;
        }, function () {
            $scope.hasRegistrationFailed = true;
        });
    };

    $scope.checkPassword = function (password1, password2) {
        return password1 == password2;
    };

    $scope.validateRegister = function () {
        if ($scope.registerData.email != '' && $scope.registerData.username != '' && $scope.registerData.password != '' && $scope.registerData.passwordRepeated != ''
            && $scope.checkPassword($scope.registerData.password, $scope.registerData.passwordRepeated)) {
            return false;
        } else {
            return true;
        }
    };

    $scope.initialiseUser = function(data){
        alert('test');
        //$http.defaults.headers.common['token'] = '{accesstokenId:1,value:123}';
        /*Profile.header('token',{accessTokenId:'1', value:'123'}).get(function(data){
            alert("BOTS!")
        });*/
        var config = {headers:  {
            'token': 'value:accesstoken'
        }
        };

        $http.get("/api/user", config);
    };
});
