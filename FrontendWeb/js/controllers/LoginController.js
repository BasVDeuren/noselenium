/**
 * Created by Atheesan on 4/02/14.
 */
function LoginController($scope,Login) {
    $scope.loginData = {
        username: "",
        password: ""
    };

    $scope.login = function () {
//         Login.save($scope.loginData, function(data,headers) {
//             $scope.accesToken = data.key;
//             $scope.go('/game');
//         }, function(data,headers) {
//             alert("failed");
//         });

        if($scope.loginData.username == 'test' && $scope.loginData.password == 'test'){
            $scope.go('/game');
        }
    };

    $scope.validateFields = function(){
        if($scope.loginData.username != ''){
            return true;
        }
    }

}