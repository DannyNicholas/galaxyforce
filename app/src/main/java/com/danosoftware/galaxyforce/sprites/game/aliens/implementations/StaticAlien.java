package com.danosoftware.galaxyforce.sprites.game.aliens.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.sprites.game.aliens.AbstractAlien;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplosionBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spinner.SpinningBehaviourFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.types.StaticConfig;

import lombok.Builder;
import lombok.NonNull;

/**
 * Alien that stays in a fixed position once spawned.
 */
public class StaticAlien extends AbstractAlien {

    @Builder
    public StaticAlien(
            @NonNull final ExplosionBehaviourFactory explosionFactory,
            @NonNull final SpawnBehaviourFactory spawnFactory,
            @NonNull final SpinningBehaviourFactory spinningFactory,
            @NonNull final PowerUpBehaviourFactory powerUpFactory,
            @NonNull final FireBehaviourFactory fireFactory,
            @NonNull final HitBehaviourFactory hitFactory,
            @NonNull final StaticConfig alienConfig,
            final PowerUpType powerUpType,
            @NonNull final Integer xStart,
            @NonNull final Integer yStart) {

        super(
                alienConfig.getAlienCharacter(),
                alienConfig.getAlienCharacter().getAnimation(),
                xStart,
                yStart,
                alienConfig.getEnergy(),
                fireFactory.createFireBehaviour(
                        alienConfig.getMissileConfig()),
                powerUpFactory.createPowerUpBehaviour(
                        powerUpType),
                spawnFactory.createSpawnBehaviour(
                        alienConfig.getSpawnConfig()),
                hitFactory.createHitBehaviour(
                        alienConfig.getAlienCharacter().getHitAnimation()),
                explosionFactory.createExplosionBehaviour(
                        alienConfig.getExplosionConfig(),
                        alienConfig.getAlienCharacter()),
                spinningFactory.createSpinningBehaviour(
                        alienConfig.getSpinningConfig()));
    }
}
