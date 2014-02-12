/**
 * Created by Atheesan on 4/02/14.
 */
var spaceApp = angular.module('spaceApp');

spaceApp.controller("ProfileController", function ($scope, $cookies, Profile) {

    $scope.editUserData = {
        email: "",
        username: "",
        password: "",
        passwordRepeated: ""
    };


    Profile.get(function (data, headers) {
        $scope.editUserData.username = data.username;
        $scope.editUserData.email = data.email;
        $scope.editUserData.password = data.password;
        $scope.editUserData.passwordRepeated = data.password;
    }, function (data, headers) {
                alert('Failed!');
    });


    $scope.hasRegistrationFailed = false;
    $scope.editUser = function () {
        Profile.save($scope.editUserData,function () {

        }, function (data, headers) {

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

});
