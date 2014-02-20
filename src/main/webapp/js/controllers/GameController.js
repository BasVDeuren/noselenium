/**
 * Created by Dimi on 3/02/14.
 */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("GameController", function ($scope, $translate, Map, Game, Action, $routeParams) {
    var game = new Phaser.Game(1120, 600, Phaser.AUTO, 'game', { preload: preload, create: create, update: update, render: render});

    $scope.gameIdFromParameter = $routeParams.gameId;
    console.log("game" + game);

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

    // actionType, ship, destinationPlanet
    $scope.action = { actionType: "", ship: {shipId: "", planetName: ""}, destinationPlanet: "" };
    function preload() {
        game.load.image('bg', 'assets/SpaceCrackBackground.jpg');
        game.load.image('button', 'assets/planet3.png');
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


    function create() {
        //move camara with cursors
        cursors = game.input.keyboard.createCursorKeys();

        // A simple background for our game
        var backgroundsprite = game.add.sprite(0, 0, 'bg');
        backgroundsprite.inputEnabled = true;

        var commandpointsText = game.add.text(5, 5, "Commandpoints: 5", { font: '50xp', fill: '#FF0000', backgroundColor: '#000000' }, sprites);
        commandpointsText.fixedToCamera = true;
        var button = game.add.button(5, 20, 'button', btnEndTurnClick, this, 2, 1, 0);
        button.fixedToCamera = true;

        backgroundsprite.events.onInputDown.add(backgroundlistener, this);
        var bgImg = game.cache.getImage('bg');
        console.log("width: " + bgImg.width + " height:" + bgImg.height);
        game.world.setBounds(0, 0, 1600, 1000);

        var graphics = game.add.graphics(0, 0);
        //graphics.beginFill(0x00FF00);
        graphics.lineStyle(3, 0x999999, 1);

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
                var planetSprite = new PlanetExtendedSprite(game, planets[i].x - width / 2, planets[i].y - height / 2, planets[i].name);
                $scope.planetSprites[i] = planetSprite;
                $scope.planetSpritesByLetter[planetSprite.name] = planetSprite;
                console.log("in get, planetSprite name: " + $scope.planetSprites[i].name);
                game.add.existing(planetSprite);

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
            function drawGame(data) {
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

                    // Add sprite
                    var image = game.cache.getImage('spaceship');
                    var width = image.width;
                    var height = image.height;

                    var player1shipsprite = new ShipExtendedSprite(game, planets[i].x - width / 2 + 10, planets[i].y - height / 2, player1ships[i]);
                    game.add.existing(player1shipsprite);


                    player1shipsprite.body.immovable = true;

                    player1shipsprite.inputEnabled = true;
                    player1shipsprite.events.onInputDown.add(spaceshipListener, this);
                }
            }

            Game.save(function (data) {
                drawGame.call(this, data);
            });
            Game.get()
        })
    }

    function render() {
        //debug helper
        game.debug.renderInputInfo(0, 0);
    }

    function update() {
        console.log("cursors", cursors);
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
        selectedSpaceShipSprite = spaceShipXSprite;

    }

    function planetListener(sprite) {
        console.log("clicked on planet: " + sprite.name);
        if (selectedSpaceShipSprite != null) {

            console.log("selectedShipSprite was not null!!");

            $scope.action.actionType = "MOVESHIP";

            $scope.action.destinationPlanet = sprite.name;
            $scope.action.ship = selectedSpaceShipSprite.ship;

            if ($scope.commandPoints > 0) {
                Action.save($scope.action, function () {
                    allPlanetsNormal();

                    game.physics.moveToXY(selectedSpaceShipSprite, sprite.center.x - 25 + xExtraByCamera, sprite.center.y - 10 + yExtraByCamera, 60, 1000);
                    setTimeout(function () {
                        selectedSpaceShipSprite.body.velocity.x = 0;
                        selectedSpaceShipSprite.body.velocity.y = 0;
                    }, 1000);
                    subtracktCommandPoints(1);
                    drawColonyFlag(sprite.name);
                    console.log("moveship was acceptable");
                    selectedSpaceShipSprite.ship.planetName = sprite.name;

                    spaceshipListener(selectedSpaceShipSprite);
                }, function () {
                    alert("moveship was unAcceptable");
                })
            } else {
                alert("You have no more commandpoints to use.");
            }
        }
    }

    function allPlanetsNormal() {
        for (var i = 0; i < $scope.planetSprites.length; i++) {
            $scope.planetSprites[i].loadTexture('planet1');
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


})
;