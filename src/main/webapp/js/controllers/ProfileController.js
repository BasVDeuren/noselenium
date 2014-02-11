/**
 * Created by Atheesan on 4/02/14.
 */
var spaceApp = angular.module('spaceApp');

spaceApp.controller("ProfileController", function ($scope, Profile,UserService) {
    $scope.registerData = {
        email: "",
        username: "",
        password: "",
        passwordRepeated: ""
    };
    $scope.hasRegistrationFailed = false;
    $scope.editUser = function () {
//        alert($scope.registerData.firstname + ' ' + $scope.registerData.lastname + ' ' + $scope.registerData.email + ' ' + $scope.registerData.username + ' ' + $scope.registerData.password);
        Profile.get(function (data, headers) {
            UserService.accessToken = data.value;
            $scope.go('/spacecrack/game');
            $scope.hasRegistrationFailed = false;
        }, function (data, headers) {
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
