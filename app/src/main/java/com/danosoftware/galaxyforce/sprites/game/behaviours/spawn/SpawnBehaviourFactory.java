package com.danosoftware.galaxyforce.sprites.game.behaviours.spawn;

import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.SpawningAlienConfig;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocatorFactory;

public class SpawnBehaviourFactory {

    /**
     * Create spawn behaviour based on the supplied alien config.
     */
    public static SpawnBehaviour createSpawnBehaviour(
            final GameModel model,
            final AlienFactory alienFactory,
            final PowerUpAllocatorFactory powerUpAllocatorFactory,
            final AlienConfig alienConfig) {

        final SpawnConfig spawnConfig = alienConfig.getSpawnConfig();
        if (spawnConfig != null
                && spawnConfig.getType() == SpawnConfig.SpawnType.SPAWN
                && spawnConfig instanceof SpawningAlienConfig) {

            final SpawningAlienConfig spawningConfig = ((SpawningAlienConfig) spawnConfig);

            // behaviour that spawns aliens using spawnedAlienConfig
            return new SpawnRandomDelay(
                    alienFactory,
                    powerUpAllocatorFactory,
                    model,
                    spawningConfig.getSpawnedAlienConfig(),
                    spawningConfig.getSpwanedPowerUpTypes(),
                    spawningConfig.getMinimumSpawnDelayTime(),
                    spawningConfig.getMaximumAdditionalRandomSpawnDelayTime());
        }

        // behaviour that disables spawning
        return new SpawnDisabled();
    }
}
