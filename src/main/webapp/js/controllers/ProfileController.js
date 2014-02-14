/**
 * Created by Atheesan on 4/02/14.
 */
var spaceApp = angular.module('spaceApp');

spaceApp.controller("ProfileController", function ($scope, $cookies, Profile,Contact) {

    /**PAssword**/

    $scope.editUserData = {
        email: "",
        username: "",
        password: "",
        passwordRepeated: ""
    };


    Profile.get(function (data, headers) {
        $scope.editUserData.username = data.username;
        $scope.editUserData.email = data.email;
        $scope.editUserData.password = data.password;
        $scope.editUserData.passwordRepeated = data.password;
    }, function (data, headers) {
                alert('Failed!');
    });


    $scope.hasRegistrationFailed = false;
    $scope.editUser = function () {
        Profile.save($scope.editUserData,function () {

        }, function (data, headers) {
            alert('Save failed');
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

//--------------------------------------------------------------------------------------------------
    /**CONTACT**/
        //Image Upload
    function convertImgToBase64(url, callback, outputFormat){
        var canvas = document.createElement('CANVAS');
        var ctx = canvas.getContext('2d');
        var img = new Image;
        img.crossOrigin = 'Anonymous';
        img.onload = function(){
            canvas.height = img.height;
            canvas.width = img.width;
            ctx.drawImage(img,0,0);
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
        gender: "true",
        dateOfBirth: "",
        image: ""
    };

    $scope.editContact = function () {
        if($scope.contactData.gender == "true"){
            $scope.contactData.gender = true;
        }else{
            $scope.contactData.gender = false;
        }
        //$scope.contactData.dateOfBirth = $scope.contactData.dateOfBirth.toLocaleDateString();
        Contact.save($scope.contactData, function () {
            alert('save succeed');
        }, function () {


        })
    };





    //DatePicker
    $scope.today = function() {
        $scope.dt = new Date();
    };
    $scope.today();

    $scope.showWeeks = true;
    $scope.toggleWeeks = function () {
        $scope.showWeeks = ! $scope.showWeeks;
    };

    $scope.clear = function () {
        $scope.dt = null;
    };

//    // Disable weekend selection
//    $scope.disabled = function(date, mode) {
//        //return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
//        return false;
//    };

    $scope.toggleMin = function() {
        $scope.minDate = ( $scope.minDate ) ? null : new Date();
    };
    $scope.toggleMin();

    $scope.open = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
    };

    $scope.dateOptions = {
        'year-format': "'yy'",
        'starting-day': 1
    };

});
