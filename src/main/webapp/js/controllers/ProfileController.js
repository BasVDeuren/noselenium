/**
 * Created by Atheesan on 4/02/14.
 */
var spaceApp = angular.module('spaceApp');

spaceApp.controller("ProfileController", function ($scope, $cookieStore, Profile, Contact, Spinner) {

    /**Password**/

    $scope.editUserData = {
        email: "",
        username: "",
        password: "",
        passwordRepeated: ""
    };

    $scope.isSaveSuccess = false;
    $scope.isSaveDone = false;

    Profile.get(function (data, headers) {
        console.log("get api/auth/user");
        $scope.editUserData.username = data.username;
        $scope.editUserData.email = data.email;
    }, function (data, headers) {
    });


    $scope.hasRegistrationFailed = false;
    $scope.editUser = function () {
        Spinner.spinner.spin(Spinner.target);
        Profile.save($scope.editUserData, function () {
            Spinner.spinner.stop();
            console.log("post api/auth/user succeed");
            $scope.isSaveDone = true;
            $scope.isSaveSuccess = true;
        }, function (data, headers) {
            Spinner.spinner.stop();
            console.log("post api/auth/user failed");
            $scope.isSaveDone = true;
            $scope.isSaveSuccess = false;
        });
    };

    $scope.checkPassword = function (password1, password2) {
        return password1 == password2;
    };

    $scope.validateForm = function () {
        if ($scope.editUserData.password != '' && $scope.editUserData.passwordRepeated != ''
            && $scope.checkPassword($scope.editUserData.password, $scope.editUserData.passwordRepeated)) {
            return false;
        } else {
            return true;
        }
    };

    $scope.showErrorMsg = function () {

        return ($scope.isSaveDone && !$scope.isSaveSuccess);
    };
    $scope.showSuccesMsg = function () {
        return ($scope.isSaveDone && $scope.isSaveSuccess);
    };

//--------------------------------------------------------------------------------------------------
    /**CONTACT**/
        //Image Upload
    function convertImgToBase64(url, callback, outputFormat) {
        var canvas = document.createElement('CANVAS');
        var ctx = canvas.getContext('2d');
        var img = new Image;
        img.crossOrigin = 'Anonymous';
        img.onload = function () {
            canvas.height = img.height;
            canvas.width = img.width;
            ctx.drawImage(img, 0, 0);
            var dataURL = canvas.toDataURL(outputFormat || 'image/png');
            callback.call(this, dataURL);
            // Clean up
            canvas = null;
        };
        img.src = url;
    }

    $scope.contactData = {
        firstname: "",
        lastname: "",
        dayOfBirth: "",
        image: "",
        email: ""
    };
    $scope.convertedDate = {
        value: ""
    };

    $scope.isContactSaveSuccess = false;
    $scope.isContactSaveDone = false;
    $scope.showContactErrorMsg = function () {
        return ($scope.isContactSaveDone && !$scope.isContactSaveSuccess);
    };
    $scope.showContactSuccesMsg = function () {
        return ($scope.isContactSaveDone && $scope.isContactSaveSuccess);
    };

    Spinner.spinner.spin(Spinner.target);
    Contact.get(function (data) {
        Spinner.spinner.stop();
        $scope.contactData.firstname = data.firstname;
        $scope.contactData.lastname = data.lastname;
        $scope.convertedDate.value = new Date(data.dayOfBirth);
        $scope.contactData.image = data.image;
    }, function () {
        Spinner.spinner.stop();
    });

    $scope.editContact = function () {
        Spinner.spinner.spin(Spinner.target);
        $scope.contactData.dayOfBirth = $scope.convertedDate.value.toLocaleDateString();
        Contact.save($scope.contactData, function () {
            Spinner.spinner.stop();
            $scope.isContactSaveDone = true;
            $scope.isContactSaveSuccess = true;
        }, function () {
            Spinner.spinner.stop();
            $scope.isContactSaveDone = true;
            $scope.isContactSaveSuccess = false;
        })
    };


    $scope.today = function () {
        $scope.dt = new Date();
    };
    $scope.today();

    $scope.showWeeks = true;
    $scope.toggleWeeks = function () {
        $scope.showWeeks = !$scope.showWeeks;
    };

    $scope.clear = function () {
        $scope.dt = null;
    };


    $scope.toggleMin = function () {
        $scope.minDate = ( $scope.minDate ) ? null : new Date();
    };
    $scope.toggleMin();

    $scope.open = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
    };

    $scope.dateOptions = {
        'year-format': "'yy'",
        'starting-day': 1
    };

});
