package com.danosoftware.galaxyforce.sprites.game.behaviours.spawn;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.assets.SpawnedAliensDto;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocator;
import com.danosoftware.galaxyforce.waves.utilities.PowerUpAllocatorFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Spawn behaviour where an alien spawns and then destroys itself.
 */
public class SpawnAndExplode implements SpawnBehaviour {
    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* reference to game model */
    private final GameModel model;

    /*
     * maximum random time after minimum delay before next alien will spawn.
     * actual delay = MIN_DELAY + (DELAY_BUFFER * (0 - 1))
     */
    private float delayUntilSpawn;

    /* alien config of alien to spawn */
    private final AlienConfig alienConfig;

    // allocate power-ups to spawned aliens
    private final PowerUpAllocator powerUpAllocator;

    private final AlienFactory alienFactory;
    private final PowerUpAllocatorFactory powerUpAllocatorFactory;

    /**
     * @param alienFactory     - factory to create aliens
     * @param powerUpAllocatorFactory - factory to create power-up allocators
     * @param model            - model to receive aliens
     * @param alienConfig      - config of alien to spawn
     * @param powerUpType      - allocated power-up (can be null)
     * @param delayUntilSpawn  - time before spawns
     */
    SpawnAndExplode(
            final AlienFactory alienFactory,
            final PowerUpAllocatorFactory powerUpAllocatorFactory,
            final GameModel model,
            final AlienConfig alienConfig,
            final PowerUpType powerUpType,
            final float delayUntilSpawn) {

        this.delayUntilSpawn = delayUntilSpawn;
        this.alienFactory = alienFactory;
        this.powerUpAllocatorFactory = powerUpAllocatorFactory;
        this.model = model;
        this.alienConfig = alienConfig;

        final List<PowerUpType> powerUps;
        if (powerUpType != null) {
            powerUps = Collections.singletonList(powerUpType);
        } else {
            powerUps = new ArrayList<>();
        }
        this.powerUpAllocator = powerUpAllocatorFactory.createAllocator(
                powerUps,
                1);
    }

    @Override
    public boolean readyToSpawn(float deltaTime) {

        // countdown until alien spawns
        delayUntilSpawn -= deltaTime;

        // if timer has reached zero, then we're ready to spawn
        return (delayUntilSpawn <= 0);
    }

    @Override
    public void spawn(IAlien alien) {
        // create and send new alien bean
        SpawnedAliensDto aliens = alienFactory.createSpawnedAlien(
                alienConfig,
                powerUpAllocatorFactory,
                powerUpAllocator.allocate(),
                alien.x(),
                alien.y());

        model.spawnAliens(aliens);

        // destroy our alien
        alien.explode();
    }
}
