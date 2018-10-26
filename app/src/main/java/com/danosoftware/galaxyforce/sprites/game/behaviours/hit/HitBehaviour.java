package com.danosoftware.galaxyforce.sprites.game.behaviours.hit;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

/**
 * Interface that handles behaviour following a sprite being hit.
 */
public interface HitBehaviour {

    /**
     * Start hit behaviour. Supply stateTime to keep hit animation
     * in sync with the parent sprite.
     */
    void startHit(float stateTime);

    /**
     * Is sprite still performing hit behaviour?
     */
    boolean isHit();

    /**
     * Get the current hit sprite.
     */
    ISpriteIdentifier getHit(float deltaTime);
}
