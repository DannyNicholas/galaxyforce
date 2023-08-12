package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.aliens.implementations.helpers.BoundariesChecker;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.types.HunterConfig;

import lombok.NonNull;

/**
 * Alien hunter that will attempt to crash into the base.
 */
public class HunterAlien extends AbstractAlien {

  /* distance alien can move each cycle in pixels each second */
  private final int speedInPixelsPerSecond;

  /* time delay between alien direction changes */
  private static final float ALIEN_DIRECTION_CHANGE_DELAY = 0.1f;

  /* maximum alien change direction in radians */
  private static final float MAX_DIRECTION_CHANGE_ANGLE = 0.3f;

  /* angle from alien to base */
  private float angle;

  /* how many seconds to delay before alien starts to follow path */
  private float timeDelayStart;

  /* variable to store time passed since last alien direction change */
  private float timeSinceLastDirectionChange;

  private final GameModel model;

  // represents the boundaries the alien can fly within
  private final BoundariesChecker boundariesChecker;

  /**
   * Create Alien Hunter.
   */
  public HunterAlien(
      @NonNull final ExplosionBehaviourFactory explosionFactory,
      @NonNull final SpawnBehaviourFactory spawnFactory,
      @NonNull final SpinningBehaviourFactory spinningFactory,
      @NonNull final PowerUpBehaviourFactory powerUpFactory,
      @NonNull final FireBehaviourFactory fireFactory,
      @NonNull final HitBehaviourFactory hitFactory,
      @NonNull final GameModel model,
      @NonNull final HunterConfig alienConfig,
      final PowerUpType powerUpType,
      @NonNull final Float xStart,
      @NonNull final Float yStart,
      @NonNull final Float timeDelayStart) {

    super(
        alienConfig.getAlienCharacter(),
        xStart,
        yStart,
        alienConfig.getEnergy(),
        fireFactory.createFireBehaviour(
            alienConfig.getMissileConfig()),
        powerUpFactory.createPowerUpBehaviour(
            powerUpType),
        spawnFactory.createSpawnBehaviour(
            alienConfig.getSpawnConfig()),
        hitFactory.createHitBehaviour(),
        explosionFactory.createExplosionBehaviour(
            alienConfig.getExplosionConfig(),
            alienConfig.getAlienCharacter()),
        spinningFactory.createSpinningBehaviour(
            alienConfig.getSpinningConfig(),
            alienConfig.getSpeed()));

    this.boundariesChecker = new BoundariesChecker(
        this,
        alienConfig.getBoundaries().getMinX(),
        alienConfig.getBoundaries().getMaxX(),
        alienConfig.getBoundaries().getMinY(),
        alienConfig.getBoundaries().getMaxY(),
        alienConfig.getBoundaries().getLanePolicy(),
        alienConfig.getBoundaries().getLanes());

    waiting();

    this.model = model;

    // set positional and movement behaviour
    this.timeDelayStart = timeDelayStart;

    // reset timer since last alien direction change
    timeSinceLastDirectionChange = 0f;

    // set starting direction angle
    this.angle = recalculateAngle(0f);

    this.speedInPixelsPerSecond = alienConfig.getSpeed().getSpeedInPixelsPerSeconds();
  }

  public static HunterAlienBuilder builder() {
    return new HunterAlienBuilder();
  }

  @Override
  public void animate(float deltaTime) {

    super.animate(deltaTime);

    /* if active then alien can move */
    if (isActive()) {
      timeSinceLastDirectionChange += deltaTime;

      /*
       * Guide alien every x seconds so the alien changes direction to
       * follow any changes in the base's position.
       */
      if (timeSinceLastDirectionChange > ALIEN_DIRECTION_CHANGE_DELAY) {

        // recalculate direction angle
        this.angle = recalculateAngle(this.angle);

        // reset timer since last missile direction change
        timeSinceLastDirectionChange = 0f;
      }

      // calculate the deltas to be applied each move
      float xDelta = speedInPixelsPerSecond * (float) Math.cos(this.angle);
      float yDelta = speedInPixelsPerSecond * (float) Math.sin(this.angle);

      // move alien by calculated deltas
      moveByDelta(
          xDelta * deltaTime,
          yDelta * deltaTime);

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

  /**
   * Recalculate the angle direction of the alien so it heads towards base.
   */
  private float recalculateAngle(float angle) {

    IBasePrimary base = model.getBase();
    if (base != null) {

      // calculate angle from alien position to base
      float newAngle = (float) Math.atan2(base.y() - y(), base.x() - x());

      // if alien is outside wanted boundaries (e.g. off-screen), return it back immediately (can get lost!).
      // calculates new angle to take it to the centre of it's boundaries
      if (boundariesChecker.isOutsideBoundaries()) {
        return (float) Math.atan2(boundariesChecker.centreY() - y(),
            boundariesChecker.centreX() - x());
      }

      // don't allow sudden changes of direction. limit to MAX radians.
      if ((newAngle - angle) > MAX_DIRECTION_CHANGE_ANGLE) {
        return angle + MAX_DIRECTION_CHANGE_ANGLE;
      }
      if ((newAngle - angle) < MAX_DIRECTION_CHANGE_ANGLE) {
        return angle - MAX_DIRECTION_CHANGE_ANGLE;
      }

      // otherwise return calculated angle.
      return newAngle;
    }

    return angle;
  }

  public static class HunterAlienBuilder {
    private @NonNull ExplosionBehaviourFactory explosionFactory;
    private @NonNull SpawnBehaviourFactory spawnFactory;
    private @NonNull SpinningBehaviourFactory spinningFactory;
    private @NonNull PowerUpBehaviourFactory powerUpFactory;
    private @NonNull FireBehaviourFactory fireFactory;
    private @NonNull HitBehaviourFactory hitFactory;
    private @NonNull GameModel model;
    private @NonNull HunterConfig alienConfig;
    private PowerUpType powerUpType;
    private @NonNull Float xStart;
    private @NonNull Float yStart;
    private @NonNull Float timeDelayStart;

    HunterAlienBuilder() {
    }

    public HunterAlienBuilder explosionFactory(@NonNull ExplosionBehaviourFactory explosionFactory) {
      this.explosionFactory = explosionFactory;
      return this;
    }

    public HunterAlienBuilder spawnFactory(@NonNull SpawnBehaviourFactory spawnFactory) {
      this.spawnFactory = spawnFactory;
      return this;
    }

    public HunterAlienBuilder spinningFactory(@NonNull SpinningBehaviourFactory spinningFactory) {
      this.spinningFactory = spinningFactory;
      return this;
    }

    public HunterAlienBuilder powerUpFactory(@NonNull PowerUpBehaviourFactory powerUpFactory) {
      this.powerUpFactory = powerUpFactory;
      return this;
    }

    public HunterAlienBuilder fireFactory(@NonNull FireBehaviourFactory fireFactory) {
      this.fireFactory = fireFactory;
      return this;
    }

    public HunterAlienBuilder hitFactory(@NonNull HitBehaviourFactory hitFactory) {
      this.hitFactory = hitFactory;
      return this;
    }

    public HunterAlienBuilder model(@NonNull GameModel model) {
      this.model = model;
      return this;
    }

    public HunterAlienBuilder alienConfig(@NonNull HunterConfig alienConfig) {
      this.alienConfig = alienConfig;
      return this;
    }

    public HunterAlienBuilder powerUpType(PowerUpType powerUpType) {
      this.powerUpType = powerUpType;
      return this;
    }

    public HunterAlienBuilder xStart(@NonNull Float xStart) {
      this.xStart = xStart;
      return this;
    }

    public HunterAlienBuilder yStart(@NonNull Float yStart) {
      this.yStart = yStart;
      return this;
    }

    public HunterAlienBuilder timeDelayStart(@NonNull Float timeDelayStart) {
      this.timeDelayStart = timeDelayStart;
      return this;
    }

    public HunterAlien build() {
      return new HunterAlien(explosionFactory, spawnFactory, spinningFactory, powerUpFactory, fireFactory, hitFactory, model, alienConfig, powerUpType, xStart, yStart, timeDelayStart);
    }
  }
}
