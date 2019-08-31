package com.danosoftware.galaxyforce.sprites.game.behaviours.spinner;

import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;

/**
 * Spins alien at a fixed angular speed.
 * Angular speed can be relative to it's movement speed speed or fixed to a value.
 */
public class SpinningByFixedAngularRotation implements SpinningBehaviour {

    /* current angle for sprite rotation */
    private float angle;

    /* speed of sprite rotation */
    private final int anglularSpeed;

    /**
     * construct fixed speed spinning behaviour based on supplied alien speed
     */
    public SpinningByFixedAngularRotation(final AlienSpeed speed) {

        // set angular rotation speed relative to downward speed
        this((speed.getSpeedInPixelsPerSeconds() - 50) * 2);
    }

    /**
     * construct fixed speed spinning behaviour based on supplied angular speed
     */
    public SpinningByFixedAngularRotation(final int angularSpeed) {

        // set random starting rotation angle
        this.angle = (float) (Math.random() * 360);
        this.anglularSpeed = angularSpeed;
    }

    @Override
    public boolean isSpinning() {
        return true;
    }

    @Override
    public void spin(IAlien alien, float deltaTime) {
        // rotate alien
        angle = (angle + (deltaTime * anglularSpeed)) % 360;
        alien.rotate((int) (angle));
    }
}
