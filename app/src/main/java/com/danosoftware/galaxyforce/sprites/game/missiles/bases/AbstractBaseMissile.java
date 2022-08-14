package com.danosoftware.galaxyforce.sprites.game.missiles.bases;

import com.danosoftware.galaxyforce.sprites.common.AbstractCollidingSprite;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.view.Animation;


public abstract class AbstractBaseMissile extends AbstractCollidingSprite implements IBaseMissile {

  private boolean isDestroyed;

  private final Animation animation;

  // state time used to select the current animation frame
  private float stateTime;

  AbstractBaseMissile(
      Animation animation,
      float x,
      float y,
      SpriteDetails spriteDetails) {

    // adjust missile starting position by half the missile's height
    super(
        spriteDetails,
        x,
        y +
            (spriteDetails.getDimensions() != null
                ? spriteDetails.getDimensions().getHeight() / 2f
                : 0f));
    this.isDestroyed = false;
    this.animation = animation;
    this.stateTime = 0f;
  }

  // by default, most base missiles will only hit an alien once and destroy themselves
  @Override
  public boolean hitBefore(IAlien alien) {
    return false;
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
