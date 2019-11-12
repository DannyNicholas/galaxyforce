package com.danosoftware.galaxyforce.waves.utilities;

import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.config.SubWaveConfig;
import com.danosoftware.galaxyforce.waves.config.SubWaveNoPathConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.SpawningExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnOnDemandConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningBySpeedConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DriftingConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.SplitterConfig;
import com.danosoftware.galaxyforce.waves.rules.SubWaveRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WaveFactoryHelper {

    private static final List<PowerUpType> NO_POWER_UPS = Collections.emptyList();
    private static final float HALF_PI = (float)(Math.PI/2f);
    private static final float PI = (float)(Math.PI);
    private static final float QUARTER_PI = (float)(Math.PI/4f);

    /*
     * Flatten an array of arrays into a single arrray
     */
    public static SubWaveConfig[] flatten(SubWaveConfig[]... lists) {
        List<SubWaveConfig> list = new ArrayList<>();
        for (SubWaveConfig[] array: lists) {
            list.addAll(Arrays.asList(array));
        }
        SubWaveConfig[] itemsArray = new SubWaveConfig[list.size()];
        return list.toArray(itemsArray);
    }

    /**
     * Create an asteroid field with asteroids at random positions, moving at specific angles.
     * On explosion, each asteroid will spawn two mini-asteroids at 45 degree angles
     */
    public static SubWaveConfig[] createSurroundingAsteroids(final float angle, final AlienSpeed speed) {
        return new SubWaveConfig[] {
                createAsteroids(SubWaveRule.RANDOM_TOP, angle, speed),
                createAsteroids(SubWaveRule.RANDOM_LEFT, angle + HALF_PI, speed),
                createAsteroids(SubWaveRule.RANDOM_BOTTOM, angle + PI, speed),
                createAsteroids(SubWaveRule.RANDOM_RIGHT, angle - HALF_PI, speed)
        };
    }

    private static SubWaveConfig createAsteroids(
            final SubWaveRule subWaveRule,
            final float angle,
            final AlienSpeed speed) {
        return new SubWaveNoPathConfig(
                subWaveRule,
                DriftingConfig
                        .builder()
                        .alienCharacter(AlienCharacter.ASTEROID)
                        .energy(2)
                        .speed(speed)
                        .angle(angle)
                        .spinningConfig(
                                SpinningBySpeedConfig
                                        .builder()
                                        .build())
                        .explosionConfig(
                                SpawningExplosionConfig
                                        .builder()
                                        .spawnConfig(
                                                SpawnOnDemandConfig
                                                        .builder()
                                                        .spwanedPowerUpTypes(
                                                                NO_POWER_UPS)
                                                        .spawnedAlienConfig(SplitterConfig
                                                                .builder()
                                                                .alienCharacter(AlienCharacter.ASTEROID_MINI)
                                                                .energy(1)
                                                                .speed(speed)
                                                                .spinningConfig(
                                                                        SpinningBySpeedConfig
                                                                                .builder()
                                                                                .build())
                                                                .angles(
                                                                        Arrays.asList(
                                                                                angle - QUARTER_PI ,
                                                                                angle + QUARTER_PI))
                                                                .build())
                                                        .build())
                                        .build()
                        )
                        .build(),
                Collections.singletonList(PowerUpType.MISSILE_LASER)
        );
    }
}
