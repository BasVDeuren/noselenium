/**
 * Created by Atheesan on 4/02/14.
 */
var spaceApp = angular.module('spaceApp');

spaceApp.controller("RegisterController",function($scope, Register){
    $scope.registerData = {
        email: "",
        username: "",
        password: "",
        repeatPassword: ""
    };

    $scope.register = function () {
//        alert($scope.registerData.firstname + ' ' + $scope.registerData.lastname + ' ' + $scope.registerData.email + ' ' + $scope.registerData.username + ' ' + $scope.registerData.password);
        Register.save($scope.registerData);
    };

    $scope.checkPassword = function (password1, password2) {
        return true;
    }
});
