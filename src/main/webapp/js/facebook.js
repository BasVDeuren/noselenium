/**
 * Created with IntelliJ IDEA.
 * User: Dimi
 * Date: 11/02/14
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */

//window.fbAsyncInit = function() {
//    FB.init({
//        appId      : '649165391811913',
//        channelUrl : 'http://localhost.local/',
//        status     : true, // check login status
//        cookie     : true, // enable cookies to allow the server to access the session
//        xfbml      : true  // parse XFBML
//    });
//
//    FB.Event.subscribe('auth.authResponseChange', function(response) {
//        if (response.status === 'connected') {
//            testAPI();
//        } else if (response.status === 'not_authorized') {
//            FB.login();
//        } else {
//            FB.login();
//        }
//    });
//};
//
//// Load the SDK asynchronously
//(function(d){
//    var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
//    if (d.getElementById(id)) {return;}
//    js = d.createElement('script'); js.id = id; js.async = true;
//    js.src = "//connect.facebook.net/en_US/all.js";
//    ref.parentNode.insertBefore(js, ref);
//}(document));
//
//// Here we run a very simple test of the Graph API after login is successful.
//// This testAPI() function is only called in those cases.
//function testAPI() {
//    console.log('Welcome!  Fetching your information.... ');
//    FB.api('/me', function(response) {
//        console.log('Good to see you, ' + response.name + '.');
//    });
//}

window.fbAsyncInit = function() {
    FB.init({
        appId      : '649165391811913', // Set YOUR APP ID
        channelUrl : 'http://localhost.local/', // Channel File
        status     : true, // check login status
        cookie     : true, // enable cookies to allow the server to access the session
        xfbml      : true  // parse XFBML
    });

    FB.Event.subscribe('auth.authResponseChange', function(response)
    {
        if (response.status === 'connected')
        {
            document.getElementById("message").innerHTML +=  "<br>Connected to Facebook";
            //SUCCESS

        }
        else if (response.status === 'not_authorized')
        {
            document.getElementById("message").innerHTML +=  "<br>Failed to Connect";

            //FAILED
        } else
        {
            document.getElementById("message").innerHTML +=  "<br>Logged Out";

            //UNKNOWN ERROR
        }
    });

};

function Login()
{

    FB.login(function(response) {
        if (response.authResponse)
        {
            getUserInfo();
        } else
        {
            console.log('User cancelled login or did not fully authorize.');
        }
    },{scope: 'email,user_photos,user_videos'});

}

function getUserInfo() {
    FB.api('/me', function(response) {

        var str="<b>Name</b> : "+response.name+"<br>";
        str +="<b>Link: </b>"+response.link+"<br>";
        str +="<b>Username:</b> "+response.username+"<br>";
        str +="<b>id: </b>"+response.id+"<br>";
        str +="<b>Email:</b> "+response.email+"<br>";
        str +="<input type='button' value='Get Photo' onclick='getPhoto();'/>";
        str +="<input type='button' value='Logout' onclick='Logout();'/>";
        document.getElementById("status").innerHTML=str;

    });
}
function getPhoto()
{
    FB.api('/me/picture?type=normal', function(response) {

        var str="<br/><b>Pic</b> : <img src='"+response.data.url+"'/>";
        document.getElementById("status").innerHTML+=str;

    });

}
function Logout()
{
    FB.logout(function(){document.location.reload();});
}

// Load the SDK asynchronously
(function(d){
    var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
    if (d.getElementById(id)) {return;}
    js = d.createElement('script'); js.id = id; js.async = true;
    js.src = "//connect.facebook.net/en_US/all.js";
    ref.parentNode.insertBefore(js, ref);
}(document));