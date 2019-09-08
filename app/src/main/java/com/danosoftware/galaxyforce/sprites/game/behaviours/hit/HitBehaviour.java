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
     * Start hit behaviour. Supply stateTime to keep hit animation
     * in sync with the parent sprite.
     * <p>
     * This implementation is silent and does not cause a hit
     * sound or any vibration. Typically used for followers
     * when another sprite will handle sound/vibration effects.
     */
    void startHitSilently(float stateTime);

    /**
     * Is sprite still performing hit behaviour?
     */
    boolean isHit();

    /**
     * Get the current hit sprite.
     */
    ISpriteIdentifier getHit(float deltaTime);
}
