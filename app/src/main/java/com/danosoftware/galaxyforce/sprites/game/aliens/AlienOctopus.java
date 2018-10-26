package com.danosoftware.galaxyforce.sprites.game.aliens;

import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.game.handlers.GameHandler;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireRandomDelay;
import com.danosoftware.galaxyforce.sprites.game.behaviours.hit.HitDisabled;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnDisabled;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

import java.util.List;

public class AlienOctopus extends AbstractAlienWithPath
{

    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* minimum delay between alien firing missiles in seconds */
    private static final float MIN_MISSILE_DELAY = 4.5f;

    /* maximum addition random time before firing */
    private static final float MISSILE_DELAY_RANDOM = 2f;

    /* energy of this sprite */
    private static final int ENERGY = 1;

    // alien animation
    private static final Animation ANIMATION = new Animation(
            0.5f,
            GameSpriteIdentifier.ALIEN_OCTOPUS_LEFT,
            GameSpriteIdentifier.ALIEN_OCTOPUS_RIGHT);

    /**
     * Create Alien Octopus that has rotated missiles and generates random
     * power-ups.
     */
    public AlienOctopus(
            final GameHandler model,
            final PowerUpType powerUp,
            final List<Point> alienPath,
            final float delayStart,
            final boolean restartImmediately)
    {
        super(
                ANIMATION,
                new FireRandomDelay(
                        model,
                        AlienMissileType.ROTATED,
                        MIN_MISSILE_DELAY,
                        MISSILE_DELAY_RANDOM),
                new PowerUpSingle(model, powerUp),
                new SpawnDisabled(),
                new HitDisabled(),
                new ExplodeSimple(),
                alienPath,
                delayStart,
                ENERGY,
                restartImmediately);
    }
}
