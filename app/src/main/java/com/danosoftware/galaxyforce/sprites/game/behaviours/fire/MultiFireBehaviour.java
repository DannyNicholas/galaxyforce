package com.danosoftware.galaxyforce.sprites.game.behaviours.fire;

import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a collection of fire behaviours. Allows an alien to have multiple firing behaviours.
 * For example: fire downwards every 5 seconds and fire guided every 7 seconds.
 */
public class MultiFireBehaviour implements FireBehaviour {

  // all fire behaviours supported
  private final Collection<FireBehaviour> fireBehaviours;

  // fire behaviours that will fire when fire() is called
  private final Set<FireBehaviour> readyToFireBehaviours;

  public MultiFireBehaviour(
      final Collection<FireBehaviour> fireBehaviours) {

    this.fireBehaviours = fireBehaviours;
    this.readyToFireBehaviours = new HashSet<>();
  }

  /**
   * Iterate through all fire behaviours and identify any that are ready to fire.
   * <p>
   * Return true if any are ready to fire. We will control which actually fire in the fire()
   * method.
   */
  @Override
  public boolean readyToFire(float deltaTime) {
    for (FireBehaviour firingBehaviour : fireBehaviours) {
      if (firingBehaviour.readyToFire(deltaTime)) {
        readyToFireBehaviours.add(firingBehaviour);
      }
    }
    return readyToFireBehaviours.size() > 0;
  }

  /**
   * Fire any missiles that are ready then set back to unready.
   */
  @Override
  public void fire(IAlien alien) {
    for (FireBehaviour firingBehaviour : readyToFireBehaviours) {
      firingBehaviour.fire(alien);
    }
    readyToFireBehaviours.clear();
  }
}
