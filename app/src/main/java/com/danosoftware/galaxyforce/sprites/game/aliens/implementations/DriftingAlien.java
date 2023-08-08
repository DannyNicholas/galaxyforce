package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenBottom;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenLeft;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenRight;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenTop;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DriftingConfig;

import lombok.NonNull;

/**
 * Alien that drifts from starting position across the screen. When it reaches the edge of the
 * screen, it reappears on the other side.
 */
public class DriftingAlien extends AbstractAlien {

  // offset applied to x and y every move
  private final float xDelta;
  private final float yDelta;

  /* how many seconds to delay before alien starts */
  private float timeDelayStart;

  public DriftingAlien(
      @NonNull final ExplosionBehaviourFactory explosionFactory,
      @NonNull final SpawnBehaviourFactory spawnFactory,
      @NonNull final SpinningBehaviourFactory spinningFactory,
      @NonNull final PowerUpBehaviourFactory powerUpFactory,
      @NonNull final FireBehaviourFactory fireFactory,
      @NonNull final HitBehaviourFactory hitFactory,
      @NonNull final DriftingConfig alienConfig,
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

    waiting();

    // set positional and movement behaviour
    this.timeDelayStart = timeDelayStart;

    // calculate the deltas to be applied each move
    final int movePixelsPerSecond = alienConfig.getSpeed().getSpeedInPixelsPerSeconds();
    final float angle = alienConfig.getAngle();

    this.xDelta = movePixelsPerSecond * (float) Math.cos(angle);
    this.yDelta = movePixelsPerSecond * (float) Math.sin(angle);
  }

  public static DriftingAlienBuilder builder() {
    return new DriftingAlienBuilder();
  }

  @Override
  public void animate(float deltaTime) {
    super.animate(deltaTime);

    /* if active then alien can move */
    if (isActive()) {

      // move alien by calculated deltas
      moveByDelta(
          xDelta * deltaTime,
          yDelta * deltaTime);

      // test if alien is off the screen and is continuing to move
      // away. In that case, shift aline to the opposite side of screen
      // so it will re-appear on the opposite side.
      if (offScreenLeft(this) && xDelta < 0) {
        moveXByDelta(GAME_WIDTH - x() + halfWidth());
      }
      if (offScreenRight(this) && xDelta > 0) {
        moveXByDelta(-GAME_WIDTH - ((x() + halfWidth()) % GAME_WIDTH));
      }
      if (offScreenBottom(this) && yDelta < 0) {
        moveYByDelta(GAME_HEIGHT - y() + halfHeight());
      }
      if (offScreenTop(this) && yDelta > 0) {
        moveYByDelta(-GAME_HEIGHT - ((y() + halfHeight()) % GAME_HEIGHT));
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

  public static class DriftingAlienBuilder {
    private @NonNull ExplosionBehaviourFactory explosionFactory;
    private @NonNull SpawnBehaviourFactory spawnFactory;
    private @NonNull SpinningBehaviourFactory spinningFactory;
    private @NonNull PowerUpBehaviourFactory powerUpFactory;
    private @NonNull FireBehaviourFactory fireFactory;
    private @NonNull HitBehaviourFactory hitFactory;
    private @NonNull DriftingConfig alienConfig;
    private PowerUpType powerUpType;
    private @NonNull Float xStart;
    private @NonNull Float yStart;
    private @NonNull Float timeDelayStart;

    DriftingAlienBuilder() {
    }

    public DriftingAlienBuilder explosionFactory(@NonNull ExplosionBehaviourFactory explosionFactory) {
      this.explosionFactory = explosionFactory;
      return this;
    }

    public DriftingAlienBuilder spawnFactory(@NonNull SpawnBehaviourFactory spawnFactory) {
      this.spawnFactory = spawnFactory;
      return this;
    }

    public DriftingAlienBuilder spinningFactory(@NonNull SpinningBehaviourFactory spinningFactory) {
      this.spinningFactory = spinningFactory;
      return this;
    }

    public DriftingAlienBuilder powerUpFactory(@NonNull PowerUpBehaviourFactory powerUpFactory) {
      this.powerUpFactory = powerUpFactory;
      return this;
    }

    public DriftingAlienBuilder fireFactory(@NonNull FireBehaviourFactory fireFactory) {
      this.fireFactory = fireFactory;
      return this;
    }

    public DriftingAlienBuilder hitFactory(@NonNull HitBehaviourFactory hitFactory) {
      this.hitFactory = hitFactory;
      return this;
    }

    public DriftingAlienBuilder alienConfig(@NonNull DriftingConfig alienConfig) {
      this.alienConfig = alienConfig;
      return this;
    }

    public DriftingAlienBuilder powerUpType(PowerUpType powerUpType) {
      this.powerUpType = powerUpType;
      return this;
    }

    public DriftingAlienBuilder xStart(@NonNull Float xStart) {
      this.xStart = xStart;
      return this;
    }

    public DriftingAlienBuilder yStart(@NonNull Float yStart) {
      this.yStart = yStart;
      return this;
    }

    public DriftingAlienBuilder timeDelayStart(@NonNull Float timeDelayStart) {
      this.timeDelayStart = timeDelayStart;
      return this;
    }

    public DriftingAlien build() {
      return new DriftingAlien(explosionFactory, spawnFactory, spinningFactory, powerUpFactory, fireFactory, hitFactory, alienConfig, powerUpType, xStart, yStart, timeDelayStart);
    }
  }
}
