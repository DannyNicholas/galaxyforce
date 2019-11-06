package com.danosoftware.galaxyforce.sprites.game.bases.explode;

import com.danosoftware.galaxyforce.sprites.game.bases.IBase;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

public interface BaseExploder {
    /**
     * Initialise start of explosion
     */
    void startExplosion(IBase base);

    /**
     * Get the current explosion sprite.
     */
    ISpriteIdentifier getExplosion(float deltaTime);

    /**
     * Has the explosion finished?
     */
    boolean finishedExploding();
}
