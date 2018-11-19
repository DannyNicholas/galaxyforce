package com.danosoftware.galaxyforce.sprites.refactor;

import com.danosoftware.galaxyforce.sprites.game.bases.IBase;

import static com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier.BASE;
import static com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier.BASE_LEFT;
import static com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier.BASE_RIGHT;

/**
 * Helper class that moves the base around the screen and changes sprites depending
 * on speed of turning.
 * <p>
 * For instance a sharp right turn, changes the base sprite to a banked right turn.
 * <p>
 * Instances of this class reference a specific base so a new instance must be created
 * every time a new base is created.
 */
public class MoveBaseHelper {

    // distance base can move each second in pixels
    private static final int BASE_MOVE_PIXELS = 10 * 60;

    // weighting that must be exceeded to trigger base left or right turn
    private static float WEIGHTING_TURN = 0.5f;

    // increased for tilt controller as weighting never goes close to zero in tilt mode
    private static float WEIGHTING_STEADY = 0.1f;

    // number of seconds for base to be steady for before resetting left or right turn
    private static float STEADY_DELAY = 0.1f;

    // base being animated
    private final IBase base;

    // holds time base has been steady for (i.e. not turning left or right)
    private float baseTurnSteadyTime;

    // is base currently turning left or right?
    private boolean baseTurning;

    // width/height of actual usable screen */
    private final int width, height;


    public MoveBaseHelper(
            final IBase base,
            final int width,
            final int height) {

        this.base = base;
        this.width = width;
        this.height = height;
        this.baseTurnSteadyTime = 0f;
        this.baseTurning = false;
    }

    /**
     * Moves base by the supplied weighting
     */
    public void moveBase(float weightingX, float weightingY, float deltaTime) {

        /*
         * can cause jittery movement if game is running very slowly as base
         * can overshoot target but ensures that base movement doesn't slow
         * down when game slows down.
         */
        float maxDistanceMoved = BASE_MOVE_PIXELS * deltaTime;
        int x = base.x() + (int) (maxDistanceMoved * weightingX);
        int y = base.y() + (int) (maxDistanceMoved * weightingY);

        // don't allow base to go off screen left
        if (x - base.halfWidth() < 0) {
            x = base.halfWidth();
        }

        // don't allow base to go off screen right
        if ((x + base.halfWidth()) > width) {
            x = (width - base.halfWidth());
        }

        // don't allow base to go off screen bottom
        if ((y - base.halfHeight()) < 0) {
            y = base.halfHeight();
        }

        // don't allow base to go off screen top
        if ((y + base.halfHeight()) > height) {
            y = (height - base.halfHeight());
        }

        // move base to new position
        base.move(x, y);

        /*
         * if base is going right then use base right turn sprite. reset
         * time since base was steady so that base doesn't immediately
         * un-turn.
         */
        if (weightingX > WEIGHTING_TURN) {
            base.changeType(BASE_RIGHT);
            baseTurnSteadyTime = 0f;
            baseTurning = true;
        }
        /*
         * if base is going left then use base left turn sprite. reset time
         * since base was steady so that base doesn't immediately un-turn.
         */
        else if (weightingX < -WEIGHTING_TURN) {
            base.changeType(BASE_LEFT);
            baseTurnSteadyTime = 0f;
            baseTurning = true;
        }
        /*
         * if base not moving increment time base has been steady for. if
         * base has been steady for more that set time then revert to normal
         * sprite.
         */
        else if (baseTurning && weightingX > -WEIGHTING_STEADY && weightingX < WEIGHTING_STEADY) {
            baseTurnSteadyTime = baseTurnSteadyTime + deltaTime;
            if (baseTurnSteadyTime > STEADY_DELAY) {
                base.changeType(BASE);
                baseTurnSteadyTime = 0f;
                baseTurning = false;
            }
        }
    }
}