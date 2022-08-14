package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlienFollower;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlienFollower;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.types.FollowerConfig;
import lombok.Builder;
import lombok.NonNull;

/**
 * Alien that is one follower in a chain of following aliens. Followers will attempt to follow a
 * followable alien (normally the head of the chain).
 */
public class FollowerAlien extends AbstractAlienFollower implements IAlienFollower {

  /* minimum distance between follower bodies */
  private static final int MIN_DISTANCE = 25;

  @Builder
  public FollowerAlien(
      @NonNull final ExplosionBehaviourFactory explosionFactory,
      @NonNull final SpawnBehaviourFactory spawnFactory,
      @NonNull final SpinningBehaviourFactory spinningFactory,
      @NonNull final PowerUpBehaviourFactory powerUpFactory,
      @NonNull final FireBehaviourFactory fireFactory,
      @NonNull final HitBehaviourFactory hitFactory,
      @NonNull final FollowerConfig alienConfig,
      final PowerUpType powerUpType,
      @NonNull final Float xStart,
      @NonNull final Float yStart) {

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
            alienConfig.getSpeed()),
        alienConfig
            .getSpeed()
            .getSpeedInPixelsPerSeconds(),
        MIN_DISTANCE);
  }
}
