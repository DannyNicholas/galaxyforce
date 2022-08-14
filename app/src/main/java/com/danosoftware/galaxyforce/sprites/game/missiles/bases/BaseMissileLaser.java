package com.danosoftware.galaxyforce.sprites.game.missiles.bases;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenTop;

import com.danosoftware.galaxyforce.enumerations.BaseMissileSpeed;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.view.Animation;
import java.util.HashSet;
import java.util.Set;

/**
 * Laser missile that is not destroyed on contact with an alien.
 */
public class BaseMissileLaser extends AbstractBaseMissile {

  /* distance missile can move per cycle */
  private final int missileSpeed;

  /* list of any aliens laser has already hit */
  private final Set<IAlien> hitAliens;

  public BaseMissileLaser(
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
    this.hitAliens = new HashSet<>();
  }

  /**
   * Lasers have infinite energy and so can never be destroyed by hitting an alien.
   */
  @Override
  public void destroy() {
    // do nothing
  }

  /**
   * Since laser can never be destroyed if should only hurt an alien once.
   * <p>
   * Otherwise it will provide the same damage after every collision detection cycle. This will
   * unfairly result in the destruction of any alien with one laser bolt.
   * <p>
   * Keep a reference to any aliens hit so they can't be hit twice by the same laser bolt.
   *
   * @return true if alien has been hit before
   */
  @Override
  public boolean hitBefore(IAlien alien) {
    if (hitAliens.contains(alien)) {
      // hit before return true
      return true;
    } else {
      // not hit before so add to list and return false
      hitAliens.add(alien);
      return false;
    }
  }

  @Override
  public void animate(float deltaTime) {
    // move missile until off-screen and then destroy it
    moveYByDelta(missileSpeed * deltaTime);

    // missile can only be destroyed when off screen
    // use superclass destroy() since our destroy()
    // method has no effect.
    if (offScreenTop(this)) {
      super.destroy();
    }
  }
}
