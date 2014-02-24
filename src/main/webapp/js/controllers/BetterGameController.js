/**
 * Created by Tim on 24/02/14.
 */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("BetterGameController", function ($scope, $translate, Map, Game, Action, ActiveGame,$route, $routeParams, UserService) {
    //region Declarations
    var game = new Phaser.Game(1120, 600, Phaser.AUTO, 'game', { preload: preload, create: create, update: update, render: render});
    var cursors;
    var xExtraByCamera;
    var yExtraByCamera;
    var activePlayerFirebase;
    var opponentFireBase;

    var planetGroup;
    var shipGroup;
    var colonyGroup;
    $scope.planetXSpritesByLetter = [];
    $scope.shipXSpritesById = [];
    $scope.activePlayerId = "";
    $scope.opponentPlayerId = "";
    $scope.selectedSpaceShipXSprite = "";
    $scope.activePlayerColonyImage = "";

    var PlanetExtendedSprite = function (game, x, y, planet) {
        var image = game.cache.getImage('planet1');
        var width = image.width;
        var height = image.height;
        Phaser.Sprite.call(this, game, x - width / 2, y - height / 2, 'planet1');
        this.planet = planet;
        this.inputEnabled = true;
        this.body.immovable = true;
        this.events.onInputDown.add(planetListener, this);
    }
    PlanetExtendedSprite.prototype = Object.create(Phaser.Sprite.prototype);
    PlanetExtendedSprite.prototype.constructor = PlanetExtendedSprite;
    PlanetExtendedSprite.prototype.eligibleMove = false;


    var ShipExtendedSprite = function (game, ship, playerId, image) {
        var planetName = ship.planetName;
        var planet = $scope.planetXSpritesByLetter[planetName].planet;
        Phaser.Sprite.call(this, game, planet.x, planet.y - 15, image);
        this.ship = ship;
        this.playerId = playerId;
        this.inputEnabled = true;
        this.body.immovable = true;
        if (playerId == $scope.activePlayerId) {
            this.events.onInputDown.add(spaceshipListener, this);
        }
    }

    ShipExtendedSprite.prototype = Object.create(Phaser.Sprite.prototype);
    ShipExtendedSprite.prototype.constructor = ShipExtendedSprite;
    ShipExtendedSprite.prototype.previousPlanet = null;
//Template Function for doing actions and pushing them to the server and the opponent's client!
    function doAction(action, frontEndCondition , frontEndLogic)
    {
        if(frontEndCondition)
        {
           frontEndLogic();
           Action.save(action, function(){
                activePlayerFirebase.push(action);
           }, function()
           {
               create();
           });
        }
    }




    var ColonyExtendedSprite = function (game, planetName, playerId, image) {
        this.planetName = planetName;
        var planet = $scope.planetXSpritesByLetter[planetName].planet;
        Phaser.Sprite.call(this, game, planet.x - 5, planet.y - 32, image);
        this.planet = planet
    };
    ColonyExtendedSprite.prototype = Object.create(Phaser.Sprite.prototype);
    ColonyExtendedSprite.prototype.constructor = ColonyExtendedSprite;
    //endregion

    //region Lifecycle Methods
    function preload() {
        game.load.image('bg', 'assets/SpaceCrackBackground.jpg');
        game.load.image('button', 'assets/endturn.png');
        game.load.image('planet1', 'assets/planet1_small.png');
        game.load.image('planet1_selected', 'assets/planet1_small_selected.png');
        game.load.image('spaceship', 'assets/spaceship.png');
        game.load.image('player1flag', 'assets/player1flag.png');
        game.load.image('player2flag', 'assets/player2flag.png');
    }




    function create() {
        drawGame();
        console.log("activeplayerid: " + $scope.activePlayerId);
        console.log("opponentid: " + $scope.opponentPlayerId);
        activePlayerFirebase = new Firebase('https://vivid-fire-9476.firebaseio.com/player' + $scope.activePlayerId);
        opponentFireBase = new Firebase('https://vivid-fire-9476.firebaseio.com/player' + $scope.opponentPlayerId);
        addFirebaseListener();
        game.world.setBounds(0, 0, 1600, 1000);
        cursors = game.input.keyboard.createCursorKeys();
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
        xExtraByCamera = game.camera.x;
        yExtraByCamera = game.camera.y;

    }

    function render() {

    }

    //endregion


    function drawGame() {
        var backgroundsprite = game.add.sprite(0, 0, 'bg');
        backgroundsprite.inputEnabled = true;
        var graphics = game.add.graphics(0, 0);
        graphics.lineStyle(3, 0x999999, 1);

        Map.get(function (data) {

            //assign planets to their sprites;
            drawPlanets(data);
            drawConnections();
            ActiveGame.get({gameId: $routeParams.gameId}, applyGameData);
        });

        function drawPlanets(data) {
            planetGroup = game.add.group();
            shipGroup = game.add.group();
            colonyGroup = game.add.group();
            var planetArray = data.planets;

            for (var planetKey in planetArray) {
                var planet = planetArray[planetKey];

                $scope.planetXSpritesByLetter[planet.name] = new PlanetExtendedSprite(game, planet.x, planet.y, planet);
                var planetXSprite = $scope.planetXSpritesByLetter[planet.name];
                planetGroup.add(planetXSprite);
            }
        }

        function drawConnections() {
            for (var key in $scope.planetXSpritesByLetter) {
                var planetXSprite = $scope.planetXSpritesByLetter[key];

                var connectedPlanets = planetXSprite.planet.connectedPlanets;

                for (var cpKey in  connectedPlanets) {
                    graphics.moveTo(planetXSprite.x + planetXSprite.center.x, planetXSprite.y + planetXSprite.center.y);
                    var connectedPlanet = connectedPlanets[cpKey];
                    graphics.lineTo(connectedPlanet.x, connectedPlanet.y);
                }
            }
        }


        function applyGameData(data) {
            $scope.activePlayerId = data.activePlayerId;
            if ($scope.activePlayerId == data.game.player1.playerId) {
                $scope.opponentPlayerId = data.game.player2.playerId;
                $scope.activePlayerColonyImage = 'player1flag';
            } else if ($scope.activePlayerId == data.game.player2.playerId) {
                $scope.activePlayerColonyImage = 'player2flag';
                $scope.opponentPlayerId = data.game.player1.playerId;
            }
            drawShipsOfPlayer(data.game.player1, 'spaceship');
            drawShipsOfPlayer(data.game.player2, 'spaceship');
            drawColoniesOfPlayer(data.game.player1, 'player1flag');
            drawColoniesOfPlayer(data.game.player2, 'player2flag');
        }

        function drawShipsOfPlayer(player, image) {
            var ships = player.ships;
            for (var shipKey in ships) {
                var ship = ships[shipKey];
                var shipXSprite = new ShipExtendedSprite(game, ship, player.playerId, image);
                $scope.shipXSpritesById[ship.shipId] = shipXSprite;

                shipGroup.add(shipXSprite);
            }
        }

        function drawColoniesOfPlayer(player, image) {
            var colonies = player.colonies;
            for (var colonyKey in colonies) {
                var colony = colonies[colonyKey];
                var colonyXSprite = new ColonyExtendedSprite(game, colony.planetName, player.playerId, image);

                colonyGroup.add(colonyXSprite);
            }
        }
    }
    function addFirebaseListener() {
        opponentFireBase.on('child_added', function (snapshot) {
            var action = snapshot.val();
            if (action.actionType == "MOVESHIP") {
                var shipXSprite = $scope.shipXSpritesById[action.ship.shipId];
                var planetXSprite = $scope.planetXSpritesByLetter[action.destinationPlanetName];
                moveShipToPlanetSprite(planetXSprite, shipXSprite);
            }


        });
    }

    function spaceshipListener(spaceShipXSprite) {
        $scope.selectedSpaceShipXSprite = spaceShipXSprite;
        HighlightConnectedPlanets(spaceShipXSprite)
    }

    function HighlightConnectedPlanets(spaceShipXSprite) {
        var planetXSprite = $scope.planetXSpritesByLetter[spaceShipXSprite.ship.planetName];
        var connectedPlanets = planetXSprite.planet.connectedPlanets;
        for (var connectedPlanetKey in  connectedPlanets) {
            var planet = connectedPlanets[connectedPlanetKey];
            var planetSpriteToHighlight = $scope.planetXSpritesByLetter[planet.name];
            planetSpriteToHighlight.loadTexture('planet1_selected');
            planetSpriteToHighlight.eligibleMove = true;
        }
    }

    function planetListener(planetXSprite) {
        //Move spaceShip
        function frontEndmoveShip() {
            allPlanetsNormal();
            moveShipToPlanetSprite(planetXSprite, $scope.selectedSpaceShipXSprite);
            $scope.selectedSpaceShipXSprite.ship.planetName = planetXSprite.planet.name;
            spaceshipListener($scope.selectedSpaceShipXSprite);
            //Draw new colony
            var colonyXSprite = new ColonyExtendedSprite(game, planetXSprite.planet.name, $scope.activePlayerId, $scope.activePlayerColonyImage);
            colonyGroup.add(colonyXSprite);
        }

            var action = {
            actionType: "MOVESHIP",
            ship: $scope.selectedSpaceShipXSprite.ship,
            destinationPlanetName: planetXSprite.planet.name
        };
        doAction(action, planetXSprite.eligibleMove, frontEndmoveShip);
    }

    function allPlanetsNormal() {
        for (var planetkey in $scope.planetXSpritesByLetter) {
            $scope.planetXSpritesByLetter[planetkey].loadTexture('planet1');
            $scope.planetXSpritesByLetter[planetkey].eligibleMove = false;
        }
    }

    function moveShipToPlanetSprite(planetXSprite, ship) {
//        ship = ship || $scope.selectedSpaceShipXSprite;
        game.input.disabled = true;
        var x = planetXSprite.center.x - 25 + xExtraByCamera;
        var y = planetXSprite.center.y - 10 + yExtraByCamera;
        game.physics.moveToXY(ship, x, y, 60, 1000);
        setTimeout(function () {
            ship.body.velocity.x = 0;
            ship.body.velocity.y = 0;
            game.input.disabled = false;
        }, 1000);
    }
});
