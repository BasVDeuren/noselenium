/**
 * Created by Tim on 24/02/14.
 */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("BetterGameController", function ($scope, $translate, Map, Game, Action, ActiveGame, $route, $routeParams, UserService) {
    //region Declarations
    var game = new Phaser.Game(1120, 600, Phaser.AUTO, 'game', { preload: preload, create: create, update: update});
    var cursors;
    var xExtraByCamera;
    var yExtraByCamera;
    var opponentFireBase;

    var planetGroup;
    var shipGroup;
    var colonyGroup;
    $scope.planetXSpritesByLetter = [];

    $scope.game = {
        gameId: "",
        shipXSpritesById: [],
        activePlayerId: "",
        opponentPlayerId: "",
        selectedSpaceShipXSprite: "",
        activePlayerColonyImage: "",
        opponentPlayerColonyImage: "",
        commandPoints: ""
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
            for (var connectedPlanetKey in this.planet.connectedPlanets) {
                var connectedPlanet = this.planet.connectedPlanets[connectedPlanetKey];
                connectedPlanetXSprites[connectedPlanetKey] = $scope.planetXSpritesByLetter[connectedPlanet.name];
            }
            return connectedPlanetXSprites;
        };

        this.setEligibleMove = function (isEligible) {
            eligibleMove = isEligible;
            if(eligibleMove )
            {
                this.loadTexture('planet1_selected');
            }else{
                this.loadTexture('planet1');
            }
        }
        this.getEligibleMove = function () {
            return eligibleMove();
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
            this.x = $scope.planetXSpritesByLetter[ship.planetName].planet.x;
            this.y = $scope.planetXSpritesByLetter[ship.planetName].planet.y - 15;
        };

        this.animatePositionChanges = function (afterAnimationCode) {
            var x = $scope.planetXSpritesByLetter[ship.planetName].planet.x;
            var y = $scope.planetXSpritesByLetter[ship.planetName].planet.y - 15;
            game.physics.moveToXY(this, x, y, 60, 1000);
            function afterTimeOut()
            {
                this.this.enforcePositionChanges();
                afterAnimationCode();
            }
            setTimeout(afterTimeOut(), 1000);
        };


        this.highlightConnectedPlanets = function () {
            var planetXSprite = $scope.planetXSpritesByLetter[this.ship.planetName];
            var connectedPlanetXSprites = planetXSprite.getConnectedPlanetXSprites();
            for (var key in  connectedPlanetXSprites) {
                var planetSpriteToHighlight = $scope.planetXSpritesByLetter[key];
                planetSpriteToHighlight.setEligibleMove(true);


            }
        }

    };

    ShipExtendedSprite.prototype = Object.create(Phaser.Sprite.prototype);
    ShipExtendedSprite.prototype.constructor = ShipExtendedSprite;
    ShipExtendedSprite.prototype.previousPlanet = null;


//Template Function for doing actions and pushing them to the server and the opponent's client!
    function doAction(action, frontEndCondition, frontEndLogic) {
        if (frontEndCondition) {
            frontEndLogic();
            Action.save(action, function (data) {
                applyGameData(data);
            }, function () {
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
    ColonyExtendedSprite.prototype.setPlanet = function (planet) {
        this.planet = planet;
        this.x = $scope.planetXSpritesByLetter[ship.planetName].planet.x;
        this.y = $scope.planetXSpritesByLetter[ship.planetName].planet.y;
    }
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
        game.world.setBounds(0, 0, 1780, 1000);
        cursors = game.input.keyboard.createCursorKeys();
        drawGame();

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
            ActiveGame.get({gameId: $routeParams.gameId}, applyGameActiveViewModelData);
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

        function applyGameActiveViewModelData(data) {
            $scope.game.activePlayerId = data.activePlayerId;
            var gameData = data.game;

            applyGameData(gameData);

            opponentFireBase = new Firebase(data.opponentFirebaseURL);
            addFirebaseListener();
        }


    }

    function addFirebaseListener() {
        console.log("opponentFireBase in listener: " + opponentFireBase);
        opponentFireBase.on('child_added', function (snapshot) {
            var action = snapshot.val();
            if (action.actionType == "MOVESHIP") {
                var shipXSprite = $scope.game.shipXSpritesById[action.ship.shipId];
                var planetXSprite = $scope.planetXSpritesByLetter[action.destinationPlanetName];
                shipXSprite.ship = action.ship;
                shipXSprite.animatePositionChanges(function(){/*do nothing, all's fine*/});

            }
        });
    }

    function selectShip(spaceShipXSprite) {
        $scope.game.selectedSpaceShipXSprite = spaceShipXSprite;
        spaceShipXSprite.highlightConnectedPlanets();
    }

    function spaceshipListener(spaceShipXSprite) {
        selectShip(spaceShipXSprite);
    }


    function planetListener(planetXSprite) {

        allPlanetsNormal();

        $scope.game.selectedSpaceShipXSprite.ship.planet = planetXSprite.planet;
        $scope.game.selectedSpaceShipXSprite.animatePositionChanges(function()
        {
            $scope.game.selectedSpaceShipXSprite.highlightConnectedPlanets();
        });

        var action = {
            gameId: $scope.game.gameId,
            actionType: "MOVESHIP",
            ship: $scope.game.selectedSpaceShipXSprite.ship,
            destinationPlanetName: planetXSprite.planet.name,
            playerId: $scope.game.activePlayerId
        };
        Action.save(action, function (data) {
           applyGameData(data);
        }, function () {
            create();
        });
    }

    function allPlanetsNormal() {
        for (var planetkey in $scope.planetXSpritesByLetter) {
            $scope.planetXSpritesByLetter[planetkey].setEligibleMove(false);
        }
    }

    function moveShipToPlanetSprite(planetXSprite, shipXSprite, moveByActivePlayer) {
//        ship = ship || $scope.game.selectedSpaceShipXSprite;

        var image;
        if (moveByActivePlayer) {
            game.input.disabled = true;
            image = $scope.game.activePlayerColonyImage;
        } else {
            image = $scope.game.opponentPlayerColonyImage;
        }

        var x = planetXSprite.center.x - 25 + xExtraByCamera;
        var y = planetXSprite.center.y - 10 + yExtraByCamera;
        console.log("shipXSprite: " + shipXSprite);
        console.log("shipXSpriteId: " + shipXSprite.ship.shipId);
        console.log("shipXSpritePlanetName: " + shipXSprite.ship.planetName);
        console.log("planetXSprite: " + planetXSprite);
        console.log("x: " + x + "\ny: " + y);
        game.physics.moveToXY(shipXSprite, x, y, 60, 1000);
        setTimeout(function () {
            shipXSprite.body.velocity.x = 0;
            shipXSprite.body.velocity.y = 0;
            shipXSprite.x = x;
            shipXSprite.y = y;
            game.input.disabled = false;
        }, 1000);
        console.log("ShipXPlanetName: " + shipXSprite.ship.planetName);
        //Draw new colony
        var colonyXSprite = new ColonyExtendedSprite(game, planetXSprite.planet.name, $scope.game.activePlayerId, image);
        colonyGroup.add(colonyXSprite);
    }

    function applyGameData(gameData) {
        $scope.game.gameId = gameData.gameId;
        if ($scope.game.activePlayerId == gameData.player1.playerId) {
            $scope.game.opponentPlayerId = gameData.player2.playerId;
            $scope.game.opponentPlayerColonyImage = 'player2flag';

            $scope.game.activePlayerColonyImage = 'player1flag';
        } else if ($scope.game.activePlayerId == gameData.player2.playerId) {
            $scope.game.activePlayerColonyImage = 'player2flag';
            $scope.game.opponentPlayerColonyImage = 'player1flag';
            $scope.game.opponentPlayerId = gameData.player1.playerId;
        }
        drawShipsOfPlayer(gameData.player1, 'spaceship');
        drawShipsOfPlayer(gameData.player2, 'spaceship');
        drawColoniesOfPlayer(gameData.player1, 'player1flag');
        drawColoniesOfPlayer(gameData.player2, 'player2flag');

        function drawShipsOfPlayer(player, image) {
            var ships = player.ships;
            for (var shipKey in ships) {
                var ship = ships[shipKey];
                var shipXSprite = $scope.game.shipXSpritesById[ship.shipId];
                if (shipXSprite == undefined) {
                    shipXSprite = new ShipExtendedSprite(game, ship, player.playerId, image);
                } else {
                    shipXSprite.ship = ship;
                    shipXSprite.animatePositionChanges(function(){/*doNothing, all's fine*/});
                }
                $scope.game.shipXSpritesById[ship.shipId] = shipXSprite;
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

});
