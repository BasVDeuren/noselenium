/**
 * Created by Tim on 10/03/14.
 */
var spaceApp = angular.module('spaceApp');
spaceApp.controller("ReplayGameController", function ($scope, $translate, Map, Game, Action, $route, $routeParams, ReplayGame) {
    //region Declarations
    const CANVASWIDTH = 1180;
    const CANVASHEIGHT = 600;
    var game = new Phaser.Game(CANVASWIDTH, CANVASHEIGHT, Phaser.AUTO, 'game', { preload: preload, create: create, update: update, render: render });
    var cursors;
    var xExtraByCamera;
    var yExtraByCamera;
    var firebaseGameRef;
    var p;

    var planetGroup;
    var shipGroup;
    var colonyGroup;
    var miniShipGroup;

    $scope.planetXSpritesByLetter = [];
    $scope.colonyXSpritesById = [];

    $scope.game = {
        gameId: "",
        shipXSpritesById: [],
        activePlayerId: "",
        opponentPlayerId: "",
        activePlayerColonyImage: "",
        opponentPlayerColonyImage: ""
    };
    //endregion

    //region Lifecycle Methods
    function preload() {
        game.load.image('planet1', 'assets/planet1_small.png');
        game.load.image('planet1_selected', 'assets/planet1_small_selected.png');
        game.load.image('player1spaceship', 'assets/player1spaceship.png');
        game.load.image('player2spaceship', 'assets/player2spaceship.png');
        game.load.image('player1flag', 'assets/player1flag.png');
        game.load.image('player2flag', 'assets/player2flag.png');
        game.load.image('player1castle', 'assets/player1castle.png');
        game.load.image('player2castle', 'assets/player2castle.png');
        game.load.image('winner', 'assets/monkey_winner_icon.png');
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

    function render() {

    }
    //endregion

    function drawGame() {
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

            var planetArray = data.planets;

            for (var planetKey in planetArray) {
                var planet = planetArray[planetKey];

                $scope.planetXSpritesByLetter[planet.name] = new PlanetExtendedSprite(game, planet.x, planet.y, planet, function(){});
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

    function resetGame() {
        ReplayGame.get({playerId: 1}, applyGameActiveViewModelData);
    }

    function applyGameActiveViewModelData(data) {
        $scope.game.activePlayerId = null;

        firebaseGameRef = new Firebase(data.firebaseUrl);

        firebaseGameRef.on('value', function (snapshot) {
            var gameData = snapshot.val();
            applyGameData(gameData);
        });
    }

    function applyGameData(gameData) {
        $scope.game.gameId = gameData.gameId;

        var player1IsActive = false;
        var player2IsActive = false;

        drawShips(gameData);
        drawColonies(gameData);
        var loserPlayerId = gameData.loserPlayerId;
        if (loserPlayerId != 0) {
            if ($scope.game.activePlayerId == loserPlayerId) {
                showNotification($translate('LOSTTHEGAME'));

            } else {
                p = game.add.emitter(game.world.centerX, 200, 200);
                p.makeParticles('winner');
                p.start(false, 5000, 20);
            }
            for (var colonyKey in $scope.colonyXSpritesById) {
                $scope.colonyXSpritesById[colonyKey].inputEnabled = false;
            }
            for (var shipKey in $scope.game.shipXSpritesById) {
                $scope.game.shipXSpritesById[shipKey].inputEnabled = false;
            }
        }

        function drawShips(gameData) {
            for (var key in $scope.game.shipXSpritesById) {
                $scope.game.shipXSpritesById[key].destroy();
            }

            $scope.game.shipXSpritesById = [];
            drawShipsOfPlayer(gameData.player1, 'player1spaceship', player1IsActive);
            drawShipsOfPlayer(gameData.player2, 'player2spaceship', player2IsActive);
        }

        function drawShipsOfPlayer(player, image, isActivePlayer) {
            var ships = player.ships;
            for (var shipKey in ships) {
                var ship = ships[shipKey];
                var shipXSprite = new ShipExtendedSprite(game, ship, player.playerId, image, function(){}, false, shipGroup, $scope.planetXSpritesByLetter);
                $scope.game.shipXSpritesById[ship.shipId] = shipXSprite;
            }
        }

        function drawColonies(gameData) {
            for (var key in $scope.colonyXSpritesById) {
                $scope.colonyXSpritesById[key].destroy();
            }

            $scope.colonyXSpritesById = [];

            drawColoniesOfPlayer(gameData.player2, 'player2castle');
            drawColoniesOfPlayer(gameData.player1, 'player1castle');
        }

        function drawColoniesOfPlayer(player, image) {

            var colonies = player.colonies;
            for (var colonyKey in colonies) {
                var colony = colonies[colonyKey];
                var colonyXSprite;

                colonyXSprite = new ColonyExtendedSprite(game, colony, colony.planetName, player.playerId, image, colonyGroup, $scope.planetXSpritesByLetter, function(){}, function(){}, null);

                $scope.colonyXSpritesById[colony.colonyId] = colonyXSprite;
                $scope.planetXSpritesByLetter[colony.planetName].colonyXSprite = colonyXSprite;
            }
        }
    }
});