package com.danosoftware.galaxyforce.sprites.game.missiles.aliens;

import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.view.Animation;

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

    // offset applied to x and y every move
    private final int xDelta;
    private final int yDelta;

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
        final float angle;
        if (base != null) {
            angle = (float) Math.atan2(
                    base.y() - yStart,
                    base.x() - xStart);
        } else {
            // if base is null fire downwards
            angle = (float) Math.atan2(-1, 0);
        }

        // convert angle to degrees for sprite rotation.
        // needs to be adjusted by 90 deg for correct rotation.
        rotate((int) ((angle - Math.PI / 2f) * (180f / Math.PI)));

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