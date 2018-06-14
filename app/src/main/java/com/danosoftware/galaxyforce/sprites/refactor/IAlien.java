package com.danosoftware.galaxyforce.sprites.refactor;

public interface IAlien extends IMovingSprite  {

    void animate();

    // reset path - only valid for aliens with path
    void reset();

    void onHitBy(IBaseMissile baseMissile);

    void destroy();
}
