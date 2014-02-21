/**
 * Created by Dimi on 3/02/14.
 */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("GameController", function ($scope, $translate, Map, Game, Action, ActiveGame, $routeParams) {
    var game = new Phaser.Game(1120, 600, Phaser.AUTO, 'game', { preload: preload, create: create, update: update, render: render});
    console.log("game" + game);
    /*$scope.map = {
     planets: [{x:"",y:""}]
     };*/
    $scope.planetArray = [
        {name: "", x: "", y: "", connectedPlanets: [
            {name: ""}
        ]}
    ];
    $scope.game = {
        player1colonies: [
            {colonyId: "", planetName: ""}
        ],
        player1ships: [
            {shipId: "", planetName: ""}
        ]
    };
    $scope.changedPlanetSprites = [
        {planetXSprite: {planetSprite: null, name: ""}}
    ];

    $scope.planetsByLetter = [
        {name: "", x: "", y: "", connectedPlanets: [
            {name: ""}
        ]}
    ];
    $scope.planetSpritesByLetter = [];

    $scope.commandPoints = "";
//extending sprite with planet name so we can use it in the listener
    var PlanetExtendedSprite = function (game, x, y, name) {
        Phaser.Sprite.call(this, game, x, y, 'planet1');
        this.name = name;
    }
    PlanetExtendedSprite.prototype = Object.create(Phaser.Sprite.prototype);
    PlanetExtendedSprite.prototype.constructor = PlanetExtendedSprite;
    PlanetExtendedSprite.prototype.eligibleMove = false;

    $scope.planetSprites = [
        {planetXSprite: {planetSprite: null, name: ""}}
    ];

//extending sprite with ship so we can use it in the listener
    var ShipExtendedSprite = function (game, x, y, ship) {
        Phaser.Sprite.call(this, game, x, y, 'spaceship');
        this.ship = ship;
    }
    ShipExtendedSprite.prototype = Object.create(Phaser.Sprite.prototype);
    ShipExtendedSprite.prototype.constructor = ShipExtendedSprite;
    ShipExtendedSprite.prototype.previousPlanet = null;

    // actionType, ship, destinationPlanet
    $scope.action = { actionType: "", ship: {shipId: "", planetName: ""}, destinationPlanet: "" };
    function preload() {
        game.load.image('bg', 'assets/SpaceCrackBackground.jpg');
        game.load.image('button', 'assets/endturn.png');
        game.load.image('planet1', 'assets/planet1_small.png');
        game.load.image('planet2', 'assets/planet2.png');
        game.load.image('planet3', 'assets/planet3.png');
        game.load.image('planet1_small_selected', 'assets/planet1_small_selected.png');
        game.load.image('planet4', 'assets/planet4.png');
        game.load.image('spaceship', 'assets/spaceship.png');
        game.load.image('player1flag', 'assets/player1flag.png');
    }


    var selectedSpaceShipSprite = null;

    var cursors;
    var xExtraByCamera;
    var yExtraByCamera;
    var sprites;
    var commandPointsText;

    function create() {
        // A simple background for our game
        var backgroundsprite = game.add.sprite(0, 0, 'bg');
        backgroundsprite.inputEnabled = true;

        var commandpointsprite = game.add.sprite(0,0);
        commandpointsprite.fixedToCamera = true;
        commandPointsText = game.add.text(125, 5, "Commandpoints: ", { font: '50px', fill: '#FF0000'}, sprites);
        commandpointsprite.addChild(commandPointsText);

        var button = game.add.button(5, 20, 'button', btnEndTurnClick, this, 2, 1, 0);
        button.fixedToCamera = true;

        backgroundsprite.events.onInputDown.add(backgroundlistener, this);
        var bgImg = game.cache.getImage('bg');
        console.log("width: " + bgImg.width + " height:" + bgImg.height);
        game.world.setBounds(0, 0, 1600, 1000);

        var graphics = game.add.graphics(0, 0);
        //graphics.beginFill(0x00FF00);
        graphics.lineStyle(3, 0x999999, 1);

        // graphics.drawCircle($scope.map.planets[0].x,$scope.map.planets[0].y, 5);
        sprites = game.add.group();


        Map.get(function (data, header) {
            $scope.planetArray = data.planets;
            console.log("planetArray:" + $scope.planetArray[0].x);

            var planets = $scope.planetArray;
            for (var i = 0; i < planets.length; i++) {
                $scope.planetsByLetter[planets[i].name] = planets[i];

                var name = $scope.planetArray[i].name;
                var x = $scope.planetArray[i].x;
                var y = $scope.planetArray[i].y;

                // Add sprite
                var image = game.cache.getImage('planet1');
                var width = image.width;
                var height = image.height;
                var planetSprite = new PlanetExtendedSprite(game, planets[i].x - width / 2, planets[i].y - height / 2, planets[i].name, false);
                $scope.planetSprites[i] = planetSprite;
                $scope.planetSpritesByLetter[planetSprite.name] = planetSprite;
                console.log("in get, planetSprite name: " + $scope.planetSprites[i].name);
                game.add.existing(planetSprite);
//                var planetSprite = sprites.create(x - width / 2, y - height / 2, 'planet1');
                planetSprite.body.immovable = true;
                planetSprite.inputEnabled = true;
                planetSprite.events.onInputDown.add(planetListener, this);

                // Add text
                var text = game.add.text(x - width / 2, y - height / 2, name, { font: '16px', fill: '#ff0000' }, sprites);

                // Draw planet connections (optimalisation possible)

                var connectedPlanets = $scope.planetArray[i].connectedPlanets;
                for (var j = 0; j < connectedPlanets.length; j++) {
                    var toX = connectedPlanets[j].x;
                    var toY = connectedPlanets[j].y;
                    graphics.moveTo(x, y);
                    graphics.lineTo(toX, toY);
                }
            }
            function applyGameData(data) {
                //  "$.player1.colonies[0].planet.name"
                $scope.commandPoints = data.player1.commandPoints;
                console.log($scope.commandPoints);

                $scope.game.player1ships = data.player1.ships;
                $scope.game.player1colonies = data.player1.colonies;
                var player1colonies = $scope.game.player1colonies;
                //Create colonysprites
                for (var i = 0; i < player1colonies.length; i++) {
                    var x;
                    var y;
                    var planetName = player1colonies[i].planetName;

                    for (j = 0; j < $scope.planetArray.length; j++) {
                        var planet = $scope.planetArray[j];
                        if (planet.name === planetName) {
                            x = planet.x;
                            y = planet.y;
                        }
                    }
                    drawColonyFlag(planetName);
                }

                var player1ships = $scope.game.player1ships;
                for (var i = 0; i < player1ships.length; i++) {
                    var x;
                    var y;
                    for (j = 0; j < $scope.planetArray.length; j++) {
                        var planet = $scope.planetArray[j];
                        if (planet.name === planetName) {
                            x = planet.x;
                            y = planet.y;
                        }
                    }
//                    var x = player1ships[i].planet.x;
//                    var y =player1ships[i].planet.y;
                    // Add sprite
                    var image = game.cache.getImage('spaceship');
                    var width = image.width;
                    var height = image.height;

                    var player1shipsprite = new ShipExtendedSprite(game, $scope.planetsByLetter[player1ships[i].planetName].x - width / 2 + 10, $scope.planetsByLetter[player1ships[i].planetName].y - height / 2, player1ships[i]);
                    game.add.existing(player1shipsprite);


                    player1shipsprite.body.immovable = true;

                    player1shipsprite.inputEnabled = true;
                    player1shipsprite.events.onInputDown.add(spaceshipListener, this);
                }
            }

            if ($routeParams.gameId == 0) {
                Game.save(function (data) {
                    applyGameData(data);
                })
            } else {
                ActiveGame.get({gameId: $routeParams.gameId}, function (data) {
                    alert("SUCCESS!!");
                    applyGameData(data);
                }, function () {
                    alert("FAIL!!");
                })
            }
        });


        // Move camera with cursors
        cursors = game.input.keyboard.createCursorKeys();

//        spaceship = game.add.sprite(0, 0, 'spaceship');
//        //enables all kind of input actions on this image (click, etc)
//        spaceship.inputEnabled = true;
//        //spaceship.events.onInputDown.add(spaceshipListener, this);*/

    }

    function update() {
        if (cursors.up.isDown) {
            game.camera.y -= 4;
        } else if (cursors.down.isDown) {
            game.camera.y += 4;
        }
        if (cursors.left.isDown) {
            game.camera.x -= 4;
        } else if (cursors.right.isDown) {
            game.camera.x += 4;
        }
        yExtraByCamera = game.camera.y;
        xExtraByCamera = game.camera.x;

        functieCommandPoints();
    }

    function functieCommandPoints(){
        commandPointsText.setText("Commandpoints: " + $scope.commandPoints);
    }

    function render() {
        //debug helper
        game.debug.renderInputInfo(0, 0);
    }

//   functions
//   ---------
    function drawColonyFlag(planetName) {
// Add sprite
        var image = game.cache.getImage('player1flag');
        var width = image.width;
        var height = image.height;
        console.log("planetName: " + planetName);
        var planet = $scope.planetsByLetter[planetName];
        var player1flagsprite = sprites.create(planet.x - width / 2 + 10, planet.y - height / 2 - 24, 'player1flag');

        player1flagsprite.body.immovable = true;
    }

    function subtracktCommandPoints(numberOfActionCommandPoints) {
        $scope.commandPoints = $scope.commandPoints - numberOfActionCommandPoints;
    }

    function changePlanetOnSpaceShipClicked(spaceShipXSprite, i) {
        if (spaceShipXSprite.ship.planetName == $scope.planetArray[i].name) {
            console.log("planet found in planetArray " + spaceShipXSprite.ship.planetName + " " + $scope.planetArray[i].name);
            var arrayPlanet = $scope.planetArray[i];
            console.log(arrayPlanet.name);
            console.log("connectedPlanets: " + arrayPlanet.connectedPlanets);
            for (var j = 0; j < $scope.planetArray[i].connectedPlanets.length; j++) {
                for (var k = 0; k < $scope.planetSprites.length; k++) {
                    if (arrayPlanet.connectedPlanets[j].name == $scope.planetSprites[k].name) {
                        console.log($scope.planetSprites[k]);
                        $scope.planetSprites[k].loadTexture('planet1_small_selected');
                        $scope.planetSprites[k].eligibleMove = true;
                    }
                }
            }

        }
    }

    function spaceshipListener(spaceShipXSprite) {
        console.log("ship clicked!!" + spaceShipXSprite.ship.shipId);
        for (var i = 0; i < $scope.planetArray.length; i++) {
            var change = changePlanetOnSpaceShipClicked(spaceShipXSprite, i);

        }
        spaceShipXSprite.previousPlanet = spaceShipXSprite.ship.planetName;
        selectedSpaceShipSprite = spaceShipXSprite;

    }

    function moveToPlanetSprite(planetXSprite) {
        game.input.disabled = true;
        game.physics.moveToXY(selectedSpaceShipSprite, planetXSprite.center.x - 25 + xExtraByCamera, planetXSprite.center.y - 10 + yExtraByCamera, 60, 1000);
        setTimeout(function () {
            selectedSpaceShipSprite.body.velocity.x = 0;
            selectedSpaceShipSprite.body.velocity.y = 0;
            game.input.disabled = false;
        }, 1000);
    }

    function planetListener(planetXSprite) {
        console.log("clicked on planet: " + planetXSprite.name);
        if (selectedSpaceShipSprite != null) {

            console.log("selectedShipSprite was not null!!");

            $scope.action.actionType = "MOVESHIP";

            $scope.action.destinationPlanet = planetXSprite.name;
            $scope.action.ship = selectedSpaceShipSprite.ship;

            if ($scope.commandPoints > 0) {
                if (planetXSprite.eligibleMove) {
                    allPlanetsNormal();

                    moveToPlanetSprite(planetXSprite);
                    subtracktCommandPoints(1);

                    drawColonyFlag(planetXSprite.name);
                    console.log("moveship was acceptable");


                    Action.save($scope.action, function () {
                        selectedSpaceShipSprite.ship.planetName = planetXSprite.name;

                        spaceshipListener(selectedSpaceShipSprite);
                    }, function () {
                        alert("moveship was unAcceptable");
                        moveToPlanetSprite(selectedSpaceShipSprite.previousPlanet);
                    })
                }
            } else {
                alert("You have no more commandpoints to use. Click end turn to refill your command points.");
            }
        }
    }

    function allPlanetsNormal() {
        for (var i = 0; i < $scope.planetSprites.length; i++) {
            $scope.planetSprites[i].loadTexture('planet1');
            $scope.planetSprites[i].eligibleMove = false;
        }
    }

    function backgroundlistener() {
        console.log("clicked on background!");
        selectedSpaceShipSprite = null;
        allPlanetsNormal();
    }

    function btnEndTurnClick() {
        console.log("clicked on end turn");
        $scope.action.actionType = "ENDTURN";
        Action.save($scope.action, function () {
            alert("You have ended your turn, wait until you receive new commandpoints.");
        }, function () {
            alert("Something went wrong when ending your turn, please try again or wait for new commandpoints.");
        });
    }


});