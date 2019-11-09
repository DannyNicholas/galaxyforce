package com.danosoftware.galaxyforce.sprites.game.behaviours.explode;

import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.MultiExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.NormalExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.SpawningExplosionConfig;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocatorFactory;

public class ExplosionBehaviourFactory {

    /**
     * Create explosion behaviour based on the supplied  alien config.
     */
    public static ExplodeBehaviour createExplosionBehaviour(
            final GameModel model,
            final ExplosionConfig explosionConfig,
            final Animation explosionAnimation,
            final AlienFactory alienFactory,
            final PowerUpAllocatorFactory powerUpAllocatorFactory,
            final SoundPlayerService sounds,
            final VibrationService vibrator) {

        if (explosionConfig != null
                && explosionConfig.getType() == ExplosionConfig.ExplosionConfigType.NORMAL
                && explosionConfig instanceof NormalExplosionConfig) {

            final NormalExplosionConfig normalExplosionConfig = (NormalExplosionConfig) explosionConfig;

            // normal explosion behaviour
            return new ExplodeSimple(
                    sounds,
                    vibrator,
                    explosionAnimation);
        }

        if (explosionConfig != null
                && explosionConfig.getType() == ExplosionConfig.ExplosionConfigType.MULTI_EXPLOSION
                && explosionConfig instanceof MultiExplosionConfig) {

            final MultiExplosionConfig multiExplosionConfig = (MultiExplosionConfig) explosionConfig;

            // multi-explosion behaviour
            return new ExplodeMultiple(
                    alienFactory,
                    powerUpAllocatorFactory,
                    model,
                    sounds,
                    vibrator,
                    explosionAnimation,
                    multiExplosionConfig.getNumberOfExplosions(),
                    multiExplosionConfig.getMaximumExplosionStartTime(),
                    multiExplosionConfig.getExplosionConfig());
        }

        if (explosionConfig != null
                && explosionConfig.getType() == ExplosionConfig.ExplosionConfigType.NORMAL_AND_SPAWN
                && explosionConfig instanceof SpawningExplosionConfig) {

            // behaviour that spawns aliens on explosion
            final SpawningExplosionConfig spawningExplosionConfig = (SpawningExplosionConfig) explosionConfig;
            final SpawnBehaviour spawner = SpawnBehaviourFactory.createSpawnBehaviour(
                    model,
                    alienFactory,
                    powerUpAllocatorFactory,
                    spawningExplosionConfig.getSpawnConfig());

            // create explode behaviour that wraps a normal explosion
            return new ExplodeAndSpawn(
                    new ExplodeSimple(
                            sounds,
                            vibrator,
                            explosionAnimation),
                    spawner
            );
        }

        // default explosion if none specified
        return new ExplodeSimple(
                sounds,
                vibrator,
                explosionAnimation);
    }
}
