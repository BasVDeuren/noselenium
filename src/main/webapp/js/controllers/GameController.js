/**
 * Created by Dimi on 3/02/14.
 */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("GameController", function ($scope, $translate, Map, Game, Action, ActiveGame, $routeParams) {


    var game = new Phaser.Game(1120, 600, Phaser.AUTO, 'game', { preload: preload, create: create, update: update, render: render});
    console.log("game" + game);

   // $scope.activePlayerId = null;

    $scope.planetArray = [
        {name: "", x: "", y: "", connectedPlanets: [
            {name: ""}
        ]}
    ];
    $scope.game = {
        player1Id: "",
        player2Id: "",
        player1colonies: [
            {colonyId: "", planetName: ""}
        ],
        player1ships: [
            {shipId: "", planetName: ""}
        ],
        player2colonies: [
            {colonyId: "", planetName: ""}
        ],
        player2ships: [
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
    };
    PlanetExtendedSprite.prototype = Object.create(Phaser.Sprite.prototype);
    PlanetExtendedSprite.prototype.constructor = PlanetExtendedSprite;
    PlanetExtendedSprite.prototype.eligibleMove = false;
    PlanetExtendedSprite.renderOrderID = 0;

    $scope.planetSprites = [
        {planetXSprite: {planetSprite: null, name: ""}}
    ];

//extending sprite with ship so we can use it in the listener
    var ShipExtendedSprite = function (game, x, y, ship, playerId) {
        Phaser.Sprite.call(this, game, x, y, 'spaceship');
        this.ship = ship;
        this.playerId = playerId;
    };
    ShipExtendedSprite.prototype = Object.create(Phaser.Sprite.prototype);
    ShipExtendedSprite.prototype.constructor = ShipExtendedSprite;
    ShipExtendedSprite.prototype.previousPlanet = null;
    ShipExtendedSprite.renderOrderID = 2;

    // actionType, ship, destinationPlanet
    $scope.action = { actionType: "", ship: {shipId: "", planetName: ""}, destinationPlanet: "", playerId: ""};
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
        game.load.image('player2flag', 'assets/player2flag.png');
    }


    var selectedSpaceShipSprite = null;

    var cursors;
    var xExtraByCamera;
    var yExtraByCamera;
    var sprites;


    function create() {
        // A simple background for our game
        var backgroundsprite = game.add.sprite(0, 0, 'bg');
        backgroundsprite.inputEnabled = true;

        var commandpointsprite = game.add.sprite(0, 0);
        commandpointsprite.fixedToCamera = true;
        commandPointsText = game.add.text(125, 5, "Commandpoints: ", { font: '50px', fill: '#FF0000'}, sprites);
        commandpointsprite.addChild(commandPointsText);

        var button = game.add.button(5, 20, 'button', btnEndTurnClick, this, 2, 1, 0);
        button.fixedToCamera = true;

        backgroundsprite.events.onInputDown.add(backgroundlistener, this);
        var bgImg = game.cache.getImage('bg');
        console.log("width: " + bgImg.width + " height:" + bgImg.height);
        game.world.setBounds(0, 0, 1800, 1000);

        var graphics = game.add.graphics(0, 0);



        sprites = game.add.group();


        Map.get(function (data) {
            console.log(data.v);
            $scope.planetArray = data.planets;
            console.log("planetArray:" + $scope.planetArray[0].x);

            var planets = $scope.planetArray;

            // Draw connections first, rest on top
            graphics.lineStyle(3, 0x999999, 1);
            for (var i = 0; i < planets.length; i++) {
                var x = $scope.planetArray[i].x;
                var y = $scope.planetArray[i].y;

                // Draw planet connections (optimalisation possible)
                var connectedPlanets = $scope.planetArray[i].connectedPlanets;
                for (var j = 0; j < connectedPlanets.length; j++) {
                    var toX = connectedPlanets[j].x;
                    var toY = connectedPlanets[j].y;
                    graphics.moveTo(x, y);
                    graphics.lineTo(toX, toY);
                }
            }
            graphics.lineStyle(0);
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



                // Add planet name text
                var nameTag = game.add.text(0, 0, name, { font: '12px bold Arial', fill: 'white' }, sprites);
                nameTag.x = x - nameTag.width / 2;
                nameTag.y = y + height / 2;
                graphics.beginFill(0x000000);
                graphics.drawRect(nameTag.x - 5, nameTag.y, nameTag.width + 10, nameTag.height);
            }


            function drawPlayerShips(ships, playerId) {
                for (var i = 0; i < ships.length; i++) {

                    var ship = ships[i];
                    var planet = $scope.planetsByLetter[ship.planetName];
                    // Add sprite
                    var image = game.cache.getImage('spaceship');
                    var width = image.width;
                    var height = image.height;

                    var shipsprite = new ShipExtendedSprite(game, $scope.planetsByLetter[ships[i].planetName].x - width / 2 + 10, $scope.planetsByLetter[ships[i].planetName].y - height / 2, ships[i], playerId);
                    game.add.existing(shipsprite);


                    shipsprite.body.immovable = true;

                    shipsprite.inputEnabled = true;

                    if (playerId == $scope.activePlayerId) {
                        shipsprite.events.onInputDown.add(spaceshipListener, this);
                    }

                }
            }

            function applyGameData(data) {
                //  "$.player1.colonies[0].planet.name"
                $scope.game.player1Id = data.game.player1.playerId;
                $scope.game.player2Id = data.game.player2.playerId;

                $scope.activePlayerId = data.activePlayerId;
                if ($scope.activePlayerId == data.game.player1.playerId) {
                    $scope.commandPoints = data.game.player1.commandPoints;
                    console.log($scope.commandPoints);
                } else if ($scope.activePlayerId == data.game.player2.playerId) {
                    $scope.commandPoints = data.game.player2.commandPoints;
                }

                $scope.action.playerId = $scope.activePlayerId;
                $scope.game.player1ships = data.game.player1.ships;
                $scope.game.player1colonies = data.game.player1.colonies;
                $scope.game.player2ships = data.game.player2.ships;
                $scope.game.player2colonies = data.game.player2.colonies;

                //Create colonysprites
                drawColoniesOnPlanets($scope.game.player1colonies, 'player1flag');
                drawColoniesOnPlanets($scope.game.player2colonies, 'player2flag');
                drawPlayerShips($scope.game.player1ships, $scope.game.player1Id);
                drawPlayerShips($scope.game.player2ships, $scope.game.player2Id);
            }

            function drawColoniesOnPlanets(player1colonies, imageName) {
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
                    drawColonyFlag(planetName, imageName);
                }
                return planetName;
            }

            if ($routeParams.gameId == 0) {
                Game.save(function (data) {
                    applyGameData(data);
                })
            } else {
                ActiveGame.get({gameId: $routeParams.gameId}, function (data) {

                    applyGameData(data);
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

    function HighlightConnectedPlanets(spaceShipXSprite) {
        var arrayPlanet = $scope.planetsByLetter[spaceShipXSprite.ship.planetName];
        console.log(arrayPlanet.name);
        console.log("connectedPlanets: " + arrayPlanet.connectedPlanets);
        for (var j = 0; j < arrayPlanet.connectedPlanets.length; j++) {
            for (var k = 0; k < $scope.planetSprites.length; k++) {
                if (arrayPlanet.connectedPlanets[j].name == $scope.planetSprites[k].name) {
                    console.log($scope.planetSprites[k]);
                    $scope.planetSprites[k].loadTexture('planet1_small_selected');
                    $scope.planetSprites[k].eligibleMove = true;
                }
            }
        }
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

        updateCommandPoints();
    }

    function updateCommandPoints() {
        commandPointsText.setText("Commandpoints: " + $scope.commandPoints);
    }

    function render() {
        //debug helper
        game.debug.renderInputInfo(0, 0);
    }

//   functions
//   ---------
    function drawColonyFlag(planetName, imageName) {
        imageName = imageName || 'player1flag';
// Add sprite
        var image = game.cache.getImage(imageName);
        var width = image.width;
        var height = image.height;
        console.log("planetName: " + planetName);
        var planet = $scope.planetsByLetter[planetName];
        var player1flagsprite = sprites.create(planet.x - width / 2 + 10, planet.y - height / 2 - 24, imageName);
        player1flagsprite.renderOrderID = 1;
        player1flagsprite.body.immovable = true;
    }

    function substractCommandPoints(numberOfCommandPoints) {
        $scope.commandPoints = $scope.commandPoints - numberOfCommandPoints;
    }


    function spaceshipListener(spaceShipXSprite) {
        console.log("ship clicked!!" + spaceShipXSprite.ship.shipId);
        HighlightConnectedPlanets(spaceShipXSprite);
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
                    substractCommandPoints(1);

                    if (selectedSpaceShipSprite.playerId == $scope.game.player1Id) {
                        drawColonyFlag(planetXSprite.name, 'player1flag');
                    } else if (selectedSpaceShipSprite.playerId == $scope.game.player2Id) {
                        drawColonyFlag(planetXSprite.name, 'player2flag');
                    }

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

        }, function () {            alert("Something went wrong when ending your turn, please try again or wait for new commandpoints.");
        });
    }


});