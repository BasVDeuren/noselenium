/**
 * Created by Dimi on 3/02/14.
 */
function GameController ($scope, $translate,Map) {
    var game = new Phaser.Game(800, 500, Phaser.AUTO, 'game', { preload: preload, create: create, update: update, render: render});

    $scope.map = {
        planets: [{x:"",y:""}]
};
    function preload() {

        game.load.image('space', 'assets/space.jpg');
        game.load.image('planet1', 'assets/planet1.png');
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
    //    game.add.sprite(0, 0, 'space');


        Map.get(function(data,header){
          $scope.map = data.planets;
            console.log(data);

        });
console.log("map:" + $scope.map.planets[0].x);
        var graphics = game.add.graphics(0,0);
        graphics.beginFill(0x999999);
        graphics.lineStyle(3, 0x999999, 1);
        for(var planet in $scope.map.planets) {
            graphics.drawCircle(planet.x, planet.y, 5);
        }
       // platforms = game.add.group();

//        var x = 100;
//        var y = 100;
//        var planet1 = platforms.create(x, y, 'planet1');
//        planet1.body.immovable = true;
//        planet1.inputEnabled = true;
//        planet1.events.onInputDown.add(planetListener, this);
//        graphics.moveTo(x,y);
//
//
//        var x2 = 600;
//        var y2 = 100;
//        var planet2 = platforms.create(x2, y2, 'planet2');
//        planet2.body.immovable = true;
//        planet2.inputEnabled = true;
//        planet2.events.onInputDown.add(planetListener, this);
//
//        var planet3 = platforms.create(100, 400, 'planet3');
//        planet3.body.immovable = true;
//        planet3.inputEnabled = true;
//        planet3.events.onInputDown.add(planetListener, this);
//
//        var planet4 = platforms.create(500, 400, 'planet4');
//        planet4.body.immovable = true;
//        planet4.inputEnabled = true;
//        planet4.events.onInputDown.add(planetListener, this);
//
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