package com.danosoftware.galaxyforce.waves.utilities;

import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.config.SubWaveConfig;
import com.danosoftware.galaxyforce.waves.config.SubWaveNoPathConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.SpawningExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnOnDemandConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningBySpeedConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DirectionalConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DriftingConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.SplitterConfig;
import com.danosoftware.galaxyforce.waves.rules.SubWaveRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WaveFactoryHelper {

    public static final List<PowerUpType> NO_POWER_UPS = Collections.emptyList();
    public static final float HALF_PI = (float)(Math.PI/2f);
    public static final float PI = (float)(Math.PI);
    public static final float QUARTER_PI = (float)(Math.PI/4f);

    /**
     * Create an asteroid field with asteroids at random positions, moving at specific angles.
     * On explosion, each asteroid will spawn two mini-asteroids at 45 degree angles
     */
    public static SubWaveConfig[] createAsteroidField(final List<PowerUpType> powerUpTypes) {

        /*
         * Normally power-ups are allocated across a sub-wave config.
         * For this asteroid field, each asteroid has its own sub-wave config so we need
         * to create our own local power-up allocator to share power-ups across our
         * asteroids.
         */
        final PowerUpAllocator powerUpAllocator = new PowerUpAllocator(
                powerUpTypes,
                3 * 4,  // expected number of asteroids
                0);

        return flatten(
                createSurroundingAsteroids(-HALF_PI - (PI / 5.0f), AlienSpeed.MEDIUM, powerUpAllocator),
                createSurroundingAsteroids(-HALF_PI + (PI / 7.0f), AlienSpeed.FAST, powerUpAllocator),
                createSurroundingAsteroids(-HALF_PI + (PI / 3.0f), AlienSpeed.MEDIUM, powerUpAllocator)
        );
    }

    /**
     * Create asteroids that start off screen: top, bottom, left and right.
     * The angles are adjusted so asteroids move towards centre of screen.
     */
    public static SubWaveConfig[] createSurroundingAsteroids(
            final float angle,
            final AlienSpeed speed,
            final PowerUpAllocator powerUpAllocator) {
        return new SubWaveConfig[] {
                createAsteroids(SubWaveRule.RANDOM_TOP, angle, speed, powerUpAllocator),
                createAsteroids(SubWaveRule.RANDOM_LEFT, angle + HALF_PI, speed, powerUpAllocator),
                createAsteroids(SubWaveRule.RANDOM_BOTTOM, angle + PI, speed, powerUpAllocator),
                createAsteroids(SubWaveRule.RANDOM_RIGHT, angle - HALF_PI, speed, powerUpAllocator)
        };
    }

    private static SubWaveConfig createAsteroids(
            final SubWaveRule subWaveRule,
            final float angle,
            final AlienSpeed speed,
            final PowerUpAllocator powerUpAllocator) {

        final PowerUpType powerUp = powerUpAllocator.allocate();

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
                                                        // do not assign power-ups. would result in same
                                                        // power-up being given to both split asteroids.
                                                        .spwanedPowerUpTypes(
                                                                NO_POWER_UPS)
                                                        .spawnedAlienConfig(
                                                                SplitterConfig
                                                                    .builder()
                                                                    .alienConfigs(
                                                                            Arrays.asList(
                                                                                    createMiniDriftingAsteroid(angle - QUARTER_PI, speed),
                                                                                    createMiniDriftingAsteroid(angle + QUARTER_PI, speed)))
                                                                .build())
                                                        .build())
                                        .build()
                        )
                        .build(),
                powerUp == null ? NO_POWER_UPS : Collections.singletonList(powerUp)
        );
    }

    // drifting mini-asteroid drifting that will split off
    private static AlienConfig createMiniDriftingAsteroid(
            final float angle,
            final AlienSpeed speed) {

        return DriftingConfig
            .builder()
            .alienCharacter(AlienCharacter.ASTEROID_MINI)
            .energy(1)
            .speed(speed)
            .spinningConfig(
                    SpinningBySpeedConfig
                            .builder()
                            .build())
            .angle(angle)
            .build();
    }

    // directional mini-asteroid that will split off
    public static AlienConfig createMiniDirectionalAsteroid(
            final float angle,
            final AlienSpeed speed) {

        return DirectionalConfig
            .builder()
            .alienCharacter(AlienCharacter.ASTEROID_MINI)
            .energy(1)
            .speed(speed)
            .spinningConfig(
                    SpinningBySpeedConfig
                            .builder()
                            .build())
            .angle(angle)
            .build();
    }

    /*
     * Flatten an array of sub-wave configs into a single arrray
     */
    private static SubWaveConfig[] flatten(SubWaveConfig[]... lists) {
        List<SubWaveConfig> list = new ArrayList<>();
        for (SubWaveConfig[] array: lists) {
            list.addAll(Arrays.asList(array));
        }
        SubWaveConfig[] itemsArray = new SubWaveConfig[list.size()];
        return list.toArray(itemsArray);
    }
}
