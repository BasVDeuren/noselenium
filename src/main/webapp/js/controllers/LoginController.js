/**
 * Created by Atheesan on 4/02/14.
 */
function LoginController($scope, Login, Register, $cookieStore, Spinner, Contact) {

    //data klaar zetten
    $scope.contactData = {
        firstname: "",
        lastname: "",
        dayOfBirth: "",
        image: "",
        email: ""
    };

    Contact.get(function (data) {
        $scope.contactData.firstname = data.firstname;
        $scope.contactData.lastname = data.lastname;
        $scope.convertedDate.value = new Date(data.dayOfBirth);
        $scope.contactData.image = data.image;
        $scope.contactData.email = data.email;
    }, function () {
    });

    //Loading Spinner
    $scope.startLoading = false;
    $scope.loginData = {
        email: "",
        password: ""
    };
    $scope.hasLoginFailed = false;
    $scope.alreadyRegistered = false;
    $scope.login = function () {
        Spinner.spinner.spin(Spinner.target);
        Login.save($scope.loginData, function (data) {
            Spinner.spinner.stop();
            $cookieStore.put('accessToken', data.value);
            $scope.go('/');
            $scope.hasLoginFailed = false;
        }, function () {
            Spinner.spinner.stop();
            $scope.hasLoginFailed = true;
        });

    };

    $scope.validateLogin = function () {
        return !($scope.loginData.email != '' && $scope.loginData.password != '');
    };

    $scope.fbLogin = function () {
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
                    Spinner.spinner.spin(Spinner.target);
                    Login.save(user, function (data) {
                        Spinner.spinner.stop();
                        $cookieStore.put('accessToken', data.value);
                        $scope.updateFbProfile(response);
                        $scope.go('/');
                        $scope.hasLoginFailed = false;
                    }, function () {
                        Spinner.spinner.stop();
                        $scope.registerFB(response);
                    });
                });
            } else {
//                    Spinner.spinner.stop();
                console.log('User cancelled login or did not fully authorize.');
            }
        }, {scope: 'email, user_birthday, user_photos, read_friendlists'});
    };

    $scope.registerFB = function (response) {
        Spinner.spinner.spin(Spinner.target);
        var user = {
            email: response.email,
            username: response.name,
            password: 'facebook' + response.id,
            passwordRepeated: 'facebook' + response.id
        };

        Register.save(user, function (data) {
            Spinner.spinner.stop();
            $cookieStore.put('accessToken', data.value);
            $scope.updateFbProfile(response);
            $scope.go('/');
            $scope.alreadyRegistered = false;
        }, function () {
            Spinner.spinner.stop();
            $scope.alreadyRegistered = true;
        });
    };

    //Update profile with facebook data

    $scope.updateFbProfile = function (response) {
        FB.api("/me/picture", function(response){
            $scope.contactData.image= response.data.url;
        });
        FB.api("/me/birthday", function(response){
            $scope.contactData.dayOfBirth= response.birthday;
        });
        $scope.contactData.firstname=response.name;
        //$scope.contactData.dayOfBirth="1-1-1970";
        alert($scope.contactData.firstname );
        alert($scope.contactData.dayOfBirth);

        Contact.save($scope.contactData, function () {
        }, function () {
        })
    }
}