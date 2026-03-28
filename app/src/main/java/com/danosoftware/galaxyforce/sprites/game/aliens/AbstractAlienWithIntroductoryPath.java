package com.danosoftware.galaxyforce.sprites.game.aliens;

import android.util.Log;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.flightpath.paths.PathPoint;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviour;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import java.util.List;

/**
 * Alien that has an introductory path (normally used to move alien onto screen). Once the
 * introductory path is completed, the alien will follow a repeating path until destroyed.
 */
public abstract class AbstractAlienWithIntroductoryPath extends AbstractAlien {

  private static final float TIME_PER_INDEX = 1f / 60f;

  private final List<PathPoint> introductoryPath;
  private final List<PathPoint> repeatingPath;

  /* how many seconds to delay before alien starts */
  private float timeDelayStart;

  /* how many seconds have passed since alien started current path */
  private float timeElapsed;

  /* should the alien be rotated to follow it's path */
  private final boolean rotated;

  private boolean isOnIntroductoryPath;

  protected AbstractAlienWithIntroductoryPath(
      AlienCharacter character,
      FireBehaviour fireBehaviour,
      PowerUpBehaviour powerUpBehaviour,
      SpawnBehaviour spawnBehaviour,
      HitBehaviour hitBehaviour,
      ExplodeBehaviour explodeBehaviour,
      SpinningBehaviour spinningBehaviour,
      List<PathPoint> introductoryPath,
      List<PathPoint> repeatingPath,
      float delayStart,
      int energy,
      boolean angledToPath) {

    super(
        character,
        introductoryPath.get(0).getX(),
        introductoryPath.get(0).getY(),
        energy,
        fireBehaviour,
        powerUpBehaviour,
        spawnBehaviour,
        hitBehaviour,
        explodeBehaviour,
        spinningBehaviour);

    this.introductoryPath = introductoryPath;
    this.repeatingPath = repeatingPath;
    this.timeDelayStart = delayStart;
    this.rotated = angledToPath;
    this.isOnIntroductoryPath = true;
    this.timeElapsed = 0f;

    waiting();
    PathPoint position = introductoryPath.get(0);
    move(position);
  }

  @Override
  public void animate(float deltaTime) {
    super.animate(deltaTime);

    /* if alien active then alien can move */
    if (isActive()) {
      timeElapsed += deltaTime;

      /*
       * calculate path index based on start time. assumption is that
       * aliens path advances 60 elements every second. Rounds to the
       * nearest whole number to keep movement as smooth as possible.
       */
      int index = Math.round(timeElapsed * 60f);

      /*
       * If we are still on introductory path, we will move the alien
       * to the next position until they reach the end of the path.
       */
      if (isOnIntroductoryPath) {
        if (index > introductoryPath.size() - 1) {
          Log.i(GameConstants.LOG_TAG,
              "Time: " + timeElapsed);
          isOnIntroductoryPath = false;
          timeElapsed = timeElapsed % TIME_PER_INDEX;
          Log.i(GameConstants.LOG_TAG,
              "Time: " + timeElapsed);
          index = Math.round(timeElapsed * 60f);
        } else {
          PathPoint position = introductoryPath.get(index);
          move(position);
          Log.i(GameConstants.LOG_TAG,
              "Time: " + timeElapsed + " - Index: " + index + "/" + (introductoryPath.size() - 1));
        }
      }

      /*
       * If we are still on the repeating path, we will continue to move the alien
       * to the next position. When they reach the end of the path,
       * we will recalculate their position at the beginning of the path.
       */
      if (!isOnIntroductoryPath) {
        if (index > repeatingPath.size() - 1) {
          timeElapsed = timeElapsed % TIME_PER_INDEX;
          index = Math.round(timeElapsed * 60f);
        }
        PathPoint position = repeatingPath.get(index);
        move(position);
        Log.i(GameConstants.LOG_TAG,
            "Time: " + timeElapsed + " - Index: " + index + "/" + (repeatingPath.size() - 1));
      }

    } else if (isWaiting()) {
      // countdown until activation time
      timeDelayStart -= deltaTime;

      // activate alien. can only happen once!
      if (timeDelayStart <= 0) {
        activate();
        animate(0 - timeDelayStart);
      }
    }
  }

  private void move(PathPoint position) {
    move(
        position.getX(),
        position.getY()
    );
    if (rotated) {
      rotate(position.getAngle());
    }
  }
}
