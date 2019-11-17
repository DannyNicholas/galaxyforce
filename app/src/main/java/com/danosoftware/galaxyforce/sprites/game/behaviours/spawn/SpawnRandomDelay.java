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

public class SpawnRandomDelay implements SpawnBehaviour {
    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* reference to game model */
    private final GameModel model;

    /* minimum delay between alien spqwning in seconds */
    private final float minSpawnDelay;

    /*
     * maximum random time after minimum delay before next alien will spawn.
     * actual delay = MIN_DELAY + (DELAY_BUFFER * (0 - 1))
     */
    private final float spawnDelayRandom;

    /* alien config of alien to spawn */
    private final AlienConfig alienConfig;

    /* variable to store actual delay before alien can spawn */
    private double delayUntilNextSpawn;

    /* variable to store time passed since last spawn */
    private float timeSinceLastSpawn;

    // allocate power-ups to spawned aliens
    private final PowerUpAllocator powerUpAllocator;

    // since we don't know how many aliens will be spawned, use
    // a multiplier to guess the number of aliens for power-up allocation
    // A value of 10 indicates 1 power-up for every 10 aliens spawned
    private final static int POWER_UP_MULTIPLIER = 10;

    private final AlienFactory alienFactory;

    /**
     * @param alienFactory     - factory to create aliens
     * @param powerUpAllocatorFactory - factory to create power-up allocators
     * @param model            - model to receive aliens
     * @param alienConfig      - config of alien to spawn
     * @param minSpawnDelay    - minimum delay between spawns
     * @param spawnDelayRandom - additional maximum random time before spawns
     */
    SpawnRandomDelay(
            final AlienFactory alienFactory,
            final PowerUpAllocatorFactory powerUpAllocatorFactory,
            final GameModel model,
            final AlienConfig alienConfig,
            final List<PowerUpType> powerUpTypes,
            final float minSpawnDelay,
            final float spawnDelayRandom) {
        this.alienFactory = alienFactory;
        this.model = model;
        this.alienConfig = alienConfig;
        this.minSpawnDelay = minSpawnDelay;
        this.spawnDelayRandom = spawnDelayRandom;

        /* reset time delay until alien can spawn */
        delayUntilNextSpawn = minSpawnDelay + (spawnDelayRandom * Math.random());

        /*
         * reset time since missile last fired to random value. initialise with
         * random delay to further randomise each alien's firing delay
         */
        timeSinceLastSpawn = (float) (delayUntilNextSpawn * Math.random());

        this.powerUpAllocator = powerUpAllocatorFactory.createAllocator(
                powerUpTypes,
                powerUpTypes.size() * POWER_UP_MULTIPLIER);
    }

    @Override
    public boolean readyToSpawn(float deltaTime) {
        // increment timer referencing time since alien last spawned
        timeSinceLastSpawn = timeSinceLastSpawn + deltaTime;

        // if timer has exceeded delay time and is active then ready to spawn
        return (timeSinceLastSpawn > delayUntilNextSpawn);
    }

    @Override
    public void spawn(IAlien alien) {
        /* reset time since base last spawned */
        timeSinceLastSpawn = 0f;

        /* reset time delay until alien can spawn */
        delayUntilNextSpawn = minSpawnDelay + (spawnDelayRandom * Math.random());

        // create and send new alien bean
        SpawnedAliensDto aliens = alienFactory.createSpawnedAlien(
                alienConfig,
                powerUpAllocator.allocate(),
                alien.x(),
                alien.y());

        model.spawnAliens(aliens);
    }
}
