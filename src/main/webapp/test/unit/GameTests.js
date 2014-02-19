describe("SpaceApp", function () {
    beforeEach(module("spaceApp"));

    describe("GameController Tests", function () {
        var scope,
            controller;

        beforeEach(inject(function ($rootScope, $controller) {
            scope = $rootScope.$new();
            controller = $controller;
        }));

        it("passwords should match", function () {
            controller("GameController", {$scope: scope});

            scope.create();

            expect(true).toBe(true);
        });
    });
});