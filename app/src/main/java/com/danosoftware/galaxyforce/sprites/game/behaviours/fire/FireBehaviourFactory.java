package com.danosoftware.galaxyforce.sprites.game.behaviours.fire;

import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileFiringConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileMultiFiringConfig;

import java.util.ArrayList;
import java.util.List;

public class FireBehaviourFactory {

    /**
     * Create fire behaviour based on the supplied  alien config.
     */
    public static FireBehaviour createFireBehaviour(
            final GameModel model,
            final MissileConfig missileConfig) {

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

        if (missileConfig != null
                && missileConfig.getType() == MissileConfig.MissileConfigType.MULTI_MISSILE
                && missileConfig instanceof MissileMultiFiringConfig) {

            final MissileMultiFiringConfig multiFiringMissileConfig = (MissileMultiFiringConfig) missileConfig;

            // behaviour that fires missiles depending on config
            final List<FireBehaviour> fireBehaviours = new ArrayList<>();
            for (MissileFiringConfig config : multiFiringMissileConfig.getMissileConfigs()) {
                fireBehaviours.add(
                        new FireCyclicWithRandomStart(
                                model,
                                config.getMissileType(),
                                config.getMissileSpeed(),
                                config.getMissileCharacter(),
                                config.getMissileFrequency())
                );
            }
            return new MultiFireBehaviour(fireBehaviours);
        }

        // behaviour that fires no missiles
        return new FireDisabled();
    }
}
