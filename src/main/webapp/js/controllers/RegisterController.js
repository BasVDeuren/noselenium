/**
 * Created by Atheesan on 4/02/14.
 */
var spaceApp = angular.module('spaceApp');

spaceApp.controller("RegisterController", function ($scope, Profile, UserService) {
    $scope.userData = {
        email: "",
        username: "",
        password: "",
        passwordRepeated: ""
    };
    $scope.hasRegistrationFailed = false;
    $scope.register = function () {
//        alert($scope.registerData.firstname + ' ' + $scope.registerData.lastname + ' ' + $scope.registerData.email + ' ' + $scope.registerData.username + ' ' + $scope.registerData.password);
        Profile.save($scope.userData, function (data) {
            UserService.username = data.username;
            UserService.email = data.email;
            UserService.password = data.password;
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
    }
});
