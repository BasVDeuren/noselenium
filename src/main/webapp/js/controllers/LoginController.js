/**
 * Created by Atheesan on 4/02/14.
 */
function LoginController($scope,Login,UserService,$cookieStore) {
    $scope.loginData = {
        username: "",
        password: ""
    };
    $scope.hasLoginFailed = false;
    $scope.login = function () {
         Login.save($scope.loginData, function(data,headers) {
             $cookieStore.put('accessToken',data.value);
             //alert($cookies.accessToken);
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
        if($scope.loginData.username != '' && $scope.loginData.password != ''){
            return false;
        }else{
            return true;
        }
    };


}