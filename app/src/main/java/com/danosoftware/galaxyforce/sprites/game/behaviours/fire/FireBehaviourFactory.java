package com.danosoftware.galaxyforce.sprites.game.behaviours.fire;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.waves.config.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.AlienWithMissileConfig;
import com.danosoftware.galaxyforce.waves.config.MissileConfig;

public class FireBehaviourFactory {

    /**
     * Create fire behaviour based on the supplied  alien config.
     */
    public static FireBehaviour createFireBehaviour(
            final GameModel model,
            final AlienConfig alienConfig,
            final AlienMissileCharacter missileCharacter) {

        if (alienConfig.getType() == AlienConfig.Type.MISSILE
                && alienConfig instanceof AlienWithMissileConfig) {

            MissileConfig missileConfig = ((AlienWithMissileConfig) alienConfig).getMissileConfig();

            // behaviour that fires missiles depending on config
            return new FireCyclicWithRandomStart(
                    model,
                    missileConfig.getMissileType(),
                    missileConfig.getMissileSpeed(),
                    missileCharacter,
                    missileConfig.getMissileFrequency());
        }

        // behaviour that fires no missiles
        return new FireDisabled();
    }
}
