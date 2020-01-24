package com.danosoftware.galaxyforce.sprites.game.behaviours.spawn;

import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnOnDemandConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawningAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawningAndExplodingAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawningLimitedAlienConfig;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocatorFactory;

public class SpawnBehaviourFactory {

    private final GameModel model;
    private final AlienFactory alienFactory;
    private final PowerUpAllocatorFactory powerUpAllocatorFactory;

    public SpawnBehaviourFactory(
            final GameModel model,
            final AlienFactory alienFactory,
            final PowerUpAllocatorFactory powerUpAllocatorFactory) {
        this.model = model;
        this.alienFactory = alienFactory;
        this.powerUpAllocatorFactory = powerUpAllocatorFactory;
    }

    /**
     * Create spawn behaviour based on the supplied alien config.
     */
    public SpawnBehaviour createSpawnBehaviour(
            final SpawnConfig spawnConfig) {

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

        if (spawnConfig != null
                && spawnConfig.getType() == SpawnConfig.SpawnType.SPAWN_LIMITED
                && spawnConfig instanceof SpawningLimitedAlienConfig) {

            final SpawningLimitedAlienConfig spawningConfig = ((SpawningLimitedAlienConfig) spawnConfig);

            // behaviour that spawns aliens using a limited spawnedAlienConfig
            return new SpawnRandomDelayLimiter(
                    alienFactory,
                    powerUpAllocatorFactory,
                    model,
                    spawningConfig.getSpawnedAlienConfig(),
                    spawningConfig.getSpwanedPowerUpTypes(),
                    spawningConfig.getMinimumSpawnDelayTime(),
                    spawningConfig.getMaximumAdditionalRandomSpawnDelayTime(),
                    spawningConfig.getMaximumActiveSpawnedAliens(),
                    spawningConfig.getLimitedCharacter());
        }

        if (spawnConfig != null
                && spawnConfig.getType() == SpawnConfig.SpawnType.SPAWN_AND_EXPLODE
                && spawnConfig instanceof SpawningAndExplodingAlienConfig) {

            final SpawningAndExplodingAlienConfig spawningConfig = ((SpawningAndExplodingAlienConfig) spawnConfig);

            // behaviour that spawns a single alien and then explodes
            return new SpawnAndExplode(
                    alienFactory,
                    powerUpAllocatorFactory,
                    model,
                    spawningConfig.getSpawnedAlienConfig(),
                    spawningConfig.getSpwanedPowerUpType(),
                    spawningConfig.getSpawnDelayTime());
        }

        if (spawnConfig != null
                && spawnConfig.getType() == SpawnConfig.SpawnType.SPAWN_ON_DEMAND
                && spawnConfig instanceof SpawnOnDemandConfig) {

            final SpawnOnDemandConfig spawningConfig = ((SpawnOnDemandConfig) spawnConfig);

            // behaviour that spawns on  request
            return new SpawnOnDemand(
                    alienFactory,
                    powerUpAllocatorFactory,
                    model,
                    spawningConfig.getSpawnedAlienConfig(),
                    spawningConfig.getSpwanedPowerUpTypes());
        }

        // behaviour that disables spawning
        return new SpawnDisabled();
    }
}
