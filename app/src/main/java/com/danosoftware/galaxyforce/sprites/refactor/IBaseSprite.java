package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;

import java.util.List;

public interface IBaseSprite extends ICollidingSprite {

    void onHitBy(IAlien alien);

    void onHitBy(IAlienMissile missile);

    void destroy();

    void collectPowerUp(PowerUpType powerUpType);

    List<ISprite> allSprites();
}
