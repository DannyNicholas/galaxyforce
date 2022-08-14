package com.danosoftware.galaxyforce.sprites.game.missiles.bases;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenAnySide;

import com.danosoftware.galaxyforce.enumerations.BaseMissileSpeed;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * Single missile of a blast. Missiles fires in a fixed direction.
 */
public class BaseMissileBlast extends AbstractBaseMissile {

  /* offset applied to x and y every move */
  private final float xDelta;
  private final float yDelta;

  public BaseMissileBlast(
      final float xStart,
      final float yStart,
      final Animation animation,
      final float angle,
      final BaseMissileSpeed missileSpeed) {

    super(
        animation,
        xStart,
        yStart,
        animation.getKeyFrame(0, Animation.ANIMATION_LOOPING));

    // convert angle to degrees for sprite rotation.
    // needs to be adjusted by 90 deg for correct rotation.
    rotate((float) ((angle - Math.PI / 2f) * (180f / Math.PI)));

    // calculate the deltas to be applied each move
    this.xDelta = missileSpeed.getSpeed() * (float) Math.cos(angle);
    this.yDelta = missileSpeed.getSpeed() * (float) Math.sin(angle);
  }

  @Override
  public void animate(float deltaTime) {
    // move missile by calculated deltas
    moveByDelta(
        xDelta * deltaTime,
        yDelta * deltaTime);

    // if missile is now off screen then destroy it
    if (offScreenAnySide(this)) {
      destroy();
    }
  }
}
