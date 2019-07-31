package com.danosoftware.galaxyforce.sprites.game.missiles.aliens;

import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.view.Animation;

import static com.danosoftware.galaxyforce.sprites.game.missiles.aliens.AlienMissileRotater.calculateAngle;
import static com.danosoftware.galaxyforce.sprites.game.missiles.aliens.AlienMissileRotater.calculateRotation;
import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenAnySide;

/**
 * Alien missile that targets the supplied base.
 * <p>
 * The missile will re-calculate it's direction to follow the base's moves.
 */
public class AlienMissileGuided extends AbstractAlienMissile {

    /* maximum missile change direction in radians */
    private static final float MAX_DIRECTION_CHANGE_ANGLE = 0.3f;

    /* time delay between missile direction changes */
    private static final float MISSILE_DIRECTION_CHANGE_DELAY = 0.1f;

    /* reference to base to target.
    /* (this base would have been active when missile was constructed but
    /* could later target an old base position if destroyed) */
    /* shouldn't be a problem but missile will target an old base position in this scenario */
    private final IBasePrimary base;

    // offset applied to x and y every move
    private int xDelta;
    private int yDelta;

    /* current calculateAngle of missile direction */
    private float angle;

    /* missile speed in pixels per seconds */
    private final int speed;

    /* variable to store time passed since last missile direction change */
    private float timeSinceMissileDirectionChange;

    // created rotated missile aimed at base
    public AlienMissileGuided(
            int xStart,
            int yStart,
            final Animation animation,
            final AlienMissileSpeed missileSpeed,
            final IBasePrimary base) {

        super(
                animation,
                xStart,
                yStart);

        this.base = base;

        // initial calculateAngle (i.e. straight down)
        this.angle = (float) Math.atan2(-1, 0);
        this.speed = missileSpeed.getSpeed();
        calculateMovements();

        // reset timer since last missile direction change
        timeSinceMissileDirectionChange = 0f;
    }

    @Override
    public void animate(float deltaTime) {
        super.animate(deltaTime);

        /*
         * Guide missile every x seconds so the missile changes direction to
         * follow any changes in the base's position.
         */
        timeSinceMissileDirectionChange += deltaTime;
        if (timeSinceMissileDirectionChange > MISSILE_DIRECTION_CHANGE_DELAY) {
            targetBase();
            timeSinceMissileDirectionChange = 0f;
        }

        // move missile by calculated deltas
        moveByDelta(
                (int) (xDelta * deltaTime),
                (int) (yDelta * deltaTime));

        // if missile is now off screen then destroy it
        if (offScreenAnySide(this)) {
            destroy();
        }
    }

    /**
     * Calculate calculateAngle and x and y deltas required to fire missile at base's
     * current position. May be called several times to ensure missile remains
     * targeted as base moves.
     */
    private void targetBase() {
        /*
         * only re-target if we have a current base. if we have no base then
         * don't change missile direction.
         */
        if (base != null) {
            // calculate angle from missile position to base
            final AlienMissileRotateCalculation calculation = calculateAngle(this, base);
            final float newAngle = calculation.getAngle();

            // don't allow sudden changes of direction. limit to MAX radians
            if ((newAngle - angle) > MAX_DIRECTION_CHANGE_ANGLE) {
                angle += MAX_DIRECTION_CHANGE_ANGLE;
            } else if ((newAngle - angle) < MAX_DIRECTION_CHANGE_ANGLE) {
                angle -= MAX_DIRECTION_CHANGE_ANGLE;
            } else {
                this.angle = newAngle;
            }

            // recalculate rotation and movement delta based on new calculateAngle
            calculateMovements();
        }
    }

    private void calculateMovements() {
        // calculate sprite rotation for wanted angle
        rotate((calculateRotation(angle)));

        // calculate the deltas to be applied each move
        this.xDelta = (int) (this.speed * (float) Math.cos(angle));
        this.yDelta = (int) (this.speed * (float) Math.sin(angle));
    }
}