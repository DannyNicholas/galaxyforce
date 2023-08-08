package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.isTravellingOffScreen;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DirectionalDestroyableConfig;

import lombok.NonNull;

/**
 * Alien that moves from starting position across the screen in a set direction until it moves
 * off-screen. The alien will then be destroyed.
 */
public class DirectionalDestroyableAlien extends AbstractAlien {

  private final float startingX;
  private final float startingY;
  private final boolean restartImmediately;

  // how many seconds to delay before alien activates
  private float timeDelayStart;

  // offset applied to x and y every move
  private final float xDelta;
  private final float yDelta;

  public DirectionalDestroyableAlien(
      @NonNull final ExplosionBehaviourFactory explosionFactory,
      @NonNull final SpawnBehaviourFactory spawnFactory,
      @NonNull final SpinningBehaviourFactory spinningFactory,
      @NonNull final PowerUpBehaviourFactory powerUpFactory,
      @NonNull final FireBehaviourFactory fireFactory,
      @NonNull final HitBehaviourFactory hitFactory,
      @NonNull final DirectionalDestroyableConfig alienConfig,
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

    // calculate the deltas to be applied each move
    final int movePixelsPerSecond = alienConfig.getSpeed().getSpeedInPixelsPerSeconds();
    final float angle = alienConfig.getAngle();
    this.xDelta = movePixelsPerSecond * (float) Math.cos(angle);
    this.yDelta = movePixelsPerSecond * (float) Math.sin(angle);
  }

  public static DirectionalDestroyableAlienBuilder builder() {
    return new DirectionalDestroyableAlienBuilder();
  }

  @Override
  public void animate(float deltaTime) {
    super.animate(deltaTime);

    if (isActive()) {
      if (isTravellingOffScreen(this, xDelta, yDelta)) {
        if (restartImmediately) {
          move(startingX, startingY);
        } else {
          destroy();
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

  public static class DirectionalDestroyableAlienBuilder {
    private @NonNull ExplosionBehaviourFactory explosionFactory;
    private @NonNull SpawnBehaviourFactory spawnFactory;
    private @NonNull SpinningBehaviourFactory spinningFactory;
    private @NonNull PowerUpBehaviourFactory powerUpFactory;
    private @NonNull FireBehaviourFactory fireFactory;
    private @NonNull HitBehaviourFactory hitFactory;
    private @NonNull DirectionalDestroyableConfig alienConfig;
    private PowerUpType powerUpType;
    private @NonNull Float xStart;
    private @NonNull Float yStart;
    private @NonNull Float timeDelayStart;
    private @NonNull Boolean restartImmediately;

    DirectionalDestroyableAlienBuilder() {
    }

    public DirectionalDestroyableAlienBuilder explosionFactory(@NonNull ExplosionBehaviourFactory explosionFactory) {
      this.explosionFactory = explosionFactory;
      return this;
    }

    public DirectionalDestroyableAlienBuilder spawnFactory(@NonNull SpawnBehaviourFactory spawnFactory) {
      this.spawnFactory = spawnFactory;
      return this;
    }

    public DirectionalDestroyableAlienBuilder spinningFactory(@NonNull SpinningBehaviourFactory spinningFactory) {
      this.spinningFactory = spinningFactory;
      return this;
    }

    public DirectionalDestroyableAlienBuilder powerUpFactory(@NonNull PowerUpBehaviourFactory powerUpFactory) {
      this.powerUpFactory = powerUpFactory;
      return this;
    }

    public DirectionalDestroyableAlienBuilder fireFactory(@NonNull FireBehaviourFactory fireFactory) {
      this.fireFactory = fireFactory;
      return this;
    }

    public DirectionalDestroyableAlienBuilder hitFactory(@NonNull HitBehaviourFactory hitFactory) {
      this.hitFactory = hitFactory;
      return this;
    }

    public DirectionalDestroyableAlienBuilder alienConfig(@NonNull DirectionalDestroyableConfig alienConfig) {
      this.alienConfig = alienConfig;
      return this;
    }

    public DirectionalDestroyableAlienBuilder powerUpType(PowerUpType powerUpType) {
      this.powerUpType = powerUpType;
      return this;
    }

    public DirectionalDestroyableAlienBuilder xStart(@NonNull Float xStart) {
      this.xStart = xStart;
      return this;
    }

    public DirectionalDestroyableAlienBuilder yStart(@NonNull Float yStart) {
      this.yStart = yStart;
      return this;
    }

    public DirectionalDestroyableAlienBuilder timeDelayStart(@NonNull Float timeDelayStart) {
      this.timeDelayStart = timeDelayStart;
      return this;
    }

    public DirectionalDestroyableAlienBuilder restartImmediately(@NonNull Boolean restartImmediately) {
      this.restartImmediately = restartImmediately;
      return this;
    }

    public DirectionalDestroyableAlien build() {
      return new DirectionalDestroyableAlien(explosionFactory, spawnFactory, spinningFactory, powerUpFactory, fireFactory, hitFactory, alienConfig, powerUpType, xStart, yStart, timeDelayStart, restartImmediately);
    }
  }
}
