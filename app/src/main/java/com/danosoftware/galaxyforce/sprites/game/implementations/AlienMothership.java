package com.danosoftware.galaxyforce.sprites.game.implementations;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.game.handlers.GameHandler;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviourSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireDisabled;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnRandomDelay;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlienWithPath;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;
import com.danosoftware.galaxyforce.waves.AlienType;

import java.util.List;

public class AlienMothership extends SpriteAlienWithPath
{
    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* minimum delay between spawning aliens in seconds */
    private static final float MIN_SPAWN_DELAY = 0.5f;

    /*
     * maximum addition random time before spawning aliens
     */
    private static final float SPAWN_DELAY_RANDOM = 0.25f;

    /* energy of this sprite */
    private static final int ENERGY = 10;

    /* how much energy will be lost by another sprite when this sprite hits it */
    private static final int HIT_ENERGY = 2;

    // alien animation
    private static final Animation ANIMATION = new Animation(0f, GameSpriteIdentifier.EXPLODE_03);

    /**
     * Create Alien Mothership that has rotated missiles and generates random
     * power-ups.
     * 
     * @param model
     * @param alienPath
     * @param delayStart
     * @param restartImmediately
     */
    public AlienMothership(
            final GameHandler model,
            final PowerUpType powerUpType,
            final List<PowerUpType> spwanPowerUpTypes,
            final List<Point> alienPath,
            final float delayStart,
            final boolean restartImmediately)
    {
        super(
                new FireDisabled(),
                new PowerUpSingle(model, powerUpType),
                new SpawnRandomDelay(
                        model,
                        AlienType.SPAWNED_INSECT,
                        spwanPowerUpTypes,
                        MIN_SPAWN_DELAY,
                        SPAWN_DELAY_RANDOM),
                new ExplodeBehaviourSimple(),
                ANIMATION,
                alienPath,
                delayStart,
                ENERGY,
                HIT_ENERGY,
                restartImmediately);
    }
}
