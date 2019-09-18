package com.danosoftware.galaxyforce.sprites.game.missiles.bases;

import com.danosoftware.galaxyforce.enumerations.BaseMissileSpeed;
import com.danosoftware.galaxyforce.view.Animation;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenAnySide;

/**
 * Single missile of a blast. Missiles fires in a fixed direction.
 */
public class BaseMissileBlast extends AbstractBaseMissile {

    /* offset applied to x and y every move */
    private final int xDelta;
    private final int yDelta;

    public BaseMissileBlast(
            final int xStart,
            final int yStart,
            final Animation animation,
            final float angle,
            final BaseMissileSpeed missileSpeed) {

        super(animation, xStart, yStart);

        // convert angle to degrees for sprite rotation.
        // needs to be adjusted by 90 deg for correct rotation.
        rotate((int) ((angle - Math.PI / 2f) * (180f / Math.PI)));

        // calculate the deltas to be applied each move
        this.xDelta = (int) (missileSpeed.getSpeed() * (float) Math.cos(angle));
        this.yDelta = (int) (missileSpeed.getSpeed() * (float) Math.sin(angle));
    }

    @Override
    public void animate(float deltaTime) {
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
