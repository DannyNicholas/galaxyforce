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
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;
import com.danosoftware.galaxyforce.waves.AlienCharacterWithEnergy;
import com.danosoftware.galaxyforce.waves.config.aliens.types.ChangingConfig;

import java.util.List;
import java.util.ListIterator;

import lombok.NonNull;

/**
 * Alien that changes its appearance after each hit.
 */
public class ChangingAlienWithPath extends AbstractAlienWithPath {

  private final ListIterator<AlienCharacterWithEnergy> characters;
  private int energyBeforeChange = 0;

  public ChangingAlienWithPath(
      @NonNull final ExplosionBehaviourFactory explosionFactory,
      @NonNull final SpawnBehaviourFactory spawnFactory,
      @NonNull final SpinningBehaviourFactory spinningFactory,
      @NonNull final PowerUpBehaviourFactory powerUpFactory,
      @NonNull final FireBehaviourFactory fireFactory,
      @NonNull final HitBehaviourFactory hitFactory,
      @NonNull final ChangingConfig alienConfig,
      final PowerUpType powerUpType,
      @NonNull final List<PathPoint> alienPath,
      @NonNull final Float delayStartTime,
      @NonNull final Boolean restartImmediately) {
    super(
        alienConfig.getFirstCharacter(),
        fireFactory.createFireBehaviour(
            alienConfig.getMissileConfig()),
        powerUpFactory.createPowerUpBehaviour(
            powerUpType),
        spawnFactory.createSpawnBehaviour(
            alienConfig.getSpawnConfig()),
        hitFactory.createHitBehaviour(),
        explosionFactory.createExplosionBehaviour(
            alienConfig.getExplosionConfig(),
            alienConfig.getFirstCharacter()),
        spinningFactory.createSpinningBehaviour(
            alienConfig.getSpinningConfig()),
        alienPath,
        delayStartTime,
        alienConfig.getEnergy(),
        restartImmediately,
        alienConfig.getAngledToPath());

    this.characters = alienConfig.getAlienCharacters().listIterator();
    if (characters.hasNext()) {
      AlienCharacterWithEnergy character = characters.next();
      changeCharacter(character.getAlienCharacter());
      energyBeforeChange = character.getEnergy();
    }
  }

  public static ChangingAlienWithPathBuilder builder() {
    return new ChangingAlienWithPathBuilder();
  }

  /**
   * If hit, switch to next animation. Used to change alien appearance each time it is hit.
   */
  @Override
  public void onHitBy(IBaseMissile baseMissile) {
    energyBeforeChange--;
    if (characters.hasNext() && energyBeforeChange <= 0) {
      AlienCharacterWithEnergy character = characters.next();
      changeCharacter(character.getAlienCharacter());
      energyBeforeChange = character.getEnergy();
    }

    super.onHitBy(baseMissile);
  }

  public static class ChangingAlienWithPathBuilder {
    private @NonNull ExplosionBehaviourFactory explosionFactory;
    private @NonNull SpawnBehaviourFactory spawnFactory;
    private @NonNull SpinningBehaviourFactory spinningFactory;
    private @NonNull PowerUpBehaviourFactory powerUpFactory;
    private @NonNull FireBehaviourFactory fireFactory;
    private @NonNull HitBehaviourFactory hitFactory;
    private @NonNull ChangingConfig alienConfig;
    private PowerUpType powerUpType;
    private @NonNull List<PathPoint> alienPath;
    private @NonNull Float delayStartTime;
    private @NonNull Boolean restartImmediately;

    ChangingAlienWithPathBuilder() {
    }

    public ChangingAlienWithPathBuilder explosionFactory(@NonNull ExplosionBehaviourFactory explosionFactory) {
      this.explosionFactory = explosionFactory;
      return this;
    }

    public ChangingAlienWithPathBuilder spawnFactory(@NonNull SpawnBehaviourFactory spawnFactory) {
      this.spawnFactory = spawnFactory;
      return this;
    }

    public ChangingAlienWithPathBuilder spinningFactory(@NonNull SpinningBehaviourFactory spinningFactory) {
      this.spinningFactory = spinningFactory;
      return this;
    }

    public ChangingAlienWithPathBuilder powerUpFactory(@NonNull PowerUpBehaviourFactory powerUpFactory) {
      this.powerUpFactory = powerUpFactory;
      return this;
    }

    public ChangingAlienWithPathBuilder fireFactory(@NonNull FireBehaviourFactory fireFactory) {
      this.fireFactory = fireFactory;
      return this;
    }

    public ChangingAlienWithPathBuilder hitFactory(@NonNull HitBehaviourFactory hitFactory) {
      this.hitFactory = hitFactory;
      return this;
    }

    public ChangingAlienWithPathBuilder alienConfig(@NonNull ChangingConfig alienConfig) {
      this.alienConfig = alienConfig;
      return this;
    }

    public ChangingAlienWithPathBuilder powerUpType(PowerUpType powerUpType) {
      this.powerUpType = powerUpType;
      return this;
    }

    public ChangingAlienWithPathBuilder alienPath(@NonNull List<PathPoint> alienPath) {
      this.alienPath = alienPath;
      return this;
    }

    public ChangingAlienWithPathBuilder delayStartTime(@NonNull Float delayStartTime) {
      this.delayStartTime = delayStartTime;
      return this;
    }

    public ChangingAlienWithPathBuilder restartImmediately(@NonNull Boolean restartImmediately) {
      this.restartImmediately = restartImmediately;
      return this;
    }

    public ChangingAlienWithPath build() {
      return new ChangingAlienWithPath(explosionFactory, spawnFactory, spinningFactory, powerUpFactory, fireFactory, hitFactory, alienConfig, powerUpType, alienPath, delayStartTime, restartImmediately);
    }
  }
}
