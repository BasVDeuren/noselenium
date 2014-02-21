/**
 * Created by Atheesan on 4/02/14.
 */
function LoginController($scope, Login, Register, $cookieStore, Spinner, UserService) {
    //Loading Spinner

    if (UserService.loggedIn) {
        $scope.go('/');
    } else {
        $scope.startLoading = false;
        $scope.loginData = {
            email: "",
            password: ""
        };
        $scope.hasLoginFailed = false;
        $scope.alreadyRegistered = false;
        $scope.login = function () {
            Spinner.spinner.spin(Spinner.target);
            Login.save($scope.loginData, function (data, headers) {
                Spinner.spinner.stop();
                UserService.loggedIn = true;
                $cookieStore.put('accessToken', data.value);
                $scope.go('/spacecrack/home');
                $scope.hasLoginFailed = false;
            }, function (data, headers) {
                Spinner.spinner.stop();
                $scope.hasLoginFailed = true;
            });

        };

        $scope.validateLogin = function () {
            return !($scope.loginData.email != '' && $scope.loginData.password != '');
        };

        $scope.fbLogin = function () {
            Spinner.spinner.spin(Spinner.target);
            FB.login(function (response) {
                if (response.authResponse) {
                    var user;
                    console.log(response);
                    FB.api('/me', function (response) {
                        console.log(response);
                        user = {
                            email: response.email,
                            password: 'facebook' + response.id
                        };

                        Login.save(user, function (data, headers) {
                            Spinner.spinner.stop();
                            UserService.loggedIn = true;
                            $cookieStore.put('accessToken', data.value);
                            $scope.go('/spacecrack/home');
                            $scope.hasLoginFailed = false;
                        }, function (data, headers) {
                            Spinner.spinner.stop();
                            $scope.registerFB(response);
                        });
                    });

                } else {
                    Spinner.spinner.stop();
                    console.log('User cancelled login or did not fully authorize.');
                }
            }, {scope: 'email'});
        };

        $scope.registerFB = function (response) {
            Spinner.spinner.spin(Spinner.target);
            var user = {
                email: response.email,
                username: response.name,
                password: 'facebook' + response.id,
                passwordRepeated: 'facebook' + response.id
            };

            Register.save(user, function (data, headers) {
                Spinner.spinner.stop();
                UserService.loggedIn = true;
                $cookieStore.put('accessToken', data.value);
                $scope.go('/spacecrack/home');
                $scope.alreadyRegistered = false;
            }, function (data, headers) {
                Spinner.spinner.stop();
                $scope.alreadyRegistered = true;
            });
        };
    }
}
