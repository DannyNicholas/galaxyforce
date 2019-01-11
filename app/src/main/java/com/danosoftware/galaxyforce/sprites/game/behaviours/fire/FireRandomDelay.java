package com.danosoftware.galaxyforce.sprites.game.behaviours.fire;

import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.models.assets.AlienMissilesDto;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienMissileFactory;

public class FireRandomDelay implements FireBehaviour {
    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* variable to store actual delay before alien can fire */
    private double delayUntilNextFire;

    /* variable to store time passed since alien last fired */
    private float timeSinceLastFired;

    /* reference to game model */
    private final GameModel model;

    /* minimum delay between alien firing missiles in seconds */
    private final float minMissileDelay;

    /*
     * maximum random time after minimum delay before missile will fire. actual
     * delay = MIN_DELAY + (DELAY_BUFFER * (0 - 1))
     */
    private final float missleDelayRandom;

    /* missile type */
    private final AlienMissileType missileType;

    /**
     * @param model             - game model
     * @param missileType       - missile type
     * @param minMissileDelay   - minimum delay between missile fires
     * @param missleDelayRandom - additional maximum random time before missile fires
     */
    public FireRandomDelay(GameModel model, AlienMissileType missileType, float minMissileDelay, float missleDelayRandom) {
        this.model = model;
        this.missileType = missileType;
        this.minMissileDelay = minMissileDelay;
        this.missleDelayRandom = missleDelayRandom;

        /* reset time delay until alien can fire */
        delayUntilNextFire = minMissileDelay + (missleDelayRandom * Math.random());

        /*
         * reset time since missile last fired to random value. initialise with
         * random delay to further randomise each alien's firing delay
         */
        timeSinceLastFired = (float) (delayUntilNextFire * Math.random());
    }

    @Override
    public boolean readyToFire(float deltaTime) {
        // increment timer referencing time since alienlast fired
        timeSinceLastFired = timeSinceLastFired + deltaTime;

        // if missile timer has exceeded delay time, is active and meets
        // probability test - fire!!
        return (timeSinceLastFired > delayUntilNextFire);
    }

    @Override
    public void fire(IAlien alien) {
        // reset timer since last fired
        timeSinceLastFired = 0f;

        /* reset time delay until alien can fire */
        delayUntilNextFire = minMissileDelay + (missleDelayRandom * Math.random());

        AlienMissilesDto missiles = AlienMissileFactory.createAlienMissile(model.getBase(), alien, missileType);
        model.fireAlienMissiles(missiles);
    }
}
