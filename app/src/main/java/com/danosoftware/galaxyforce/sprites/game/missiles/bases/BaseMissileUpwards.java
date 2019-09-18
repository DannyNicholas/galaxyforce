package com.danosoftware.galaxyforce.sprites.game.missiles.bases;

import com.danosoftware.galaxyforce.enumerations.BaseMissileSpeed;
import com.danosoftware.galaxyforce.view.Animation;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenTop;

/**
 * Missile that fires straight upwards
 */
public class BaseMissileUpwards extends AbstractBaseMissile {

    /* distance missile can move per second */
    private final int missileSpeed;

    public BaseMissileUpwards(
            final int xStart,
            final int yStart,
            final Animation animation,
            final BaseMissileSpeed baseMissileSpeed) {
        super(
                animation,
                xStart,
                yStart);
        this.missileSpeed = baseMissileSpeed.getSpeed();
    }

    @Override
    public void animate(float deltaTime) {

        moveYByDelta((int) (missileSpeed * deltaTime));

        // if missile is now off screen then destroy it
        if (offScreenTop(this)) {
            destroy();
        }
    }
}
