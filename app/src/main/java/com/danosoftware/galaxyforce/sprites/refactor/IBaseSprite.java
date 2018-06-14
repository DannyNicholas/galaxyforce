package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;

public interface IBaseSprite extends IMovingSprite {

    void animate();

    void onHitBy(IAlien alien);

    void onHitBy(IAlienMissile missile);

    void destroy();

    void collectPowerUp(PowerUpType powerUpType);
}
