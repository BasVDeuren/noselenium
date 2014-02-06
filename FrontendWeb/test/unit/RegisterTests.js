/**
 * Created by Atheesan on 6/02/14.
 */
describe("SpaceApp", function () {
    beforeEach(module("spaceApp"));

    describe("RegisterController Tests", function () {
        var scope,
            controller;

        beforeEach(inject(function ($rootScope, $controller) {
            scope = $rootScope.$new();
            controller = $controller;
        }));

        it("passwords should match", function () {
            controller("RegisterController", {$scope: scope});
            expect(scope.checkPassword("bla","bla")).toBe(true);
        });
    });
});