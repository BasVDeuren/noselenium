/**
 * Created by Atheesan on 4/02/14.
 */
function LoginController($scope,Login,Register,UserService,$cookieStore) {
    $scope.loginData = {
        username: "",
        password: ""
    };
    $scope.hasLoginFailed = false;
    $scope.alreadyRegistered = false;
    $scope.login = function () {
         Login.save($scope.loginData, function(data,headers) {
             $cookieStore.put('accessToken',data.value);
             $scope.go('/spacecrack/home');
             $scope.hasLoginFailed = false;
         }, function(data,headers) {
             $scope.hasLoginFailed = true;
         });

//        if($scope.loginData.username == 'test' && $scope.loginData.password == 'test'){
//            $scope.go('/game');
//        }
    };

    $scope.validateLogin = function(){
        return !($scope.loginData.username != '' && $scope.loginData.password != '');
    };

    $scope.fbLogin = function() {
        FB.login(function(response) {
            if (response.authResponse) {
                var user;
                console.log(response);
                FB.api('/me', function(response) {
                    console.log(response);
                    user = {
                        username: response.name,
                        password: 'facebook' + response.id
                    };

                    Login.save(user, function(data,headers) {
                        $cookieStore.put('accessToken',data.value);
                        $scope.go('/spacecrack/home');
                        $scope.hasLoginFailed = false;
                    }, function(data,headers) {
                        var user = {
                                email: response.email,
                                username: response.name,
                                password: 'facebook' + response.id,
                                passwordRepeated: 'facebook' + response.id
                            };

                            Register.save(user, function (data, headers) {
                                $cookieStore.put('accessToken',data.value);
                                $scope.go('/spacecrack/home');
                                $scope.alreadyRegistered = false;
                            }, function (data, headers) {
                                $scope.alreadyRegistered = true;
                            });
                    });
                });

            } else {
                console.log('User cancelled login or did not fully authorize.');
            }
        }, {scope: 'email'});
    }


}