package com.danosoftware.galaxyforce.utilities;

import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.game.bases.enums.BaseLean;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.SCREEN_MID_X;
import static java.lang.Math.abs;

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

    // base's start y position when ready
    private static final int BASE_START_Y = 192;

    private static final double HALF_PI = (float) Math.PI / 2f;

    /*
     * large movement increased as base movement can be very jittery when the
     * game is running slowly as base appears to be overshoot target so next
     * move goes back in other opposite direction as so on. the slower the game
     * is, the more the base will move and so the greater chance of overshooting
     * we have.
     *
     * game optimised to run at 1/60 fps so increase BASE_MOVE_RADIUS_LARGE by
     * ratio of actual speed to ideal speed.
     */
    private static final float BASE_MOVE_RADIUS_LARGE = 20;
    private static final float BASE_MOVE_RADIUS_SMALL = 5;
    private static final float BASE_MOVE_RADIUS_DELTA = BASE_MOVE_RADIUS_LARGE - BASE_MOVE_RADIUS_SMALL;


    // distance base can move each second in pixels
    private static final int BASE_MOVE_PIXELS = 10 * 60;

    // weighting that must be exceeded to trigger base left or right turn
    private static final float WEIGHTING_TURN = 0.5f;

    // increased for tilt controller as weighting never goes close to zero in tilt mode
    private static final float WEIGHTING_STEADY = 0.1f;

    // number of seconds for base to be steady for before resetting left or right turn
    private static final float STEADY_DELAY = 0.1f;

    // base being animated
    private final IBasePrimary base;

    // holds time base has been steady for (i.e. not turning left or right)
    private float baseTurnSteadyTime;

    // is base currently turning left or right?
    private boolean baseTurning;

    // target position of where we want base to move to
    private int targetX, targetY;

    // current weighting of base movement
    private float weightingX, weightingY;

    // is base currently launching (i.e. moving to start position)
    private boolean isLaunching;


    public MoveBaseHelper(
            final IBasePrimary base) {

        this.base = base;
        this.baseTurnSteadyTime = 0f;
        this.baseTurning = false;
        this.weightingX = 0f;
        this.weightingY = 0f;
        this.targetX = SCREEN_MID_X;
        this.targetY = BASE_START_Y;
        this.isLaunching = true;
    }

    public void updateTarget(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    /**
     * Moves base
     */
    public void moveBase(float deltaTime) {

        if (isLaunching) {
            launchBase(deltaTime);
        } else {
            moveActiveBase(deltaTime);
        }
    }

    /**
     * Moves base by the supplied weighting
     */
    private void moveActiveBase(float deltaTime) {

        updateWeighting(
                targetX - base.x(),
                targetY - base.y());

        /*
         * can cause jittery movement if game is running very slowly as base
         * can overshoot target but ensures that base movement doesn't slow
         * down when game slows down.
         */
        float maxDistanceMoved = BASE_MOVE_PIXELS * deltaTime;
        int x = base.x() + (int) (maxDistanceMoved * weightingX);
        int y = base.y() + (int) (maxDistanceMoved * weightingY);

        // don't allow weightings to over-shoot targets
        if (weightingX > 0 && x > targetX) {
            x = targetX;
        }
        if (weightingX < 0 && x < targetX) {
            x = targetX;
        }
        if (weightingY > 0 && y > targetY) {
            y = targetY;
        }
        if (weightingY < 0 && y < targetY) {
            y = targetY;
        }

        // don't allow base to go off screen top
        if ((y + base.halfHeight()) > GAME_HEIGHT) {
            y = (GAME_HEIGHT - base.halfHeight());
        }

        // move base to new position
        base.move(x, y);

        /*
         * if base is going right then use base right turn sprite. reset
         * time since base was steady so that base doesn't immediately
         * un-turn.
         */
        if (weightingX > WEIGHTING_TURN) {
            base.setLean(BaseLean.RIGHT);
            baseTurnSteadyTime = 0f;
            baseTurning = true;
        }
        /*
         * if base is going left then lean left. reset time
         * since base was steady so that base doesn't immediately un-turn.
         */
        else if (weightingX < -WEIGHTING_TURN) {
            base.setLean(BaseLean.LEFT);
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
                base.setLean(BaseLean.NONE);
                baseTurnSteadyTime = 0f;
                baseTurning = false;
            }
        }
    }


    /**
     * Moves base to launch position
     */
    private void launchBase(float deltaTime) {

        updateWeighting(
                SCREEN_MID_X - base.x(),
                BASE_START_Y - base.y());

        /*
         * can cause jittery movement if game is running very slowly as base
         * can overshoot target but ensures that base movement doesn't slow
         * down when game slows down.
         */
        float maxDistanceMoved = BASE_MOVE_PIXELS * deltaTime;
        int x = base.x() + (int) (maxDistanceMoved * weightingX);
        int y = base.y() + (int) (maxDistanceMoved * weightingY);

        // has base reached launch target
        if (abs(base.y() - BASE_START_Y) <= BASE_MOVE_RADIUS_LARGE
                || base.y() >= BASE_START_Y) {
            base.move(SCREEN_MID_X, BASE_START_Y);
            isLaunching = false;
        }
        else {
            // move base to new position
            base.move(x, y);
        }
    }



    private void updateWeighting(float deltaX, float deltaY) {

        /*
        to avoid jitter when moving - check how far base is away from target.
         */
        float baseDistance = (float) Math.sqrt(
                (deltaX * deltaX) + (deltaY * deltaY));

        /*
         if distance large then calculate weighting purely on angle between
         base and current touch point
          */
        if (baseDistance > BASE_MOVE_RADIUS_LARGE) {
            /*
             calculate angle from circle centre to touch point
             atan2 doesn't have limitations of atan but is PI/2 greater than
             expected so manually correct this.
              */
            float theta = (float) (HALF_PI - Math.atan2(deltaY, deltaX));

            /*
            calculate weighting (used to calculate how far to move base's x
            and y). Ranges from 0 to 1.
             */
            weightingX = (float) Math.sin(theta);
            weightingY = (float) Math.cos(theta);
        }

        /*
         if distance small then calculate weighting based on angle but
         multiply by a calculated weight. Much smoother movement for small
         moves than previous calculation.
          */
        else if (baseDistance > BASE_MOVE_RADIUS_SMALL) {
            /*
             calculate angle from circle centre to touch point
             atan2 doesn't have limitations of atan but is PI/2 greater than
             expected so manually correct this.
              */
            float theta = (float) (HALF_PI - Math.atan2(deltaY, deltaX));

            /*
             calculate weighting using distance and threshold.
             result ranges from 0-1.
              */
            float distanceWeight = (baseDistance - BASE_MOVE_RADIUS_SMALL)
                    / (BASE_MOVE_RADIUS_DELTA);
            weightingX = (float) (distanceWeight * Math.sin(theta));
            weightingY = (float) (distanceWeight * Math.cos(theta));
        }
        // if base is very close to touch point then reset weightings
        else {
            weightingX = 0f;
            weightingY = 0f;
        }
    }
}