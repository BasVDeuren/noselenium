/**
 * Created by Atheesan on 4/02/14.
 */
function LoginController($scope,Login) {
    $scope.loginData = {
        username: "",
        password: ""
    };

    $scope.login = function () {
//        alert($scope.loginData.username +' ' +$scope.loginData.password);
         Login.save($scope.loginData);
    }


}