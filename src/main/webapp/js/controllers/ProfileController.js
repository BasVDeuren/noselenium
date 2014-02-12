/**
 * Created by Atheesan on 4/02/14.
 */
var spaceApp = angular.module('spaceApp');

spaceApp.controller("ProfileController", function ($scope, $cookies, Profile) {
    $scope.registerData = {
        email: "",
        username: "BASSSS",
        password: "",
        passwordRepeated: ""
    };
    $scope.hasRegistrationFailed = false;
    $scope.editUser = function () {
//        alert($scope.registerData.firstname + ' ' + $scope.registerData.lastname + ' ' + $scope.registerData.email + ' ' + $scope.registerData.username + ' ' + $scope.registerData.password);
        Profile.get(function (data, headers) {
            $cookies.username = data.username;
            $cookies.email = data.email;
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
    };

    $scope.getUserData = function(data){
        Profile.get(function(data) {
            $scope.registerData = {
                email: data.email,
                username: data.username,
                password: data.password,
                passwordRepeated: data.password
            };
            alert("Succeeded" + $scope.registerData.username);
        },function(){
            alert("FAIL!!");
        });
    };
});
