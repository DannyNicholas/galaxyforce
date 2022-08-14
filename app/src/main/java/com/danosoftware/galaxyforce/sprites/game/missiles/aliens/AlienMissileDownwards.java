package com.danosoftware.galaxyforce.sprites.game.missiles.aliens;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenBottom;

import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.view.Animation;

public class AlienMissileDownwards extends AbstractAlienMissile {

  // missile speed in pixels per second
  private final int missileSpeed;

  public AlienMissileDownwards(
      float xStart,
      float yStart,
      final Animation animation,
      final AlienMissileSpeed missileSpeed) {
    super(
        animation,
        xStart,
        yStart,
        animation.getKeyFrame(0, Animation.ANIMATION_LOOPING));
    this.missileSpeed = -missileSpeed.getSpeed();
  }

  @Override
  public void animate(float deltaTime) {
    super.animate(deltaTime);

    moveYByDelta(missileSpeed * deltaTime);

    // if missile is now off screen then destroy it
    if (offScreenBottom(this)) {
      destroy();
    }
  }
}
