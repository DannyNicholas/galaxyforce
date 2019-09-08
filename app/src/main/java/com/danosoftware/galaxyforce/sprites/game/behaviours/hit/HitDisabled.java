package com.danosoftware.galaxyforce.sprites.game.behaviours.hit;

import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

/**
 * Hit implementation that does not change animation when hit
 */
public class HitDisabled implements HitBehaviour {

    @Override
    public void startHit(float stateTime) {
        // no action - no hit behaviour
    }

    @Override
    public void startHitSilently(float stateTime) {
        // no action - no hit behaviour
    }

    @Override
    public boolean isHit() {
        return false;
    }

    @Override
    public ISpriteIdentifier getHit(float deltaTime) {
        // this should never be called for this implementation
        throw new GalaxyForceException("Illegal call to HitBehaviourDisabled.getHit()");
    }
}
