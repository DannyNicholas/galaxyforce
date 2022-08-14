package com.danosoftware.galaxyforce.sprites.game.missiles.bases;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenTop;

import com.danosoftware.galaxyforce.enumerations.BaseMissileSpeed;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * Missile that fires straight upwards
 */
public class BaseMissileUpwards extends AbstractBaseMissile {

  /* distance missile can move per second */
  private final int missileSpeed;

  public BaseMissileUpwards(
      final float xStart,
      final float yStart,
      final Animation animation,
      final BaseMissileSpeed baseMissileSpeed) {
    super(
        animation,
        xStart,
        yStart,
        animation.getKeyFrame(0, Animation.ANIMATION_LOOPING));
    this.missileSpeed = baseMissileSpeed.getSpeed();
  }

  @Override
  public void animate(float deltaTime) {

    moveYByDelta(missileSpeed * deltaTime);

    // if missile is now off screen then destroy it
    if (offScreenTop(this)) {
      destroy();
    }
  }
}
