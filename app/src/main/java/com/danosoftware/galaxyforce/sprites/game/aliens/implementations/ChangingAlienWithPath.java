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
import lombok.Builder;
import lombok.NonNull;

/**
 * Alien that changes its appearance after each hit.
 */
public class ChangingAlienWithPath extends AbstractAlienWithPath {

  private final ListIterator<AlienCharacterWithEnergy> characters;
  private int energyBeforeChange = 0;

  @Builder
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
}
