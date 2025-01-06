package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.paths.PathPoint;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlienWithIntroductoryPath;
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
 * Alien that follows a pre-determined introductory path and then a repeating path.
 */
public class IntroductoryPathAlien extends AbstractAlienWithIntroductoryPath {

  public IntroductoryPathAlien(
      @NonNull final ExplosionBehaviourFactory explosionFactory,
      @NonNull final SpawnBehaviourFactory spawnFactory,
      @NonNull final SpinningBehaviourFactory spinningFactory,
      @NonNull final PowerUpBehaviourFactory powerUpFactory,
      @NonNull final FireBehaviourFactory fireFactory,
      @NonNull final HitBehaviourFactory hitFactory,
      @NonNull final PathConfig alienConfig,
      final PowerUpType powerUpType,
      @NonNull final List<PathPoint> introductoryPath,
      @NonNull final List<PathPoint> repeatingPath,
      @NonNull final Float delayStartTime) {
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
        introductoryPath,
        repeatingPath,
        delayStartTime,
        alienConfig.getEnergy(),
        alienConfig.getAngledToPath());
  }

  public static IntroductoryPathAlienBuilder builder() {
    return new IntroductoryPathAlienBuilder();
  }

  public static class IntroductoryPathAlienBuilder {

    private @NonNull ExplosionBehaviourFactory explosionFactory;
    private @NonNull SpawnBehaviourFactory spawnFactory;
    private @NonNull SpinningBehaviourFactory spinningFactory;
    private @NonNull PowerUpBehaviourFactory powerUpFactory;
    private @NonNull FireBehaviourFactory fireFactory;
    private @NonNull HitBehaviourFactory hitFactory;
    private @NonNull PathConfig alienConfig;
    private PowerUpType powerUpType;
    private @NonNull List<PathPoint> introductoryPath;
    private @NonNull List<PathPoint> repeatingPath;
    private @NonNull Float delayStartTime;

    IntroductoryPathAlienBuilder() {
    }

    public IntroductoryPathAlienBuilder explosionFactory(
        @NonNull ExplosionBehaviourFactory explosionFactory) {
      this.explosionFactory = explosionFactory;
      return this;
    }

    public IntroductoryPathAlienBuilder spawnFactory(@NonNull SpawnBehaviourFactory spawnFactory) {
      this.spawnFactory = spawnFactory;
      return this;
    }

    public IntroductoryPathAlienBuilder spinningFactory(
        @NonNull SpinningBehaviourFactory spinningFactory) {
      this.spinningFactory = spinningFactory;
      return this;
    }

    public IntroductoryPathAlienBuilder powerUpFactory(
        @NonNull PowerUpBehaviourFactory powerUpFactory) {
      this.powerUpFactory = powerUpFactory;
      return this;
    }

    public IntroductoryPathAlienBuilder fireFactory(@NonNull FireBehaviourFactory fireFactory) {
      this.fireFactory = fireFactory;
      return this;
    }

    public IntroductoryPathAlienBuilder hitFactory(@NonNull HitBehaviourFactory hitFactory) {
      this.hitFactory = hitFactory;
      return this;
    }

    public IntroductoryPathAlienBuilder alienConfig(@NonNull PathConfig alienConfig) {
      this.alienConfig = alienConfig;
      return this;
    }

    public IntroductoryPathAlienBuilder powerUpType(PowerUpType powerUpType) {
      this.powerUpType = powerUpType;
      return this;
    }

    public IntroductoryPathAlienBuilder introductoryPath(
        @NonNull List<PathPoint> introductoryPath) {
      this.introductoryPath = introductoryPath;
      return this;
    }

    public IntroductoryPathAlienBuilder repeatingPath(@NonNull List<PathPoint> repeatingPath) {
      this.repeatingPath = repeatingPath;
      return this;
    }

    public IntroductoryPathAlienBuilder delayStartTime(@NonNull Float delayStartTime) {
      this.delayStartTime = delayStartTime;
      return this;
    }

    public IntroductoryPathAlien build() {
      return new IntroductoryPathAlien(
          explosionFactory,
          spawnFactory,
          spinningFactory,
          powerUpFactory,
          fireFactory,
          hitFactory,
          alienConfig,
          powerUpType,
          introductoryPath,
          repeatingPath,
          delayStartTime);
    }
  }
}
