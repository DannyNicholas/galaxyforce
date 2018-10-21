package com.danosoftware.galaxyforce.sprites.game.implementations;

import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.game.handlers.GameHandler;
import com.danosoftware.galaxyforce.sprites.game.behaviours.explode.ExplodeBehaviourSimple;
import com.danosoftware.galaxyforce.sprites.game.behaviours.fire.FireRandomDelay;
import com.danosoftware.galaxyforce.sprites.game.behaviours.powerup.PowerUpSingle;
import com.danosoftware.galaxyforce.sprites.game.behaviours.spawn.SpawnDisabled;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteAlien;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SpriteState;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

public class AlienDragonBody extends SpriteAlien
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

    /* distance alien can move each cycle in pixels each second */
    public static final int ALIEN_MOVE_PIXELS = 5 * 60;

    /* minimum distance between dragon alien bodies */
    public static final int MIN_DISTANCE_SQUARED = 25 * 25;

    /* energy of this sprite */
    private static final int ENERGY = 1;

    /* how much energy will be lost by another sprite when this sprite hits it */
    private static final int HIT_ENERGY = 2;

    /* chance that this alien will generate a power-up when destroyed */
    private static final double CHANCE_OF_POWER_UP = 0.2D;

    // alien animation
    private static final Animation ANIMATION = new Animation(0f, GameSpriteIdentifier.DRAGON_BODY);

    boolean started;

    /**
     * Create Alien Dragon's Body that has rotated missiles and generates random
     * power-ups.
     */
    public AlienDragonBody(
            final PowerUpType powerUpType,
            final int xStart,
            final int yStart,
            final GameHandler model) {

        super(
                new FireRandomDelay(model, AlienMissileType.ROTATED, MIN_MISSILE_DELAY, MISSILE_DELAY_RANDOM),
                new PowerUpSingle(model, powerUpType),
                new SpawnDisabled(),
                new ExplodeBehaviourSimple(),
                ANIMATION,
                xStart,
                yStart,
                ENERGY,
                HIT_ENERGY,
                false);

        this.started = false;
    }

    /**
     * Update position of body based on alien this is following.
     * The body will attempt to follow the body in front but will throttle
     * it's speed if it gets too close.
     *
     * @param alienFollowed - alien being followed
     * @param deltaTime
     */
    public void bodyUpdate(SpriteAlien alienFollowed, float deltaTime) {

        if (!started) {
            setState(SpriteState.ACTIVE);
            setVisible(true);
            this.started = true;
        }

        // calculate angle from this dragon body to the one we are following
        float newAngle = (float) Math.atan2(alienFollowed.getY() - getY(), alienFollowed.getX() - getX());

        // calculate the deltas to be applied each move
        int xDelta = (int) ((ALIEN_MOVE_PIXELS) * (float) Math.cos(newAngle));
        int yDelta = (int) ((ALIEN_MOVE_PIXELS) * (float) Math.sin(newAngle));

        // calculate new position
        int newX = getX() + (int) (xDelta * deltaTime);
        int newY = getY() + (int) (yDelta * deltaTime);

        // calculate squared distance from alien we are following
        int distX = (alienFollowed.getX() - newX);
        int distY = (alienFollowed.getY() - newY);
        int distSquared = (distX * distX) + (distY * distY);

        // if we are too close we need to throttle our speed
        if (distSquared > MIN_DISTANCE_SQUARED) {
            setX(newX);
            setY(newY);

            // move sprite bounds
            updateBounds();
        }
        else {
            float throttleRatio = (float) distSquared / MIN_DISTANCE_SQUARED;

            // calculate new position based on reduced speed
            int reducedXDelta = (int) (xDelta * throttleRatio);
            int reducedYDelta = (int) (yDelta * throttleRatio);
            int recalculatedX = getX() + (int) (reducedXDelta * deltaTime);
            int recalculatedY = getY() + (int) (reducedYDelta * deltaTime);

            // move alien
            setX(recalculatedX);
            setY(recalculatedY);

            // move sprite bounds
            updateBounds();
        }
    }
}
