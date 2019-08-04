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
 * The missile will calculate it's direction on construction and will continue
 * to move in that direction until off the screen.
 * <p>
 * This missile will not change direction once fired even if the targeted base
 * moves.
 *
 * @author Danny
 */
public class AlienMissileRotated extends AbstractAlienMissile {

    private static final float DEGREES_PER_PI_RADIANS = (float) (180f / Math.PI);

    // offset applied to x and y every move
    private final int xDelta;
    private final int yDelta;

    // created rotated missile aimed at base
    public AlienMissileRotated(
            int xStart,
            int yStart,
            final Animation animation,
            final AlienMissileSpeed missileSpeed,
            final IBasePrimary base) {

        super(
                animation,
                xStart,
                yStart);

        // calculate angle from missile position to base
        final AlienMissileRotateCalculation calculation = calculateAngle(this, base);
        final float angle = calculation.getAngle();
        rotate(calculation.getRotation());

        // calculate the deltas to be applied each move
        this.xDelta = (int) (missileSpeed.getSpeed() * (float) Math.cos(angle));
        this.yDelta = (int) (missileSpeed.getSpeed() * (float) Math.sin(angle));
    }

    // created rotated missile of supplied angle
    public AlienMissileRotated(
            int xStart,
            int yStart,
            final Animation animation,
            final AlienMissileSpeed missileSpeed,
            final float angle) {

        super(
                animation,
                xStart,
                yStart);

        // calculate sprite rotation for wanted angle
        rotate((calculateRotation(angle)));

        // calculate the deltas to be applied each move
        this.xDelta = (int) (missileSpeed.getSpeed() * (float) Math.cos(angle));
        this.yDelta = (int) (missileSpeed.getSpeed() * (float) Math.sin(angle));
    }

    @Override
    public void animate(float deltaTime) {
        super.animate(deltaTime);

        // move missile by calculated deltas
        moveByDelta(
                (int) (xDelta * deltaTime),
                (int) (yDelta * deltaTime));

        // if missile is now off screen then destroy it
        if (offScreenAnySide(this)) {
            destroy();
        }
    }
}