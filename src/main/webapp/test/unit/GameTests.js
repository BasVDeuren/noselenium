describe("SpaceApp", function () {
    beforeEach(module("spaceApp"));

    describe("GameController Tests", function () {
        var scope,
            controller;

        beforeEach(inject(function ($rootScope, $controller) {
            scope = $rootScope.$new();
            controller = $controller;
        }));

        it("post to /api/auth/game, game created id returned", function () {
            controller("createGameController", {$scope: scope});

            var gameData = {gameName:"SpaceCrack Game 1", opponentProfileId:"2"}
            scope.gameData = gameData;
            scope.createGame();

            expect(scope.gameId).toBeGreaterThan(0);
        });
    });
});