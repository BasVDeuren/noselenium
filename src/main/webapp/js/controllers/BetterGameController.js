/**
 * Created by Tim on 24/02/14.
 */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("BetterGameController", function ($scope, $translate, Map, Game, Action, ActiveGame, $route, $routeParams) {
    //region Declarations

    const BUILDSHIPCOST = 3;
    const COLONIZECOST = 2;
    const MOVECOST = 1;
    const CANVASWIDTH = 1120;
    const CANVASHEIGHT = 600;
    const FARRESTPOINTOFCAMERA = 661;
    const MIDPOINTOFCAMERA = 200;
    var game = new Phaser.Game(CANVASWIDTH, CANVASHEIGHT, Phaser.AUTO, 'game', { preload: preload, create: create, update: update, render: render });
    var cursors;
    var xExtraByCamera;
    var yExtraByCamera;
    var firebaseGameRef;
    var commandPointsText;

    var planetGroup;
    var shipGroup;
    var colonyGroup;
    var buttonGroup;
    var miniShipGroup;

    var btnEndTurn;
    $scope.planetXSpritesByLetter = [];
    $scope.selectedSpaceShipXSprite = null;
    $scope.commandPoints = 0;
    $scope.isTurnEnded = true;
    $scope.activePlayerMiniShipImage = null;

    $scope.game = {
        gameId: "",
        shipXSpritesById: [],
        activePlayerId: "",
        opponentPlayerId: "",
        activePlayerColonyImage: "",
        opponentPlayerColonyImage: ""
    };
    //endregion

    //region Prototypes
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
        this.colonyXSprite = null;

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
        var text = game.add.text(34, -5, ship.strength, {font: '20px Arial', fill: '#FF0000'});
        this.addChild(text);
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

        this.drawStrength = function () {
            text.setText(this.ship.strength)
        }


    };

    ShipExtendedSprite.prototype = Object.create(Phaser.Sprite.prototype);
    ShipExtendedSprite.prototype.constructor = ShipExtendedSprite;
    ShipExtendedSprite.prototype.previousPlanet = null;


    var ColonyExtendedSprite = function (game, colony, planetName, playerId, image) {
        var planet = $scope.planetXSpritesByLetter[planetName].planet;
        Phaser.Sprite.call(this, game, planet.x - 15, planet.y - 42, image);

        this.colony = colony;

        this.body.immovable = true;
        if (playerId == $scope.game.activePlayerId) {
            this.inputEnabled = true;
            this.events.onInputDown.add(colonyListener, this);
        }
        var miniShipSprite = game.add.sprite(-18, -18, $scope.activePlayerMiniShipImage);

        miniShipSprite.inputEnabled = true;
        miniShipSprite.events.onInputDown.add(miniShipListener);
        miniShipSprite.colonyXSprite = this;
        this.addChild(miniShipSprite);

        miniShipSprite.visible = false;
        this.triggerMiniShip = function () {
            miniShipSprite.visible = !miniShipSprite.visible;
        }


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
        game.load.image('player1spaceship', 'assets/player1spaceship.png');
        game.load.image('miniplayer1spaceship', 'assets/mini_player1spaceship.png');
        game.load.image('player2spaceship', 'assets/player2spaceship.png');
        game.load.image('miniplayer2spaceship', 'assets/mini_player2spaceship.png');
        game.load.image('player1flag', 'assets/player1flag.png');
        game.load.image('player2flag', 'assets/player2flag.png');
        game.load.image('player1castle', 'assets/player1castle.png');
        game.load.image('player2castle', 'assets/player2castle.png');
    }


    function create() {


        game.world.setBounds(0, 0, 1780, 1000);
        cursors = game.input.keyboard.createCursorKeys();
        drawGame();
        buttonGroup = game.add.group();


        var commandpointsprite = game.add.sprite(0, 0);
        commandpointsprite.fixedToCamera = true;
        commandPointsText = game.add.text(5, 5, "Commandpoints: ", { font: '20px Arial', fill: '#FF0000'});
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
        game.add.sprite(0, 0, 'bg');

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
            planetGroup.z = 0;
            shipGroup = game.add.group();
            shipGroup.z = 2;
            colonyGroup = game.add.group();
            colonyGroup.z = 1;
            miniShipGroup = game.add.group();
            miniShipGroup.z = 1000;
            btnEndTurn = game.add.button(500, 75, 'button', onClickEndTurn, this, 2, 1, 0);
            btnEndTurn.events.onInputDown.add(onClickEndTurn);
            btnEndTurn.fixedToCamera = true;
            btnEndTurn.cameraOffset.setTo(CANVASWIDTH / 2 - 180, CANVASHEIGHT - 83);
            planetGroup.add(btnEndTurn);
            var planetArray = data.planets;


            for (var planetKey in planetArray) {
                var planet = planetArray[planetKey];

                $scope.planetXSpritesByLetter[planet.name] = new PlanetExtendedSprite(game, planet.x, planet.y, planet);
                var planetXSprite = $scope.planetXSpritesByLetter[planet.name];
                planetGroup.add(planetXSprite);
            }


            btnEndTurn.bringToTop();

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
        if(data.game.player2.playerId == $scope.game.activePlayerId)
        {
            game.camera.x = FARRESTPOINTOFCAMERA;
            game.camera.y = MIDPOINTOFCAMERA;
        }


        firebaseGameRef = new Firebase(data.firebaseGameURL);

        var firebasePlayer1ShipsGameRef = new Firebase(data.firebaseGameURL + "/player1/ships");
        var firebasePlayer2ShipsGameRef = new Firebase(data.firebaseGameURL + "/player2/ships");


        firebaseGameRef.on('value', function (snapshot) {
            var game = snapshot.val();
            applyGameData(game);
            $scope.selectedSpaceShipXSprite.highlightConnectedPlanets();
        });

        firebasePlayer1ShipsGameRef.on('child_removed', shipRemovedListener);
        firebasePlayer2ShipsGameRef.on('child_removed', shipRemovedListener);


    }


//region Listeners
    function shipRemovedListener(snapshot) {
        var ship = snapshot.val();
        $scope.game.shipXSpritesById[ship.shipId].destroy();

    }

    function miniShipListener(miniShipXSprite) {
        if (!$scope.isTurnEnded) {
            if ($scope.commandPoints >= BUILDSHIPCOST) {
                var action = {
                    gameId: $scope.game.gameId,
                    actionType: "BUILDSHIP",
                    colony: miniShipXSprite.colonyXSprite.colony,

                    playerId: $scope.game.activePlayerId
                };
                Action.save(action, function () {
                }, function () {
                    resetGame();
                });
            } else {
                showNotification("Too few commandpoints!");
            }

        } else {
            showNotification("You can't move because your turn has ended!");
        }
    }

    function colonyListener(colonyXSprite) {

        colonyXSprite.triggerMiniShip();
    }

    function spaceshipListener(spaceShipXSprite) {
        selectShip(spaceShipXSprite);
    }


    function planetListener(planetXSprite) {
        if (planetXSprite.isEligibleMove()) {
            if (!$scope.isTurnEnded) {
                if ((planetXSprite.colonyXSprite != null && $scope.commandPoints >= MOVECOST)
                    || (planetXSprite.colonyXSprite == null && $scope.commandPoints >= COLONIZECOST)) {
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
                } else {
                    showNotification("Too few commandpoints!");
                }
            } else {
                showNotification("You can't move because your turn has ended!");
            }
        }
    }

//endregion

    function selectShip(spaceShipXSprite) {
        $scope.selectedSpaceShipXSprite = spaceShipXSprite;
        spaceShipXSprite.highlightConnectedPlanets();

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
            $scope.game.opponentPlayerColonyImage = 'player2castle';
            $scope.game.activePlayerColonyImage = 'player1castle';

            $scope.activePlayerMiniShipImage = 'miniplayer1spaceship';
            $scope.commandPoints = gameData.player1.commandPoints;
            $scope.isTurnEnded = gameData.player1.turnEnded;

        } else if ($scope.game.activePlayerId == gameData.player2.playerId) {

            $scope.game.opponentPlayerId = gameData.player1.playerId;
            $scope.game.opponentPlayerColonyImage = 'player1castle';
            $scope.game.activePlayerColonyImage = 'player2castle';
            $scope.activePlayerMiniShipImage = 'miniplayer2spaceship';
            $scope.commandPoints = gameData.player2.commandPoints;
            $scope.isTurnEnded = gameData.player2.turnEnded;

        }


        drawShipsOfPlayer(gameData.player1, 'player1spaceship');
        drawShipsOfPlayer(gameData.player2, 'player2spaceship');
        drawColoniesOfPlayer(gameData.player1, 'player1castle');
        drawColoniesOfPlayer(gameData.player2, 'player2castle');
        updateCommandPoints();
        btnEndTurn.visible = !$scope.isTurnEnded;


        function drawShipsOfPlayer(player, image) {
            var ships = player.ships;


            for (var shipKey in ships) {
                var ship = ships[shipKey];
                var shipXSprite = $scope.game.shipXSpritesById[ship.shipId];
                if (shipXSprite == undefined || shipXSprite == null) {
                    shipXSprite = new ShipExtendedSprite(game, ship, player.playerId, image);
                    $scope.game.shipXSpritesById[ship.shipId] = shipXSprite;
                } else {
                    shipXSprite.ship = ship;
                    shipXSprite.enforcePositionChanges();
                }
                shipXSprite.drawStrength();
            }
        }

        function drawColoniesOfPlayer(player, image) {
            var colonies = player.colonies;
            for (var colonyKey in colonies) {
                var colony = colonies[colonyKey];
                if ($scope.planetXSpritesByLetter[colony.planetName].colonyXSprite == null) {
                    var colonyXSprite = new ColonyExtendedSprite(game, colony, colony.planetName, player.playerId, image);

                    $scope.planetXSpritesByLetter[colony.planetName].colonyXSprite = colonyXSprite;

                    colonyGroup.add(colonyXSprite);
                }
            }
        }

    }

    function onClickEndTurn() {
        btnEndTurn.visible = false;
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

    function showNotification(string) {
        var sprite = game.add.sprite(0, 0);
        var text = game.add.text(5, 5, string, { font: '20px Arial', fill: '#FF0000'});
        sprite.addChild(text);
        sprite.fixedToCamera = true;
        sprite.cameraOffset.setTo(CANVASWIDTH / 2 - text.width / 2, CANVASHEIGHT / 2 - text.height / 2);

        setTimeout(function () {
            sprite.destroy();
        }, 5000);

    }

})
;
