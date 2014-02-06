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
        if($scope.checkPassword($scope.registerData.password,$scope.registerData.repeatPassword)){
            Register.save($scope.registerData, function(data,headers) {
                $scope.accesToken = data.key;
                $scope.go('/game');
            }, function(data,headers) {
                alert('Failed');
            });

        }

    };

    $scope.checkPassword = function (password1, password2) {
        return password1 == password2;
    }
});
