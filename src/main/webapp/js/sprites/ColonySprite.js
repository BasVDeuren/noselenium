/**
 * Created by Tim on 10/03/14.
 */
var ColonyExtendedSprite = function (game, colony, planetName, playerId, image, colonyGroup, planetXSpritesByLetter, colonyListener, miniShipListener, miniShipImage ) {
    var planet = planetXSpritesByLetter[planetName].planet;
    Phaser.Sprite.call(this, game, planet.x - 15, planet.y - 42, image);
    colonyGroup.add(this);
    this.playerId = playerId;
    this.colony = colony;
    var miniShipSprite;
    this.body.immovable = true;
    if (colonyListener != null) {
        this.inputEnabled = true;
        this.events.onInputDown.add(colonyListener, this);
    }
    if(miniShipImage != null)
    {
        miniShipSprite = game.add.sprite(-18, -18, miniShipImage);

        miniShipSprite.inputEnabled = true;
        if(miniShipListener != null)
        {
            miniShipSprite.events.onInputDown.add(miniShipListener);
        }
        miniShipSprite.colonyXSprite = this;
        this.addChild(miniShipSprite);
        miniShipSprite.visible = false;
        miniShipSprite.inputEnabled = false;
    }

    this.triggerMiniShip = function () {
        miniShipSprite.visible = !miniShipSprite.visible;
        miniShipSprite.inputEnabled = miniShipSprite.visible;
    }
};

ColonyExtendedSprite.prototype = Object.create(Phaser.Sprite.prototype);
ColonyExtendedSprite.prototype.constructor = ColonyExtendedSprite;
