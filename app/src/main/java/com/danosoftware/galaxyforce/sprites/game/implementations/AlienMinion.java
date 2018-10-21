package com.danosoftware.galaxyforce.sprites.game.implementations;

import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.paths.Point;
import com.danosoftware.galaxyforce.game.handlers.GameHandler;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviourSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireRandomDelay;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnDisabled;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlienWithPath;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

import java.util.List;

public class AlienMinion extends SpriteAlienWithPath
{
    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* minimum delay between alien firing missiles in seconds */
    private static final float MIN_MISSILE_DELAY = 2.5f;

    /* maximum addition random time before firing */
    private static final float MISSILE_DELAY_RANDOM = 2f;

    /* energy of this sprite */
    private static final int ENERGY = 1;

    /* how much energy will be lost by another sprite when this sprite hits it */
    private static final int HIT_ENERGY = 4;

    // alien animation
    private static final Animation ANIMATION = new Animation(0.5f, new GameSpriteIdentifier[] {
            GameSpriteIdentifier.ALIEN_MINION_NORMAL,
            GameSpriteIdentifier.ALIEN_MINION_FUZZ1,
            GameSpriteIdentifier.ALIEN_MINION_FUZZ2});

    /*
     * ******************************************************
     * CONSTRUCTOR
     * 
     * ******************************************************
     */

    /**
     * Create Alien Minion that has simple directional missiles and only
     * generates guided missile power-ups.
     * 
     * @param model
     * @param alienPath
     * @param delayStart
     * @param restartImmediately
     */
    public AlienMinion(
            final GameHandler model,
            final PowerUpType powerUpType,
            final List<Point> alienPath,
            final float delayStart,
            final boolean restartImmediately)
    {
        super(
                new FireRandomDelay(model, AlienMissileType.SIMPLE, MIN_MISSILE_DELAY, MISSILE_DELAY_RANDOM),
                new PowerUpSingle(model, powerUpType),
                new SpawnDisabled(),
                new ExplodeBehaviourSimple(),
                ANIMATION,
                alienPath,
                delayStart,
                ENERGY,
                HIT_ENERGY,
                restartImmediately);
    }
}
