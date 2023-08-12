package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.assets.AlienMissilesDto;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienMissileFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.types.ExplodingConfig;

import lombok.NonNull;

/**
 * Alien that stays in a fixed position once spawned for a defined duration and then explodes by
 * spraying missiles in all directions.
 */
public class ExplodingAlien extends AbstractAlien {

  /* how many seconds before bomb explodes */
  private final float timeBeforeExplosion;

  // exploding missile
  private final AlienMissileCharacter explodingMissileCharacter;

  private final GameModel model;

  /* variable to store time passed */
  private float timer;

  private boolean isExploding;

  public ExplodingAlien(
      @NonNull final ExplosionBehaviourFactory explosionFactory,
      @NonNull final SpawnBehaviourFactory spawnFactory,
      @NonNull final SpinningBehaviourFactory spinningFactory,
      @NonNull final PowerUpBehaviourFactory powerUpFactory,
      @NonNull final FireBehaviourFactory fireFactory,
      @NonNull final HitBehaviourFactory hitFactory,
      @NonNull final GameModel model,
      @NonNull final ExplodingConfig alienConfig,
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
            alienConfig.getSpinningConfig()));

    this.model = model;
    this.timeBeforeExplosion = alienConfig.getExplosionTime();
    this.explodingMissileCharacter = alienConfig.getExplodingMissileCharacter();

    // reset timer
    timer = 0f;
    isExploding = false;
  }

  public static ExplodingAlienBuilder builder() {
    return new ExplodingAlienBuilder();
  }

  @Override
  public void animate(float deltaTime) {

    super.animate(deltaTime);

    if (!isExploding) {
      timer += deltaTime;
      if (timer > timeBeforeExplosion) {
        explode();
        // send missiles to model
        AlienMissilesDto missiles = AlienMissileFactory.createAlienMissile(
            model.getBase(),
            this,
            AlienMissileType.SPRAY,
            AlienMissileSpeed.MEDIUM,
            explodingMissileCharacter);
        model.fireAlienMissiles(missiles);
        isExploding = true;
      }
    }
  }

  public static class ExplodingAlienBuilder {
    private @NonNull ExplosionBehaviourFactory explosionFactory;
    private @NonNull SpawnBehaviourFactory spawnFactory;
    private @NonNull SpinningBehaviourFactory spinningFactory;
    private @NonNull PowerUpBehaviourFactory powerUpFactory;
    private @NonNull FireBehaviourFactory fireFactory;
    private @NonNull HitBehaviourFactory hitFactory;
    private @NonNull GameModel model;
    private @NonNull ExplodingConfig alienConfig;
    private PowerUpType powerUpType;
    private @NonNull Float xStart;
    private @NonNull Float yStart;

    ExplodingAlienBuilder() {
    }

    public ExplodingAlienBuilder explosionFactory(@NonNull ExplosionBehaviourFactory explosionFactory) {
      this.explosionFactory = explosionFactory;
      return this;
    }

    public ExplodingAlienBuilder spawnFactory(@NonNull SpawnBehaviourFactory spawnFactory) {
      this.spawnFactory = spawnFactory;
      return this;
    }

    public ExplodingAlienBuilder spinningFactory(@NonNull SpinningBehaviourFactory spinningFactory) {
      this.spinningFactory = spinningFactory;
      return this;
    }

    public ExplodingAlienBuilder powerUpFactory(@NonNull PowerUpBehaviourFactory powerUpFactory) {
      this.powerUpFactory = powerUpFactory;
      return this;
    }

    public ExplodingAlienBuilder fireFactory(@NonNull FireBehaviourFactory fireFactory) {
      this.fireFactory = fireFactory;
      return this;
    }

    public ExplodingAlienBuilder hitFactory(@NonNull HitBehaviourFactory hitFactory) {
      this.hitFactory = hitFactory;
      return this;
    }

    public ExplodingAlienBuilder model(@NonNull GameModel model) {
      this.model = model;
      return this;
    }

    public ExplodingAlienBuilder alienConfig(@NonNull ExplodingConfig alienConfig) {
      this.alienConfig = alienConfig;
      return this;
    }

    public ExplodingAlienBuilder powerUpType(PowerUpType powerUpType) {
      this.powerUpType = powerUpType;
      return this;
    }

    public ExplodingAlienBuilder xStart(@NonNull Float xStart) {
      this.xStart = xStart;
      return this;
    }

    public ExplodingAlienBuilder yStart(@NonNull Float yStart) {
      this.yStart = yStart;
      return this;
    }

    public ExplodingAlien build() {
      return new ExplodingAlien(explosionFactory, spawnFactory, spinningFactory, powerUpFactory, fireFactory, hitFactory, model, alienConfig, powerUpType, xStart, yStart);
    }
  }
}
