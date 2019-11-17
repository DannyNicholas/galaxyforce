package com.danosoftware.galaxyforce.sprites.game.behaviours.spinner;

import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningBySpeedConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningFixedAngularConfig;

public class SpinningBehaviourFactory {

    public SpinningBehaviour createSpinningBehaviour(
            final SpinningConfig spinningConfig,
            final AlienSpeed speed) {

        if (spinningConfig != null
                && spinningConfig.getType() == SpinningConfig.SpinningConfigType.SPEED_BASED_ANGULAR_ROTATION
                && spinningConfig instanceof SpinningBySpeedConfig) {

            // behaviour that spins alien depending on supplied alien speed
            return new SpinningByFixedAngularRotation(
                    speed);
        }

        // create spinning behaviour based on non-speed based factory method
        return createSpinningBehaviour(spinningConfig);
    }

    public SpinningBehaviour createSpinningBehaviour(
            final SpinningConfig spinningConfig) {

        if (spinningConfig != null
                && spinningConfig.getType() == SpinningConfig.SpinningConfigType.FIXED_ANGULAR_ROTATION
                && spinningConfig instanceof SpinningFixedAngularConfig) {

            final SpinningFixedAngularConfig spinningFixedAngularConfig = (SpinningFixedAngularConfig) spinningConfig;

            // behaviour that spins alien depending on fixed angular speed
            return new SpinningByFixedAngularRotation(
                    spinningFixedAngularConfig.getAngularSpeed());
        }

        // behaviour that fires no missiles
        return new SpinningDisabled();
    }
}
