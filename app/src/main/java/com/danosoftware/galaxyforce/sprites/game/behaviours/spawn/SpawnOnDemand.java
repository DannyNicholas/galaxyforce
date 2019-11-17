package com.danosoftware.galaxyforce.sprites.game.behaviours.spawn;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.assets.SpawnedAliensDto;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocator;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocatorFactory;

import java.util.List;

/**
 * Spawn behaviour where an alien only spawns on demands.
 * This spawner does make its ownn decision on when to spawn.
 */
public class SpawnOnDemand implements SpawnBehaviour {

    /* reference to game model */
    private final GameModel model;

    /* alien config of alien to spawn */
    private final AlienConfig alienConfig;

    // allocate power-ups to spawned aliens
    private final PowerUpAllocator powerUpAllocator;

    private final AlienFactory alienFactory;

    /**
     * @param alienFactory     - factory to create aliens
     * @param powerUpAllocatorFactory - factory to create power-up allocators
     * @param model            - model to receive aliens
     * @param alienConfig      - config of alien to spawn
     * @param powerUpTypes     - allocated power-ups assigned to spawned aliens
     */
    SpawnOnDemand(
            final AlienFactory alienFactory,
            final PowerUpAllocatorFactory powerUpAllocatorFactory,
            final GameModel model,
            final AlienConfig alienConfig,
            final List<PowerUpType> powerUpTypes) {
        this.alienFactory = alienFactory;
        this.model = model;
        this.alienConfig = alienConfig;

        this.powerUpAllocator = powerUpAllocatorFactory.createAllocator(
                powerUpTypes,
                powerUpTypes.size());
    }

    @Override
    public boolean readyToSpawn(float deltaTime) {
        // this implementation has no knowledge on when to spawn
        return false;
    }

    @Override
    public void spawn(IAlien alien) {
        // create and send new alien bean
        SpawnedAliensDto aliens = alienFactory.createSpawnedAlien(
                alienConfig,
                powerUpAllocator.allocate(),
                alien.x(),
                alien.y());

        model.spawnAliens(aliens);
    }
}
