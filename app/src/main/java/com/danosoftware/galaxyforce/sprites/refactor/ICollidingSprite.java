package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.utilities.Rectangle;

public interface ICollidingSprite extends IMovingSprite {

    Rectangle getBounds();

    /**
     * Destroy the sprite
     */
    void destroy();

    /**
     * Is sprite destroyed (and can be removed)?
     */
    boolean isDestroyed();
}
