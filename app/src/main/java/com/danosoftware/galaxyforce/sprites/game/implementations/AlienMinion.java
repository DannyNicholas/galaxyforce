package com.danosoftware.galaxyforce.sprites.game.implementations;

import java.util.List;

import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.Point;
import com.danosoftware.galaxyforce.game.handlers.GameHandler;
import com.danosoftware.galaxyforce.sprites.game.behaviours.ExplodeBehaviourSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.FireRandomDelay;
import com.danosoftware.galaxyforce.sprites.game.behaviours.PowerUpRandom;
import com.danosoftware.galaxyforce.sprites.game.behaviours.SpawnDisabled;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlienWithPath;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

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

    /* chance that this alien will generate a power-up when destroyed */
    private static final double CHANCE_OF_POWER_UP = 0.6D;

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
    public AlienMinion(GameHandler model, List<Point> alienPath, float delayStart, boolean restartImmediately)
    {
        super(new FireRandomDelay(model, AlienMissileType.SIMPLE, MIN_MISSILE_DELAY, MISSILE_DELAY_RANDOM),

        new PowerUpRandom(model, CHANCE_OF_POWER_UP, PowerUpType.MISSILE_GUIDED),

        new SpawnDisabled(),

        new ExplodeBehaviourSimple(),

        ANIMATION, alienPath, delayStart, ENERGY, HIT_ENERGY, restartImmediately);
    }
}
