package com.danosoftware.galaxyforce.sprites.game.missiles.bases;

import com.danosoftware.galaxyforce.enumerations.BaseMissileSpeed;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.view.Animation;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenAnySide;

/**
 * Guided missile that chases a chosen base.
 */
public class BaseMissileGuided extends AbstractBaseMissile {

    private static final float PI_BY_TWO = (float) Math.PI / 2f;
    private static final float TWO_PI = (float) Math.PI * 2f;
    private static final float DEGREES_PER_PI = (float) (180f / Math.PI);

    /* time delay between missile direction changes */
    private static final float MISSILE_DIRECTION_CHANGE_DELAY = 0.1f;

    /* maximum missile change direction in radians */
    private static final float MAX_DIRECTION_CHANGE_ANGLE = 0.3f;

    /* distance missile can move in pixels each second */
    private final int missileSpeed;

    /* alien targeted by missile */
    private IAlien alien;

    /* reference to model */
    private final GameModel model;

    /* offset applied to x and y every move */
    private int xDelta;
    private int yDelta;

    /* current angle of missile direction */
    private float angle;

    /* variable to store time passed since last missile direction change */
    private float timeSinceMissileDirectionChange;

    public BaseMissileGuided(
            final int xStart,
            final int yStart,
            final Animation animation,
            final BaseMissileSpeed baseMissileSpeed,
            final GameModel model) {
        super(
                animation,
                xStart,
                yStart);
        this.model = model;
        this.missileSpeed = baseMissileSpeed.getSpeed();

        // initial angle (i.e. straight up)
        this.angle = PI_BY_TWO;

        // calculate rotation and movement delta based on angle
        calculateMovements();

        // reset timer since last missile direction change
        timeSinceMissileDirectionChange = 0f;
    }

    @Override
    public void animate(float deltaTime) {

        timeSinceMissileDirectionChange += deltaTime;

        /*
         * Guide missile every x seconds so the missile changes direction to
         * follow any changes in the alien's position.
         */
        if (timeSinceMissileDirectionChange > MISSILE_DIRECTION_CHANGE_DELAY) {
            /*
             * if no alien selected or alien no longer active then choose a new
             * target.
             */
            if (alien == null || !alien.isActive()) {
                chooseActiveAlien();
            }

            // re-target alien based on current position
            targetAlien();

            // reset timer since last missile direction change
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
     * Choose an active alien for the missile's next target. Called when picking
     * the original alien target or if alien is destroyed before missile hits
     * it.
     */
    private void chooseActiveAlien() {
        // choose a random active alien
        alien = model.chooseActiveAlien();
    }

    /**
     * Calculate angle and x and y deltas required to fire missile at alien's
     * current position. May be called several times to ensure missile remains
     * targeted as alien moves.
     */
    private void targetAlien() {
        /*
         * only re-target if we have a current alien. if we have no alien then
         * don't change missile direction.
         */
        if (alien != null) {
            // calculate angle from missile position to alien
            float newAngle = (float) Math.atan2(
                    alien.y() - this.y(),
                    alien.x() - this.x());

            // adjust angle so that a result more negative than PI/2
            // becomes a positive value. Gives missile a more direct
            // route to target (otherwise goes the long-way around).
            if (newAngle <  -PI_BY_TWO) {
                newAngle = newAngle + TWO_PI;
            }

            // don't allow sudden changes of direction. limit to MAX radians
            if ((newAngle - angle) > MAX_DIRECTION_CHANGE_ANGLE) {
                angle += MAX_DIRECTION_CHANGE_ANGLE;
            } else if ((newAngle - angle) < MAX_DIRECTION_CHANGE_ANGLE) {
                angle -= MAX_DIRECTION_CHANGE_ANGLE;
            } else {
                this.angle = newAngle;
            }

            // recalculate rotation and movement delta based on new angle
            calculateMovements();
        }
    }

    private void calculateMovements() {
        // convert angle to degrees for sprite rotation.
        // needs to be adjusted by 90 deg for correct rotation.
        rotate((int) ((angle - PI_BY_TWO) * DEGREES_PER_PI));

        // calculate the deltas to be applied each move
        this.xDelta = (int) (missileSpeed * (float) Math.cos(angle));
        this.yDelta = (int) (missileSpeed * (float) Math.sin(angle));
    }
}
