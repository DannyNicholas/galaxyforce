package com.danosoftware.galaxyforce.sprites.game.missiles.aliens;

import com.danosoftware.galaxyforce.sprites.common.AbstractCollidingSprite;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.view.Animation;

public abstract class AbstractAlienMissile extends AbstractCollidingSprite implements
    IAlienMissile {

  private boolean isDestroyed;
  private final Animation animation;

  // state time used to select the current animation frame
  private float stateTime;

  AbstractAlienMissile(
      Animation animation,
      float x,
      float y,
      SpriteDetails spriteDetails) {

    super(
        spriteDetails,
        x,
        y -
            (spriteDetails.getDimensions() != null
                ? spriteDetails.getDimensions().getHeight() / 2f
                : 0f));
    this.isDestroyed = false;
    this.animation = animation;
    this.stateTime = 0f;
  }

  @Override
  public void destroy() {
    this.isDestroyed = true;
  }

  @Override
  public boolean isDestroyed() {
    return isDestroyed;
  }

  @Override
  public void animate(float deltaTime) {
    stateTime += deltaTime;
    changeType(animation.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING));
  }
}
