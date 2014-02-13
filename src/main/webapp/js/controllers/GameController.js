/**
 * Created by Dimi on 3/02/14.
 */
function GameController ($scope, $translate,Map) {
    var game = new Phaser.Game(800, 500, Phaser.AUTO, 'game', { preload: preload, create: create, update: update, render: render});

    /*$scope.map = {
     planets: [{x:"",y:""}]
     };*/
    $scope.planetArray = [{x:"",y:"",connectedPlanetNames:[{name:""}]}]
    function preload() {
        game.load.image('space', 'assets/space.jpg');
        game.load.image('planet1', 'assets/planet1_small.png');
        game.load.image('planet2', 'assets/planet2.png');
        game.load.image('planet3', 'assets/planet3.png');
        game.load.image('planet4', 'assets/planet4.png');
        game.load.image('spaceship', 'assets/spaceship.png');


    }

    var platforms;
    var score = 0;
    var scoreText;
    var spaceship;
    var spaceshipSelected = true;

    function create() {

        var platforms;

        //  A simple background for our game
        game.add.sprite(0, 0, 'space');


        Map.get(function(data,header){
            $scope.planetArray = data.planets;
            console.log("planetArray:" + $scope.planetArray[0].x);
            var graphics = game.add.graphics(0,0);
            //graphics.beginFill(0x00FF00);
            graphics.lineStyle(3, 0x00FF00, 1);

            // graphics.drawCircle($scope.map.planets[0].x,$scope.map.planets[0].y, 5);
            var platforms = game.add.group();

            var planets = $scope.planetArray;
            for(var i =0; i < planets.length ;i++) {

                console.log("i = ", i);
                var x = $scope.planetArray[i].x;
                var y = $scope.planetArray[i].y;
                console.log("planet X="+ x + ", Y=" + y);
                var image = game.cache.getImage('planet1');
                var width = image.width;
                var height = image.height;
                var planetSprite = platforms.create(x-width/2,y-height/2,'planet1');
                planetSprite.body.immovable = true;
                planetSprite.inputEnabled = true;
                planetSprite.events.onInputDown.add(planetListener, this);

            }
        });


//        spaceship = game.add.sprite(0, 0, 'spaceship');
//        //enables all kind of input actions on this image (click, etc)
//        spaceship.inputEnabled = true;
//        //spaceship.events.onInputDown.add(spaceshipListener, this);*/

    }

    function update() {

    }

    function render() {
        //debug helper
        game.debug.renderInputInfo(0, 0);
    }

    function spaceshipListener() {
        spaceshipSelected = !spaceshipSelected;
        if (spaceshipSelected) {
            alert('clicked');
        }
    }

    function planetListener(planet){
        if(spaceshipSelected){
            game.physics.moveToXY(spaceship,planet.center.x,planet.center.y,60,1000);
            setTimeout(function() {
                spaceship.body.velocity.x = 0;
                spaceship.body.velocity.y = 0;
            }, 1000);
        }
    }
}