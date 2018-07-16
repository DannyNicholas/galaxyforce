package com.danosoftware.galaxyforce.sprites.game.behaviours;

import com.danosoftware.galaxyforce.sprites.game.interfaces.ExplodingSprite;

/**
 * Interface that handles behaviour following a sprite being hit.
 */
public interface HitBehaviour {

    /**
     * Start hit behaviour
     */
    void startHit();

    /**
     * Is sprite still performing hit behaviour?
     */
    boolean isHit();

    /**
     * Update hit behaviour using delta-time
     */
    void updateHit(float deltaTime);

    /**
     * Reset hit behaviour early (before finishing normally)
     */
    void reset();
}
