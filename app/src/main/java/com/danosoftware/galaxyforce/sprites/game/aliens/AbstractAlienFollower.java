package com.danosoftware.galaxyforce.sprites.game.aliens;

import static com.danosoftware.galaxyforce.sprites.game.aliens.enums.AlienState.EXPLODING;

import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviour;
import com.danosoftware.galaxyforce.waves.AlienCharacter;

public class AbstractAlienFollower extends AbstractAlien implements IAlienFollower {
  /*
   * ******************************************************
   * PRIVATE STATIC VARIABLES
   * ******************************************************
   */

  /* distance alien can move each cycle in pixels each second */
  private final int alienMoveInPixels;

  /* minimum distance between alien followers */
  private final int minimumAllowedDistanceFromFollowedSquared;

  /*
   * keep local copies of hit behaviour and stateTime to allow
   * showHit() to keep hit animation in sync with parent.
   */
  private final HitBehaviour hitBehaviour;
  private float stateTime;

  private final ExplodeBehaviour explodeBehaviour;
  private final PowerUpBehaviour powerUpBehaviour;

  private boolean started;


  public AbstractAlienFollower(
      AlienCharacter character,
      float x,
      float y,
      int energy,
      FireBehaviour fireBehaviour,
      PowerUpBehaviour powerUpBehaviour,
      SpawnBehaviour spawnBehaviour,
      HitBehaviour hitBehaviour,
      ExplodeBehaviour explodeBehaviour,
      SpinningBehaviour spinningBehaviour,
      final int alienMoveInPixels,
      int minimumDistance) {

    super(
        character,
        x,
        y,
        energy,
        fireBehaviour,
        powerUpBehaviour,
        spawnBehaviour,
        hitBehaviour,
        explodeBehaviour,
        spinningBehaviour);

    this.hitBehaviour = hitBehaviour;
    this.explodeBehaviour = explodeBehaviour;
    this.powerUpBehaviour = powerUpBehaviour;
    this.stateTime = 0f;
    this.started = false;
    this.alienMoveInPixels = alienMoveInPixels;
    this.minimumAllowedDistanceFromFollowedSquared = minimumDistance * minimumDistance;
  }

  /**
   * Update position of follower based on alien this is following. The follower will attempt to
   * follow the alien in front but will throttle it's speed if it gets too close.
   */
  @Override
  public void follow(IAlien alienFollowed, float deltaTime) {

    if (!started) {
      activate();
      this.started = true;
    }

    // calculate angle from this follower to the alien we are following
    float newAngle = (float) Math.atan2(
        alienFollowed.y() - y(),
        alienFollowed.x() - x());

    // calculate the deltas to be applied each move
    float xDelta = alienMoveInPixels * (float) Math.cos(newAngle);
    float yDelta = alienMoveInPixels * (float) Math.sin(newAngle);

    // calculate new position
    float newX = x() + (xDelta * deltaTime);
    float newY = y() + (yDelta * deltaTime);

    // calculate squared distance from alien we are following to new position
    float distX = alienFollowed.x() - newX;
    float distY = alienFollowed.y() - newY;
    float distanceFromFollowedSquared = (distX * distX) + (distY * distY);

    if (distanceFromFollowedSquared > minimumAllowedDistanceFromFollowedSquared) {
      move(newX, newY);
    } else {
      /*
       * we are too close to the followed alien.
       * we must throttle our speed and calculate a revised new position.
       */

      // calculate distance from our current position to planned new position
      float plannedMoveX = (newX - x());
      float plannedMoveY = (newY - y());
      float plannedMoveDistanceSquared =
          (plannedMoveX * plannedMoveX) + (plannedMoveY * plannedMoveY);

      // calculate how much we should reduce our planned move by to
      float reduceMoveDistanceSquared =
          minimumAllowedDistanceFromFollowedSquared - distanceFromFollowedSquared;

      float throttleRatio;
      if (plannedMoveDistanceSquared < reduceMoveDistanceSquared) {
        // handles small planned moves
        throttleRatio = distanceFromFollowedSquared / minimumAllowedDistanceFromFollowedSquared;
      } else {
        // handles large planed moves
        // normally only needed when there's been a big timing delay and followed alien has moved a long way
        throttleRatio =
            (plannedMoveDistanceSquared - reduceMoveDistanceSquared) / plannedMoveDistanceSquared;
      }

      // calculate new position based on reduced speed
      float reducedXDelta = xDelta * throttleRatio;
      float reducedYDelta = yDelta * throttleRatio;

      // move alien
      moveByDelta(
          (reducedXDelta * deltaTime),
          (reducedYDelta * deltaTime));
    }
  }

  /**
   * Update local instance of stateTime to keep in sync with parent
   */
  @Override
  public void animate(float deltaTime) {
    stateTime += deltaTime;
    super.animate(deltaTime);
  }

  /**
   * Show graphical hit in alien follower without impacting it's energy. Called if followable alien
   * was hit and passed onto this follower. We hit silently to allow followable alien to handle
   * sound/vibration.
   */
  @Override
  public void showHit() {
    hitBehaviour.startHitFollower(stateTime);
  }

  /**
   * Show graphical explosion in alien follower. Called if followable alien explodes and passed onto
   * this follower. We hit silently to allow followable alien to handle sound/vibration.
   */
  @Override
  public void showExplode() {
    explodeBehaviour.startExplosionFollower(this);
    state = EXPLODING;
    powerUpBehaviour.releasePowerUp(this);
  }
}
