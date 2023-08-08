package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.paths.PathPoint;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlienWithPath;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.types.PathConfig;

import java.util.List;

import lombok.NonNull;

/**
 * Alien that follows a pre-determined path
 */
public class PathAlien extends AbstractAlienWithPath {

  public PathAlien(
      @NonNull final ExplosionBehaviourFactory explosionFactory,
      @NonNull final SpawnBehaviourFactory spawnFactory,
      @NonNull final SpinningBehaviourFactory spinningFactory,
      @NonNull final PowerUpBehaviourFactory powerUpFactory,
      @NonNull final FireBehaviourFactory fireFactory,
      @NonNull final HitBehaviourFactory hitFactory,
      @NonNull final PathConfig alienConfig,
      final PowerUpType powerUpType,
      @NonNull final List<PathPoint> alienPath,
      @NonNull final Float delayStartTime,
      @NonNull final Boolean restartImmediately) {
    super(
        alienConfig.getAlienCharacter(),
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
            alienConfig.getSpinningConfig()),
        alienPath,
        delayStartTime,
        alienConfig.getEnergy(),
        restartImmediately,
        alienConfig.getAngledToPath());
  }

  public static PathAlienBuilder builder() {
    return new PathAlienBuilder();
  }

  public static class PathAlienBuilder {
    private @NonNull ExplosionBehaviourFactory explosionFactory;
    private @NonNull SpawnBehaviourFactory spawnFactory;
    private @NonNull SpinningBehaviourFactory spinningFactory;
    private @NonNull PowerUpBehaviourFactory powerUpFactory;
    private @NonNull FireBehaviourFactory fireFactory;
    private @NonNull HitBehaviourFactory hitFactory;
    private @NonNull PathConfig alienConfig;
    private PowerUpType powerUpType;
    private @NonNull List<PathPoint> alienPath;
    private @NonNull Float delayStartTime;
    private @NonNull Boolean restartImmediately;

    PathAlienBuilder() {
    }

    public PathAlienBuilder explosionFactory(@NonNull ExplosionBehaviourFactory explosionFactory) {
      this.explosionFactory = explosionFactory;
      return this;
    }

    public PathAlienBuilder spawnFactory(@NonNull SpawnBehaviourFactory spawnFactory) {
      this.spawnFactory = spawnFactory;
      return this;
    }

    public PathAlienBuilder spinningFactory(@NonNull SpinningBehaviourFactory spinningFactory) {
      this.spinningFactory = spinningFactory;
      return this;
    }

    public PathAlienBuilder powerUpFactory(@NonNull PowerUpBehaviourFactory powerUpFactory) {
      this.powerUpFactory = powerUpFactory;
      return this;
    }

    public PathAlienBuilder fireFactory(@NonNull FireBehaviourFactory fireFactory) {
      this.fireFactory = fireFactory;
      return this;
    }

    public PathAlienBuilder hitFactory(@NonNull HitBehaviourFactory hitFactory) {
      this.hitFactory = hitFactory;
      return this;
    }

    public PathAlienBuilder alienConfig(@NonNull PathConfig alienConfig) {
      this.alienConfig = alienConfig;
      return this;
    }

    public PathAlienBuilder powerUpType(PowerUpType powerUpType) {
      this.powerUpType = powerUpType;
      return this;
    }

    public PathAlienBuilder alienPath(@NonNull List<PathPoint> alienPath) {
      this.alienPath = alienPath;
      return this;
    }

    public PathAlienBuilder delayStartTime(@NonNull Float delayStartTime) {
      this.delayStartTime = delayStartTime;
      return this;
    }

    public PathAlienBuilder restartImmediately(@NonNull Boolean restartImmediately) {
      this.restartImmediately = restartImmediately;
      return this;
    }

    public PathAlien build() {
      return new PathAlien(explosionFactory, spawnFactory, spinningFactory, powerUpFactory, fireFactory, hitFactory, alienConfig, powerUpType, alienPath, delayStartTime, restartImmediately);
    }
  }
}
