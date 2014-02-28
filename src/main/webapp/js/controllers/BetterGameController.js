/**
 * Created by Tim on 24/02/14.
 */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("BetterGameController", function ($scope, $translate, Map, Game, Action, ActiveGame, $route, $routeParams) {
    //region Declarations


    const CANVASWIDTH = 1120;
    const CANVASHEIGHT = 600;
    var game = new Phaser.Game(CANVASWIDTH, CANVASHEIGHT, Phaser.AUTO, 'game', { preload: preload, create: create, update: update, render: render });
    var cursors;
    var xExtraByCamera;
    var yExtraByCamera;
    var firebaseGameURL;
    var commandPointsText;

    var planetGroup;
    var shipGroup;
    var colonyGroup;

    var button;
    $scope.planetXSpritesByLetter = [];
    $scope.selectedSpaceShipXSprite = null;
    $scope.commandPoints = 0;

    $scope.game = {
        gameId: "",
        shipXSpritesById: [],
        activePlayerId: "",
        opponentPlayerId: "",
        activePlayerColonyImage: "",
        opponentPlayerColonyImage: ""
    };


    var PlanetExtendedSprite = function (game, x, y, planet) {
        var image = game.cache.getImage('planet1');
        var width = image.width;
        var height = image.height;
        var eligibleMove = false;
        Phaser.Sprite.call(this, game, x - width / 2, y - height / 2, 'planet1');
        this.planet = planet;
        this.inputEnabled = true;
        this.body.immovable = true;
        this.events.onInputDown.add(planetListener, this);

        this.getConnectedPlanetXSprites = function () {
            var connectedPlanetXSprites = [];
            for (var i = 0; i < this.planet.connectedPlanets.length; i++) {
                var connectedPlanet = this.planet.connectedPlanets[i];
                connectedPlanetXSprites[i] = $scope.planetXSpritesByLetter[connectedPlanet.name];
            }
            return connectedPlanetXSprites;
        };

        this.setEligibleMove = function (isEligible) {
            eligibleMove = isEligible;
            if (eligibleMove) {
                this.loadTexture('planet1_selected');
            } else {
                this.loadTexture('planet1');
            }
        }
        this.isEligibleMove = function () {
            return eligibleMove;
        }

    }
    PlanetExtendedSprite.prototype = Object.create(Phaser.Sprite.prototype);
    PlanetExtendedSprite.prototype.constructor = PlanetExtendedSprite;


    var ShipExtendedSprite = function (game, ship, playerId, image) {
        var planet = $scope.planetXSpritesByLetter[ship.planetName].planet;
        Phaser.Sprite.call(this, game, planet.x, planet.y - 15, image);
        this.ship = ship;
        this.playerId = playerId;
        this.inputEnabled = true;
        this.body.immovable = true;
        shipGroup.add(this);
        if (playerId == $scope.game.activePlayerId) {
            this.events.onInputDown.add(spaceshipListener, this);
        }

        this.enforcePositionChanges = function () {
            this.x = $scope.planetXSpritesByLetter[this.ship.planetName].planet.x;
            this.y = $scope.planetXSpritesByLetter[this.ship.planetName].planet.y - 15;
        };

        this.highlightConnectedPlanets = function () {
            var planetXSprite = $scope.planetXSpritesByLetter[this.ship.planetName];
            var connectedPlanetXSprites = planetXSprite.getConnectedPlanetXSprites();
            for (var i = 0; i < connectedPlanetXSprites.length; i++) {
                connectedPlanetXSprites[i].setEligibleMove(true);
            }
        }
    };

    ShipExtendedSprite.prototype = Object.create(Phaser.Sprite.prototype);
    ShipExtendedSprite.prototype.constructor = ShipExtendedSprite;
    ShipExtendedSprite.prototype.previousPlanet = null;


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
        game.load.spritesheet('button', 'assets/button_sprite_sheet.png', 193, 71);
        game.load.image('planet1', 'assets/planet1_small.png');
        game.load.image('planet1_selected', 'assets/planet1_small_selected.png');
        game.load.image('spaceship', 'assets/spaceship.png');
        game.load.image('player1flag', 'assets/player1flag.png');
        game.load.image('player2flag', 'assets/player2flag.png');
    }


    function create() {

        game.world.setBounds(0, 0, 1780, 1000);
        cursors = game.input.keyboard.createCursorKeys();
        drawGame();
        button = game.add.button(500, 75, 'button', onClickEndTurn, this, 2, 1, 0);
        button.fixedToCamera = true;
        button.cameraOffset.setTo(CANVASWIDTH/2 - 130, CANVASHEIGHT - 83);

        var commandpointsprite = game.add.sprite(0, 0);
        commandpointsprite.fixedToCamera = true;
        commandPointsText = game.add.text(5, 5, "Commandpoints: ", { font: '100px', fill: '#FF0000'});
        commandpointsprite.addChild(commandPointsText);
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


    function resetGame() {
        ActiveGame.get({gameId: $routeParams.gameId}, applyGameActiveViewModelData);
    }

    function drawGame() {
        var backgroundsprite = game.add.sprite(0, 0, 'bg');
        backgroundsprite.inputEnabled = true;
        var graphics = game.add.graphics(0, 0);
        graphics.lineStyle(3, 0x999999, 1);

        Map.get(function (data) {

            //assign planets to their sprites;
            drawPlanets(data);
            drawConnections();
            resetGame();
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


    }

    function applyGameActiveViewModelData(data) {
        $scope.game.activePlayerId = data.activePlayerId;
        var gameData = data.game;

        applyGameData(gameData);

        firebaseGameURL = new Firebase(data.firebaseGameURL);
        addFirebaseListener();
    }

    function addFirebaseListener() {
        console.log("opponentFireBase in listener: " + firebaseGameURL);
        firebaseGameURL.on('value', function (snapshot) {
            var game = snapshot.val();
            applyGameData(game);
            $scope.selectedSpaceShipXSprite.highlightConnectedPlanets();
        });
    }

    function selectShip(spaceShipXSprite) {
        $scope.selectedSpaceShipXSprite = spaceShipXSprite;
        spaceShipXSprite.highlightConnectedPlanets();
    }

    function spaceshipListener(spaceShipXSprite) {
        selectShip(spaceShipXSprite);
    }


    function planetListener(planetXSprite) {
        if (planetXSprite.isEligibleMove()) {
            allPlanetsNormal();

            $scope.selectedSpaceShipXSprite.ship.planetName = planetXSprite.planet.name;
            $scope.selectedSpaceShipXSprite.enforcePositionChanges();


            var action = {
                gameId: $scope.game.gameId,
                actionType: "MOVESHIP",
                ship: $scope.selectedSpaceShipXSprite.ship,
                destinationPlanetName: planetXSprite.planet.name,
                playerId: $scope.game.activePlayerId
            };
            Action.save(action, function () {
            }, function () {
                resetGame();
            });
        }
    }

    function allPlanetsNormal() {
        for (var key in $scope.planetXSpritesByLetter) {
            $scope.planetXSpritesByLetter[key].setEligibleMove(false);
        }
    }

    function applyGameData(gameData) {
        $scope.game.gameId = gameData.gameId;

        if ($scope.game.activePlayerId == gameData.player1.playerId) {
            $scope.game.opponentPlayerId = gameData.player2.playerId;
            $scope.game.opponentPlayerColonyImage = 'player2flag';
            $scope.game.activePlayerColonyImage = 'player1flag';
            $scope.commandPoints = gameData.player1.commandPoints;
        } else if ($scope.game.activePlayerId == gameData.player2.playerId) {
            $scope.game.activePlayerColonyImage = 'player2flag';
            $scope.game.opponentPlayerColonyImage = 'player1flag';
            $scope.game.opponentPlayerId = gameData.player1.playerId;
            $scope.commandPoints = gameData.player2.commandPoints;
        }

        drawShipsOfPlayer(gameData.player1, 'spaceship');
        drawShipsOfPlayer(gameData.player2, 'spaceship');
        drawColoniesOfPlayer(gameData.player1, 'player1flag');
        drawColoniesOfPlayer(gameData.player2, 'player2flag');
        updateCommandPoints();

        function drawShipsOfPlayer(player, image) {
            var ships = player.ships;
            for (var shipKey in ships) {
                var ship = ships[shipKey];
                var shipXSprite = $scope.game.shipXSpritesById[ship.shipId];
                if (shipXSprite == undefined || shipXSprite == null) {
                    shipXSprite = new ShipExtendedSprite(game, ship, player.playerId, image);
                    $scope.game.shipXSpritesById[ship.shipId] = shipXSprite;
                } else {
                    //shipXSprite = new ShipExtendedSprite(game, ship, player.playerId, image);
                    shipXSprite.ship = ship;
                    shipXSprite.enforcePositionChanges();
                }

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

    function onClickEndTurn () {
            allPlanetsNormal();

            $scope.selectedSpaceShipXSprite = null;

            var action = {
                gameId: $scope.game.gameId,
                actionType: "ENDTURN",
                playerId: $scope.game.activePlayerId
            };
            Action.save(action, function () {
            }, function () {
                resetGame();
            });
    }

    function updateCommandPoints() {
        commandPointsText.setText("Commandpoints: " + $scope.commandPoints);
    }

});
