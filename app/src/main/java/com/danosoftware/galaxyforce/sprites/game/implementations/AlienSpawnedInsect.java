package com.danosoftware.galaxyforce.sprites.game.implementations;

import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.game.handlers.GameHandler;
import com.danosoftware.galaxyforce.sprites.game.behaviours.ExplodeBehaviourSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.FireRandomDelay;
import com.danosoftware.galaxyforce.sprites.game.behaviours.PowerUpBehaviour;
import com.danosoftware.galaxyforce.sprites.game.behaviours.PowerUpSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.SpawnDisabled;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlien;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteState;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

public class AlienSpawnedInsect extends SpriteAlien
{
    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* minimum delay between alien firing missiles in seconds */
    private static final float MIN_MISSILE_DELAY = 1f;

    /* maximum addition random time before firing */
    private static final float MISSILE_DELAY_RANDOM = 0.5f;

    /* energy of this sprite */
    private static final int ENERGY = 1;

    /* how much energy will be lost by another sprite when this sprite hits it */
    private static final int HIT_ENERGY = 2;

    // alien animation
    private static final Animation ANIMATION = new Animation(0.5f, GameSpriteIdentifier.INSECT_WINGS_UP,
            GameSpriteIdentifier.INSECT_WINGS_DOWN);


    /* distance alien can move in pixels each second */
    public static final int ALIEN_MOVE_PIXELS = 2 * 60;

    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* variable to store original position for alien when spawned */
    private final int originalYPosition;

    /* variable to store how far alien has moved since spawned */
    private float distanceYMoved = 0f;

    /**
     * Create spawned Alien Insect.
     */
    public AlienSpawnedInsect(
            final PowerUpType powerUpType,
            final int xStart,
            final int yStart,
            final GameHandler model)
    {
        super(
                new FireRandomDelay(model, AlienMissileType.SIMPLE, MIN_MISSILE_DELAY, MISSILE_DELAY_RANDOM),
                new PowerUpSingle(model, powerUpType),
                new SpawnDisabled(),
                new ExplodeBehaviourSimple(),
                ANIMATION,
                xStart,
                yStart,
                ENERGY,
                HIT_ENERGY,
                true);

        /* distance moved since spawned */
        this.distanceYMoved = 0f;

        /* original y position */
        this.originalYPosition = yStart;
    }

    @Override
    public void move(float deltaTime)
    {
        /* use superclass for movements */
        super.move(deltaTime);

        /* if active then alien can move */
        if (isActive())
        {
            distanceYMoved += ALIEN_MOVE_PIXELS * deltaTime;

            setY(originalYPosition - (int) distanceYMoved);
            // move sprite bounds
            updateBounds();
        }

        /*
         * if alien off screen then destroy alien no need to handle explosions
         */
        if (getY() < 0 - (getHeight() / 2))
        {
            setState(SpriteState.DESTROYED);
        }
    }
}
