package com.danosoftware.galaxyforce.sprites.game.behaviours.fire;

import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.MissileFiringConfig;

public class FireBehaviourFactory {

    /**
     * Create fire behaviour based on the supplied  alien config.
     */
    public static FireBehaviour createFireBehaviour(
            final GameModel model,
            final AlienConfig alienConfig) {

        final MissileConfig missileConfig = alienConfig.getMissileConfig();
        if (missileConfig != null
                && missileConfig.getType() == MissileConfig.MissileConfigType.MISSILE
                && missileConfig instanceof MissileFiringConfig) {

            final MissileFiringConfig firingMissileConfig = (MissileFiringConfig) missileConfig;

            // behaviour that fires missiles depending on config
            return new FireCyclicWithRandomStart(
                    model,
                    firingMissileConfig.getMissileType(),
                    firingMissileConfig.getMissileSpeed(),
                    firingMissileConfig.getMissileCharacter(),
                    firingMissileConfig.getMissileFrequency());
        }

        // behaviour that fires no missiles
        return new FireDisabled();
    }
}
