package com.danosoftware.galaxyforce.sprites.game.behaviours.hit;

import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * Hit implementation that does not change animation when hit
 */
public class HitDisabled implements HitBehaviour {

  @Override
  public void startHit(float stateTime) {
    // no action - no hit behaviour
  }

  @Override
  public void startHitFollower(float stateTime) {
    // no action - no hit behaviour
  }

  @Override
  public boolean isHit() {
    return false;
  }

  @Override
  public SpriteDetails getHit(Animation hitAnimation, float deltaTime) {
    // this should never be called for this implementation
    throw new GalaxyForceException("Illegal call to HitBehaviourDisabled.getHit()");
  }
}
