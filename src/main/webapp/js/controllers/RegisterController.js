/**
 * Created by Atheesan on 4/02/14.
 */
var spaceApp = angular.module('spaceApp');

spaceApp.controller("RegisterController", function ($scope, Register, $cookieStore, Spinner) {
        $scope.registerData = {
            email: "",
            username: "",
            password: "",
            passwordRepeated: ""
        };
        $scope.hasRegistrationFailed = false;
        $scope.register = function () {
            Spinner.spinner.spin(Spinner.target);
            Register.save($scope.registerData, function (data) {
                Spinner.spinner.stop();
                $cookieStore.put('accessToken', data.value);
                $scope.go('/spacecrack/home');
                $scope.hasRegistrationFailed = false;
            }, function () {
                Spinner.spinner.stop();
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


//    $scope.fbRegister = function() {
//        FB.login(function(response) {
//            if (response.authResponse) {
//                var user;
//                FB.api('/me', function(response) {
//                    user = {
//                        email: response.email,
//                        username: response.name,
//                        password: 'facebook' + response.id,
//                        passwordRepeated: 'facebook' + response.id
//                    };
//
//                    Register.save(user, function (data, headers) {
//                        $cookieStore.put('accessToken',data.value);
//                        $scope.go('/spacecrack/home');
//                        $scope.hasRegistrationFailed = false;
//                    }, function (data, headers) {
//                        $scope.hasRegistrationFailed = true;
//                    });
//                });
//
//            } else {
//                console.log('User cancelled login or did not fully authorize.');
//            }
//        }, {scope: 'email'});
//    }

});
