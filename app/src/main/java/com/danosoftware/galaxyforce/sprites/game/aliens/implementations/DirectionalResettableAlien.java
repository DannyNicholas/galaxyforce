package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.isTravellingOffScreen;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractResettableAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DirectionalResettableConfig;

import lombok.NonNull;

/**
 * Alien that moves from starting position across the screen in a set direction until it moves
 * off-screen. The alien will then be reset and replayed.
 */
public class DirectionalResettableAlien extends AbstractResettableAlien {

  private final float startingX;
  private final float startingY;
  private final boolean restartImmediately;

  /* how many seconds to delay before alien starts to follow path */
  private float timeDelayStart;

  /* original time delay before alien starts in cases where alien is reset */
  private final float originalTimeDelayStart;

  // offset applied to x and y every move
  private final float xDelta;
  private final float yDelta;

  public DirectionalResettableAlien(
      @NonNull final ExplosionBehaviourFactory explosionFactory,
      @NonNull final SpawnBehaviourFactory spawnFactory,
      @NonNull final SpinningBehaviourFactory spinningFactory,
      @NonNull final PowerUpBehaviourFactory powerUpFactory,
      @NonNull final FireBehaviourFactory fireFactory,
      @NonNull final HitBehaviourFactory hitFactory,
      @NonNull final DirectionalResettableConfig alienConfig,
      final PowerUpType powerUpType,
      @NonNull final Float xStart,
      @NonNull final Float yStart,
      @NonNull final Float timeDelayStart,
      @NonNull final Boolean restartImmediately) {

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
    this.startingX = x;
    this.startingY = y;
    this.restartImmediately = restartImmediately;
    this.timeDelayStart = timeDelayStart;
    this.originalTimeDelayStart = timeDelayStart;

    // calculate the deltas to be applied each move
    final int movePixelsPerSecond = alienConfig.getSpeed().getSpeedInPixelsPerSeconds();
    final float angle = alienConfig.getAngle();
    this.xDelta = movePixelsPerSecond * (float) Math.cos(angle);
    this.yDelta = movePixelsPerSecond * (float) Math.sin(angle);
  }

  public static DirectionalResettableAlienBuilder builder() {
    return new DirectionalResettableAlienBuilder();
  }

  @Override
  public void animate(float deltaTime) {
    super.animate(deltaTime);

    if (isActive()) {
      if (isTravellingOffScreen(this, xDelta, yDelta)) {
        if (restartImmediately) {
          reset(originalTimeDelayStart);
        } else {
          endOfPass();
        }
      }
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
   * Resets alien if the alien has gone off-screen without being destroyed and sub-wave needs to be
   * repeated.
   * <p>
   * Reduce original delay by supplied offset in case alien needs to start earlier.
   */
  @Override
  public void reset(float offset) {
    timeDelayStart = originalTimeDelayStart - offset;
    waiting();

    /*
     * reset back at start position - will be made visible and active before
     * recalculating it's position.
     */
    move(startingX, startingY);
  }

  /**
   * Get the original time delay. Can be used to calculate a corrected time delay offset.
   */
  @Override
  public float getTimeDelay() {
    return originalTimeDelayStart;
  }

  public static class DirectionalResettableAlienBuilder {
    private @NonNull ExplosionBehaviourFactory explosionFactory;
    private @NonNull SpawnBehaviourFactory spawnFactory;
    private @NonNull SpinningBehaviourFactory spinningFactory;
    private @NonNull PowerUpBehaviourFactory powerUpFactory;
    private @NonNull FireBehaviourFactory fireFactory;
    private @NonNull HitBehaviourFactory hitFactory;
    private @NonNull DirectionalResettableConfig alienConfig;
    private PowerUpType powerUpType;
    private @NonNull Float xStart;
    private @NonNull Float yStart;
    private @NonNull Float timeDelayStart;
    private @NonNull Boolean restartImmediately;

    DirectionalResettableAlienBuilder() {
    }

    public DirectionalResettableAlienBuilder explosionFactory(@NonNull ExplosionBehaviourFactory explosionFactory) {
      this.explosionFactory = explosionFactory;
      return this;
    }

    public DirectionalResettableAlienBuilder spawnFactory(@NonNull SpawnBehaviourFactory spawnFactory) {
      this.spawnFactory = spawnFactory;
      return this;
    }

    public DirectionalResettableAlienBuilder spinningFactory(@NonNull SpinningBehaviourFactory spinningFactory) {
      this.spinningFactory = spinningFactory;
      return this;
    }

    public DirectionalResettableAlienBuilder powerUpFactory(@NonNull PowerUpBehaviourFactory powerUpFactory) {
      this.powerUpFactory = powerUpFactory;
      return this;
    }

    public DirectionalResettableAlienBuilder fireFactory(@NonNull FireBehaviourFactory fireFactory) {
      this.fireFactory = fireFactory;
      return this;
    }

    public DirectionalResettableAlienBuilder hitFactory(@NonNull HitBehaviourFactory hitFactory) {
      this.hitFactory = hitFactory;
      return this;
    }

    public DirectionalResettableAlienBuilder alienConfig(@NonNull DirectionalResettableConfig alienConfig) {
      this.alienConfig = alienConfig;
      return this;
    }

    public DirectionalResettableAlienBuilder powerUpType(PowerUpType powerUpType) {
      this.powerUpType = powerUpType;
      return this;
    }

    public DirectionalResettableAlienBuilder xStart(@NonNull Float xStart) {
      this.xStart = xStart;
      return this;
    }

    public DirectionalResettableAlienBuilder yStart(@NonNull Float yStart) {
      this.yStart = yStart;
      return this;
    }

    public DirectionalResettableAlienBuilder timeDelayStart(@NonNull Float timeDelayStart) {
      this.timeDelayStart = timeDelayStart;
      return this;
    }

    public DirectionalResettableAlienBuilder restartImmediately(@NonNull Boolean restartImmediately) {
      this.restartImmediately = restartImmediately;
      return this;
    }

    public DirectionalResettableAlien build() {
      return new DirectionalResettableAlien(explosionFactory, spawnFactory, spinningFactory, powerUpFactory, fireFactory, hitFactory, alienConfig, powerUpType, xStart, yStart, timeDelayStart, restartImmediately);
    }
  }
}
