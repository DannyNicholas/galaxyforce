package com.danosoftware.galaxyforce.sprites.game.implementations;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.enumerations.Direction;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.game.handlers.GameHandler;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviourSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireDisabled;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnDisabled;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlien;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteState;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * Implementation of asteroid that has fixed angular speed and velocity.
 * 
 * @author Danny
 *
 */
public class AlienAsteroidSimple extends SpriteAlien
{
    /*
     * ******************************************************
     * PRIVATE STATIC VARIABLES
     * ******************************************************
     */

    /* energy of this sprite */
    private static final int ENERGY = 5;

    /* how much energy will be lost by another sprite when this sprite hits it */
    private static final int HIT_ENERGY = 2;

    // alien animation
    private static final Animation ANIMATION = new Animation(0f, GameSpriteIdentifier.ASTEROID);

    /*
     * ******************************************************
     * PRIVATE INSTANCE VARIABLES
     * ******************************************************
     */

    /* current for sprite rotation */
    private float angle;

    /* speed of sprite rotation */
    private final int anglularSpeed;

    /* speed of asteroid */
    private final int speed;

    /* how many seconds to delay before alien starts to follow path */
    private float timeDelayStart;

    /* direction of asteroid */
    private final Direction direction;

    /* restart asteroid as soon as it leaves screen? */
    private final boolean restartImmediately;

    /* variable to store original position for asteroid */
    private final int originalYPosition;

    /* variable to store how far alien has moved since spawned */
    private float distanceYMoved = 0f;

    /**
     * Create Alien Asteroid.
     */
    public AlienAsteroidSimple(
            final PowerUpType powerUpType,
            final int xStart,
            final int yStart,
            final float timeDelayStart,
            final boolean restartImmediately,
            final Direction direction,
            final GameHandler model)
    {
        // default is that asteroids are initially invisible
        super(
                new FireDisabled(),
                new PowerUpSingle(model, powerUpType),
                new SpawnDisabled(),
                new ExplodeBehaviourSimple(),
                ANIMATION,
                xStart,
                yStart,
                ENERGY,
                HIT_ENERGY,
                false);

        setState(SpriteState.INACTIVE);

        // set positional and movement behaviour
        this.timeDelayStart = timeDelayStart;
        this.originalYPosition = yStart;
        this.distanceYMoved = 0f;
        this.direction = direction;
        this.restartImmediately = restartImmediately;

        // set random starting rotation angle
        this.angle = (float) (Math.random() * 360);

        // set random rotation speed between 50 and 400
        this.anglularSpeed = 50 + (int) (Math.random() * 350);

        // set asteroid speed between 75 and 250 (related to angular speed)
        this.speed = 100;
    }

    @Override
    public void move(float deltaTime)
    {
        /* if active then asteroid can move */
        if (isActive())
        {

            // move until off the screen and then either destroy it or reset it
            switch (direction)
            {
            case UP:

                // move
                distanceYMoved -= speed * deltaTime;
                setY(originalYPosition - (int) distanceYMoved);

                // if asteroid is now off screen then decide whether to destory
                // it or reset
                if (getY() > GameConstants.GAME_HEIGHT - (getHeight() / 2))
                {
                    if (restartImmediately)
                    {
                        setY(originalYPosition);
                        distanceYMoved = 0f;
                    }
                    else
                    {
                        setState(SpriteState.DESTROYED);
                    }
                }
                break;

            case DOWN:

                // move
                distanceYMoved += speed * deltaTime;
                setY(originalYPosition - (int) distanceYMoved);

                // if asteroid is now off screen then decide whether to destory
                // it or reset
                if (getY() < 0 - (getHeight() / 2))
                {
                    if (restartImmediately)
                    {
                        setY(originalYPosition);
                        distanceYMoved = 0f;
                    }
                    else
                    {
                        setState(SpriteState.DESTROYED);
                    }
                }
                break;
            }

            // rotate asteroid
            angle = (angle + (deltaTime * anglularSpeed)) % 360;
            setRotation((int) (angle));

            // move sprite bounds
            updateBounds();

        }
        else if (isInactive())
        {
            /* if delayStart still > 0 then count down delay */
            if (timeDelayStart > 0)
            {
                timeDelayStart -= deltaTime;
            }
            /* otherwise activate alien. can only happen once! */
            else
            {
                setState(SpriteState.ACTIVE);
                setVisible(true);
            }
        }

        /* use superclass for any explosions */
        super.move(deltaTime);
    }
}
