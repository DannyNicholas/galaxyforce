package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.config.aliens.types.StaticExplosionConfig;

import lombok.NonNull;

/**
 * Static explosion that stays in a fixed position. Normally used in multi-explosions where
 * explosions are spawned around an alien.
 */
public class StaticExplosion extends AbstractAlien {

  public StaticExplosion(
      @NonNull final ExplosionBehaviourFactory explosionFactory,
      @NonNull final SpawnBehaviourFactory spawnFactory,
      @NonNull final SpinningBehaviourFactory spinningFactory,
      @NonNull final PowerUpBehaviourFactory powerUpFactory,
      @NonNull final FireBehaviourFactory fireFactory,
      @NonNull final HitBehaviourFactory hitFactory,
      @NonNull final StaticExplosionConfig alienConfig,
      @NonNull final Float x,
      @NonNull final Float y) {

    super(
        AlienCharacter.NULL,
        x,
        y,
        0,
        fireFactory.createFireBehaviour(null),
        powerUpFactory.createPowerUpBehaviour(null),
        spawnFactory.createSpawnBehaviour(null),
        hitFactory.createHitBehaviour(),
        explosionFactory.createExplosionBehaviour(
            alienConfig.getExplosionConfig(),
            alienConfig.getAlienCharacter()),
        spinningFactory.createSpinningBehaviour(null));
  }

  public static StaticExplosionBuilder builder() {
    return new StaticExplosionBuilder();
  }

  public static class StaticExplosionBuilder {
    private @NonNull ExplosionBehaviourFactory explosionFactory;
    private @NonNull SpawnBehaviourFactory spawnFactory;
    private @NonNull SpinningBehaviourFactory spinningFactory;
    private @NonNull PowerUpBehaviourFactory powerUpFactory;
    private @NonNull FireBehaviourFactory fireFactory;
    private @NonNull HitBehaviourFactory hitFactory;
    private @NonNull StaticExplosionConfig alienConfig;
    private @NonNull Float x;
    private @NonNull Float y;

    StaticExplosionBuilder() {
    }

    public StaticExplosionBuilder explosionFactory(@NonNull ExplosionBehaviourFactory explosionFactory) {
      this.explosionFactory = explosionFactory;
      return this;
    }

    public StaticExplosionBuilder spawnFactory(@NonNull SpawnBehaviourFactory spawnFactory) {
      this.spawnFactory = spawnFactory;
      return this;
    }

    public StaticExplosionBuilder spinningFactory(@NonNull SpinningBehaviourFactory spinningFactory) {
      this.spinningFactory = spinningFactory;
      return this;
    }

    public StaticExplosionBuilder powerUpFactory(@NonNull PowerUpBehaviourFactory powerUpFactory) {
      this.powerUpFactory = powerUpFactory;
      return this;
    }

    public StaticExplosionBuilder fireFactory(@NonNull FireBehaviourFactory fireFactory) {
      this.fireFactory = fireFactory;
      return this;
    }

    public StaticExplosionBuilder hitFactory(@NonNull HitBehaviourFactory hitFactory) {
      this.hitFactory = hitFactory;
      return this;
    }

    public StaticExplosionBuilder alienConfig(@NonNull StaticExplosionConfig alienConfig) {
      this.alienConfig = alienConfig;
      return this;
    }

    public StaticExplosionBuilder x(@NonNull Float x) {
      this.x = x;
      return this;
    }

    public StaticExplosionBuilder y(@NonNull Float y) {
      this.y = y;
      return this;
    }

    public StaticExplosion build() {
      return new StaticExplosion(explosionFactory, spawnFactory, spinningFactory, powerUpFactory, fireFactory, hitFactory, alienConfig, x, y);
    }
  }
}
