package com.danosoftware.galaxyforce.sprites.game.missiles.aliens;

import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.view.Animation;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenBottom;

public class AlienMissileSimple extends AbstractAlienMissile {

    // missile speed in pixels per second
    private final int missileSpeed;

    public AlienMissileSimple(
            int xStart,
            int yStart,
            final Animation animation,
            final AlienMissileSpeed missileSpeed) {
        super(
                animation,
                xStart,
                yStart);
        this.missileSpeed = -missileSpeed.getSpeed();
    }

    @Override
    public void animate(float deltaTime) {
        super.animate(deltaTime);

        moveYByDelta((int) (missileSpeed * deltaTime));

        // if missile is now off screen then destroy it
        if (offScreenBottom(this)) {
            destroy();
        }
    }
}
