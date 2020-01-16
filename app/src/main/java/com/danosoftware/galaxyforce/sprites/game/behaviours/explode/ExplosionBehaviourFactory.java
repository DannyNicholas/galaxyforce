package com.danosoftware.galaxyforce.sprites.game.behaviours.explode;

import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnBehaviourFactory;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.MultiExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.NormalExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.SpawningExplosionConfig;

public class ExplosionBehaviourFactory {

    private final GameModel model;
    private final AlienFactory alienFactory;
    private final SoundPlayerService sounds;
    private final VibrationService vibrator;
    private final SpawnBehaviourFactory spawnFactory;

    public ExplosionBehaviourFactory(
            final GameModel model,
            final AlienFactory alienFactory,
            final SpawnBehaviourFactory spawnFactory,
            final SoundPlayerService sounds,
            final VibrationService vibrator) {
        this.model = model;
        this.alienFactory = alienFactory;
        this.spawnFactory = spawnFactory;
        this.sounds = sounds;
        this.vibrator = vibrator;
    }

    /**
     * Create explosion behaviour based on the supplied  alien config.
     */
    public ExplodeBehaviour createExplosionBehaviour(
            final ExplosionConfig explosionConfig,
            final AlienCharacter character) {

        if (explosionConfig != null
                && explosionConfig.getType() == ExplosionConfig.ExplosionConfigType.NORMAL
                && explosionConfig instanceof NormalExplosionConfig) {

            final NormalExplosionConfig normalExplosionConfig = (NormalExplosionConfig) explosionConfig;

            // normal explosion behaviour
            return new ExplodeSimple(
                    sounds,
                    vibrator,
                    character.getExplosionAnimation());
        }

        if (explosionConfig != null
                && explosionConfig.getType() == ExplosionConfig.ExplosionConfigType.MULTI_EXPLOSION
                && explosionConfig instanceof MultiExplosionConfig) {

            final MultiExplosionConfig multiExplosionConfig = (MultiExplosionConfig) explosionConfig;

            // multi-explosion behaviour
            return new ExplodeMultiple(
                    alienFactory,
                    model,
                    sounds,
                    vibrator,
                    character,
                    multiExplosionConfig.getNumberOfExplosions(),
                    multiExplosionConfig.getMaximumExplosionStartTime(),
                    multiExplosionConfig.getExplosionConfig());
        }

        if (explosionConfig != null
                && explosionConfig.getType() == ExplosionConfig.ExplosionConfigType.NORMAL_AND_SPAWN
                && explosionConfig instanceof SpawningExplosionConfig) {

            // behaviour that spawns aliens on explosion
            final SpawningExplosionConfig spawningExplosionConfig = (SpawningExplosionConfig) explosionConfig;
            final SpawnBehaviour spawner = spawnFactory.createSpawnBehaviour(
                    spawningExplosionConfig.getSpawnConfig());

            // create explode behaviour that wraps a normal explosion
            return new ExplodeAndSpawn(
                    new ExplodeSimple(
                            sounds,
                            vibrator,
                            character.getExplosionAnimation()),
                    spawner
            );
        }

        // default explosion if none specified
        return new ExplodeSimple(
                sounds,
                vibrator,
                character.getExplosionAnimation());
    }
}
