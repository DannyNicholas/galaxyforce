package com.danosoftware.galaxyforce.waves.utilities;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.utilities.Reversed;
import com.danosoftware.galaxyforce.utilities.WaveUtilities;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.SubWave;
import com.danosoftware.galaxyforce.waves.config.SubWaveConfig;
import com.danosoftware.galaxyforce.waves.config.SubWaveNoPathConfig;
import com.danosoftware.galaxyforce.waves.config.SubWavePathConfig;
import com.danosoftware.galaxyforce.waves.config.SubWaveRepeatMode;
import com.danosoftware.galaxyforce.waves.config.aliens.DescendingConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.ExplodingConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.FollowableHunterConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.FollowerConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.HunterBoundariesConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.HunterConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.MissileFiringConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.MissileMultiFiringConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.PathConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.SpawningAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.SpawningAndExplodingAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.SpinningBySpeedConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.SpinningFixedAngularConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.StaticConfig;
import com.danosoftware.galaxyforce.waves.rules.SubWavePathRule;
import com.danosoftware.galaxyforce.waves.rules.SubWaveRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.danosoftware.galaxyforce.constants.GameConstants.SCREEN_MID_X;


/**
 * Creates a wave of aliens based on the provided wave number. Each wave
 * property can contain multiple sub-waves, each consisting of a number of
 * aliens following a path
 */
public class WaveFactory {

    private static final List<PowerUpType> NO_POWER_UPS = Collections.emptyList();

    private final WaveCreationUtils creationUtils;
    private final PowerUpAllocatorFactory powerUpAllocatorFactory;

    public WaveFactory(
            WaveCreationUtils creationUtils,
            PowerUpAllocatorFactory powerUpAllocatorFactory) {
        this.creationUtils = creationUtils;
        this.powerUpAllocatorFactory = powerUpAllocatorFactory;
    }

    /**
     * Return a collection of sub-waves based on the current wave number
     *
     * @param wave - wave number
     * @return collection of sub-waves
     */
    public List<SubWave> createWave(int wave) {
        if (!WaveUtilities.isValidWave(wave)) {
            throw new GalaxyForceException("Wave not recognised '" + wave + "'.");
        }

        // reset power-up allocation factory for a new wave
        powerUpAllocatorFactory.newWave();

        List<SubWave> subWaves = new ArrayList<>();

        switch (wave) {

            case 1:

                // triangular attack from left-to-right
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_TRIANGULAR,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.LIFE)
                                )
                        )
                );
                // triangular attack from right-to-left
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_TRIANGULAR_REVERSED,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                )
                        )
                );
                // both triangular attacks at same time
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_TRIANGULAR,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_BLAST)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_TRIANGULAR_REVERSED,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        NO_POWER_UPS
                                )
                        )
                );
                break;

            case 2:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_INTERLEAVED,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.MINION)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_FAST)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_FLIPPED,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_INTERLEAVED_FLIPPED,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.MINION)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                )
                        )
                );
                break;

            case 3:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        NO_POWER_UPS
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE_REVERSE,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        NO_POWER_UPS
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE_DOUBLE,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                )
                        )
                );
                break;

            case 4:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SPIRAL,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)

                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SPIRAL,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.MINION)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        NO_POWER_UPS
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.DOUBLE_SPIRAL,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.MINION)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.LIFE)
                                )
                        )
                );
                break;

            case 5:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_MOTHERSHIP,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.INSECT_MOTHERSHIP)
                                                .energy(10)
                                                .spawnConfig(
                                                        SpawningAlienConfig
                                                                .builder()
                                                                .spawnedAlienConfig(
                                                                        DescendingConfig
                                                                                .builder()
                                                                                .alienCharacter(AlienCharacter.INSECT)
                                                                                .energy(1)
                                                                                .missileConfig(
                                                                                        MissileFiringConfig
                                                                                                .builder()
                                                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                                                .missileFrequency(1.5f)
                                                                                                .build())
                                                                                .speed(AlienSpeed.SLOW)
                                                                                .build())
                                                                .minimumSpawnDelayTime(0.5f)
                                                                .maximumAdditionalRandomSpawnDelayTime(0.25f)
                                                                .spwanedPowerUpTypes(
                                                                        Arrays.asList(
                                                                                PowerUpType.MISSILE_GUIDED,
                                                                                PowerUpType.MISSILE_FAST,
                                                                                PowerUpType.MISSILE_PARALLEL))
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_MOTHERSHIP,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.INSECT_MOTHERSHIP)
                                                .energy(10)
                                                .spawnConfig(
                                                        SpawningAlienConfig
                                                                .builder()
                                                                .spawnedAlienConfig(
                                                                        DescendingConfig
                                                                                .builder()
                                                                                .alienCharacter(AlienCharacter.INSECT)
                                                                                .energy(1)
                                                                                .missileConfig(
                                                                                        MissileFiringConfig
                                                                                                .builder()
                                                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                                                .missileFrequency(1.5f)
                                                                                                .build())
                                                                                .speed(AlienSpeed.SLOW)
                                                                                .build())
                                                                .minimumSpawnDelayTime(0.5f)
                                                                .maximumAdditionalRandomSpawnDelayTime(0.25f)
                                                                .spwanedPowerUpTypes(
                                                                        Arrays.asList(
                                                                                PowerUpType.MISSILE_GUIDED,
                                                                                PowerUpType.MISSILE_FAST,
                                                                                PowerUpType.MISSILE_PARALLEL))
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.SHIELD)
                                )
                        )
                );
                break;

            case 6:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE_DOUBLE,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY)
                                )
                        )
                );
                break;

            case 7:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROIDS,
                                        DescendingConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.ASTEROID)
                                                .energy(2)
                                                .speed(AlienSpeed.MEDIUM)
                                                .spinningConfig(
                                                        SpinningBySpeedConfig
                                                                .builder()
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                ),
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROIDS,
                                        DescendingConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.ASTEROID)
                                                .energy(2)
                                                .speed(AlienSpeed.VERY_FAST)
                                                .spinningConfig(
                                                        SpinningBySpeedConfig
                                                                .builder()
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                )
                        )
                );
                break;

            case 8:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SPACE_INVADER,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.GOBBY)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Arrays.asList(PowerUpType.MISSILE_BLAST, PowerUpType.LIFE)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SPACE_INVADER_REVERSE,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.GOBBY)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                )
                        )
                );
                break;

            case 9:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.STAGGERED_BOUNCE_ATTACK,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.SHIELD)
                                )
                        )
                );
                break;

            case 10:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.DRAGON_CHASE,
                                        FollowableHunterConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.DRAGON_HEAD)
                                                .energy(20)
                                                .speed(AlienSpeed.VERY_FAST)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.ROTATED)
                                                                .missileCharacter(AlienMissileCharacter.FIREBALL)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .numberOfFollowers(20)
                                                .followerConfig(
                                                        FollowerConfig
                                                                .builder()
                                                                .alienCharacter(AlienCharacter.DRAGON_BODY)
                                                                .energy(1)
                                                                .speed(AlienSpeed.VERY_FAST)
                                                                .build())
                                                .followerPowerUps(
                                                        Arrays.asList(
                                                                PowerUpType.MISSILE_GUIDED,
                                                                PowerUpType.MISSILE_PARALLEL,
                                                                PowerUpType.MISSILE_SPRAY))
                                                .boundaries(
                                                        HunterBoundariesConfig.builder().build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.LIFE)
                                )
                        )
                );
                break;

            case 11:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.FIGURE_OF_EIGHT,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)
                                )
                        )
                );
                break;

            case 12:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.CROSSING_STEP_ATTACK,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_BLAST)
                                )
                        )
                );
                break;

            case 13:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.CROSSOVER_EXIT_ATTACK,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.OCTOPUS)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.HELPER_BASES)
                                )
                        )
                );
                break;

            case 14:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.BELL_CURVE,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.ZOGG)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.DOUBLE_BELL_CURVE,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.ZOGG)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.SHIELD)
                                )
                        )
                );
                break;

            case 15:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.LOOPER_ATTACK,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.SKULL)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)
                                )
                        )
                );
                break;

            case 16:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.TEAR_DROP_ATTACK,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.INSECT)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.HELPER_BASES)
                                )
                        )
                );
                break;

            case 17:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROID_FIELD,
                                        DescendingConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.ASTEROID)
                                                .energy(5)
                                                .speed(AlienSpeed.SLOW)
                                                .spinningConfig(
                                                        SpinningBySpeedConfig
                                                                .builder()
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.SHIELD)
                                )
                        )
                );
                break;

            case 18:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SPIRAL,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.SKULL)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.DOUBLE_SPIRAL,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.SKULL)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.LIFE)
                                )
                        )
                );
                break;

            case 19:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROID_MAZE_EASY,
                                        DescendingConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.ASTEROID)
                                                .energy(5)
                                                .speed(AlienSpeed.SLOW)
                                                .spinningConfig(
                                                        SpinningBySpeedConfig
                                                                .builder()
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROID_MAZE,
                                        DescendingConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.ASTEROID)
                                                .energy(5)
                                                .speed(AlienSpeed.SLOW)
                                                .spinningConfig(
                                                        SpinningBySpeedConfig
                                                                .builder()
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.SHIELD)
                                )
                        )
                );
                break;

            case 20:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.HUNTER_TOP,
                                        HunterConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.BOOK)
                                                .energy(10)
                                                .speed(AlienSpeed.VERY_FAST)
                                                .boundaries(
                                                        HunterBoundariesConfig
                                                                .builder()
                                                                .maxX(SCREEN_MID_X)
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                ),
                                new SubWaveNoPathConfig(
                                        SubWaveRule.HUNTER_BOTTOM,
                                        HunterConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.BOOK)
                                                .energy(10)
                                                .speed(AlienSpeed.VERY_FAST)
                                                .boundaries(
                                                        HunterBoundariesConfig
                                                                .builder()
                                                                .minX(SCREEN_MID_X)
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                )
                        )
                );
                break;

            case 21:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.BOMBER_RUN,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.ZOGG)
                                                .energy(10)
                                                .spawnConfig(
                                                        SpawningAlienConfig
                                                                .builder()
                                                                .spawnedAlienConfig(
                                                                        ExplodingConfig
                                                                                .builder()
                                                                                .alienCharacter(AlienCharacter.BOMB)
                                                                                .energy(1)
                                                                                .explosionTime(3f)
                                                                                .explodingMissileCharacter(AlienMissileCharacter.FIREBALL)
                                                                                .build())
                                                                .minimumSpawnDelayTime(1f)
                                                                .maximumAdditionalRandomSpawnDelayTime(0.5f)
                                                                .spwanedPowerUpTypes(
                                                                        Arrays.asList(
                                                                                PowerUpType.MISSILE_GUIDED,
                                                                                PowerUpType.MISSILE_FAST,
                                                                                PowerUpType.MISSILE_PARALLEL))
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY)
                                )
                        )
                );
                break;

            case 22:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.FLAT_ATTACK_ROW_1,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.SKULL)
                                                .energy(3)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.FAST)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.SHIELD)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.FLAT_ATTACK_ROW_2,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.GOBBY)
                                                .energy(3)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.FAST)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.FLAT_ATTACK_ROW_3,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.MINION)
                                                .energy(3)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.FAST)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.HELPER_BASES)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.FLAT_ATTACK_ROW_4,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.CIRCUIT)
                                                .energy(3)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LIGHTNING)
                                                                .missileSpeed(AlienMissileSpeed.FAST)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .spinningConfig(
                                                        SpinningFixedAngularConfig
                                                                .builder()
                                                                .angularSpeed(70)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)
                                )
                        )
                );
                break;

            case 23:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.BOMBER_RUN,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.ZOGG)
                                                .energy(10)
                                                .spawnConfig(
                                                        SpawningAlienConfig
                                                                .builder()
                                                                .spawnedAlienConfig(
                                                                        FollowableHunterConfig
                                                                                .builder()
                                                                                .alienCharacter(AlienCharacter.DRAGON_HEAD)
                                                                                .energy(20)
                                                                                .speed(AlienSpeed.VERY_FAST)
                                                                                .missileConfig(
                                                                                        MissileFiringConfig
                                                                                                .builder()
                                                                                                .missileType(AlienMissileType.ROTATED)
                                                                                                .missileCharacter(AlienMissileCharacter.FIREBALL)
                                                                                                .missileSpeed(AlienMissileSpeed.VERY_FAST)
                                                                                                .missileFrequency(6.5f)
                                                                                                .build())
                                                                                .numberOfFollowers(5)
                                                                                .followerConfig(
                                                                                        FollowerConfig
                                                                                                .builder()
                                                                                                .alienCharacter(AlienCharacter.DRAGON_BODY)
                                                                                                .energy(1)
                                                                                                .speed(AlienSpeed.VERY_FAST)
                                                                                                .build())
                                                                                .followerPowerUps(
                                                                                        Arrays.asList(
                                                                                                PowerUpType.MISSILE_GUIDED,
                                                                                                PowerUpType.MISSILE_PARALLEL,
                                                                                                PowerUpType.MISSILE_SPRAY))
                                                                                .boundaries(
                                                                                        HunterBoundariesConfig.builder().build())
                                                                                .build())
                                                                .minimumSpawnDelayTime(5f)
                                                                .maximumAdditionalRandomSpawnDelayTime(2f)
                                                                .spwanedPowerUpTypes(
                                                                        Arrays.asList(
                                                                                PowerUpType.MISSILE_GUIDED,
                                                                                PowerUpType.MISSILE_FAST,
                                                                                PowerUpType.MISSILE_PARALLEL))
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY)
                                )
                        )
                );
                break;

            case 24:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.STAGGERED_LEFT_AND_RIGHT,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.CLOUD)
                                                .energy(10)
                                                .missileConfig(
                                                        MissileMultiFiringConfig
                                                                .builder()
                                                                .missileConfigs(
                                                                        Arrays.asList(
                                                                                MissileFiringConfig
                                                                                        .builder()
                                                                                        .missileType(AlienMissileType.DOWNWARDS)
                                                                                        .missileCharacter(AlienMissileCharacter.RAIN)
                                                                                        .missileSpeed(AlienMissileSpeed.VERY_FAST)
                                                                                        .missileFrequency(0.75f)
                                                                                        .build(),
                                                                                MissileFiringConfig
                                                                                        .builder()
                                                                                        .missileType(AlienMissileType.ROTATED)
                                                                                        .missileCharacter(AlienMissileCharacter.LIGHTNING)
                                                                                        .missileSpeed(AlienMissileSpeed.VERY_FAST)
                                                                                        .missileFrequency(5f)
                                                                                        .build()))
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY))
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.STAGGERED_LEFT_AND_RIGHT_REVERSED,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.CLOUD)
                                                .energy(10)
                                                .missileConfig(
                                                        MissileMultiFiringConfig
                                                                .builder()
                                                                .missileConfigs(
                                                                        Arrays.asList(
                                                                                MissileFiringConfig
                                                                                        .builder()
                                                                                        .missileType(AlienMissileType.DOWNWARDS)
                                                                                        .missileCharacter(AlienMissileCharacter.RAIN)
                                                                                        .missileSpeed(AlienMissileSpeed.VERY_FAST)
                                                                                        .missileFrequency(2f)
                                                                                        .build(),
                                                                                MissileFiringConfig
                                                                                        .builder()
                                                                                        .missileType(AlienMissileType.ROTATED)
                                                                                        .missileCharacter(AlienMissileCharacter.LIGHTNING)
                                                                                        .missileSpeed(AlienMissileSpeed.VERY_FAST)
                                                                                        .missileFrequency(8f)
                                                                                        .build()))
                                                                .build())
                                                .build(),
                                        Arrays.asList(PowerUpType.SHIELD, PowerUpType.HELPER_BASES, PowerUpType.MISSILE_LASER))
                        )
                );
                break;

            case 25:
                /*
                 * Single hunter bat that starts from top of screen.
                 * Aims fireballs at base.
                 */
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.HUNTER_TOP,
                                        HunterConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.BATTY)
                                                .energy(10)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.ROTATED)
                                                                .missileCharacter(AlienMissileCharacter.FIREBALL)
                                                                .missileSpeed(AlienMissileSpeed.VERY_FAST)
                                                                .missileFrequency(2f)
                                                                .build())
                                                .speed(AlienSpeed.VERY_FAST)
                                                .boundaries(
                                                        HunterBoundariesConfig
                                                                .builder()
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.SHIELD))
                        )
                );
                /*
                 * Two hunter bats that start from left and right of screen.
                 * Each bat flies within the left or right hand-side of screen.
                 * Both aim fireballs at base.
                 */
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.HUNTER_LEFT,
                                        HunterConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.BATTY)
                                                .energy(10)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.ROTATED)
                                                                .missileCharacter(AlienMissileCharacter.FIREBALL)
                                                                .missileSpeed(AlienMissileSpeed.VERY_FAST)
                                                                .missileFrequency(2f)
                                                                .build())
                                                .speed(AlienSpeed.VERY_FAST)
                                                .boundaries(
                                                        HunterBoundariesConfig
                                                                .builder()
                                                                .maxX(SCREEN_MID_X)
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.SHIELD)),
                                new SubWaveNoPathConfig(
                                        SubWaveRule.HUNTER_RIGHT,
                                        HunterConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.BATTY)
                                                .energy(10)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.ROTATED)
                                                                .missileCharacter(AlienMissileCharacter.FIREBALL)
                                                                .missileSpeed(AlienMissileSpeed.VERY_FAST)
                                                                .missileFrequency(2f)
                                                                .build())
                                                .speed(AlienSpeed.VERY_FAST)
                                                .boundaries(
                                                        HunterBoundariesConfig
                                                                .builder()
                                                                .minX(SCREEN_MID_X)
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY))
                        )
                );
                break;


            case 26:
                // alien test wave to check alien animation
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_TRIANGULAR,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.SPINNER_PULSE_GREEN)
                                                .energy(4)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.LIFE)
                                )
                        )
                );
                break;


            case 27:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROIDS,
                                        DescendingConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.MOLECULE)
                                                .energy(5)
                                                .speed(AlienSpeed.MEDIUM)
                                                .spinningConfig(
                                                        SpinningBySpeedConfig
                                                                .builder()
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                )
                        )
                );
                break;

            /**
             * Alien spawns an egg that cracks and then spawns a dragon!!
             */
            case 28:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.BOMBER_RUN,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.ZOGG)
                                                .energy(10)
                                                .spawnConfig(
                                                        SpawningAlienConfig
                                                                .builder()
                                                                .spawnedAlienConfig(
                                                                        StaticConfig
                                                                                .builder()
                                                                                .alienCharacter(AlienCharacter.EGG)
                                                                                .energy(1)
                                                                                .spawnConfig(
                                                                                        SpawningAndExplodingAlienConfig.builder()
                                                                                                .spawnedAlienConfig(
                                                                                                    FollowableHunterConfig
                                                                                                            .builder()
                                                                                                            .alienCharacter(AlienCharacter.DRAGON_HEAD)
                                                                                                            .energy(5)
                                                                                                            .speed(AlienSpeed.VERY_FAST)
                                                                                                            .missileConfig(
                                                                                                                    MissileFiringConfig
                                                                                                                            .builder()
                                                                                                                            .missileType(AlienMissileType.ROTATED)
                                                                                                                            .missileCharacter(AlienMissileCharacter.FIREBALL)
                                                                                                                            .missileSpeed(AlienMissileSpeed.VERY_FAST)
                                                                                                                            .missileFrequency(6.5f)
                                                                                                                            .build())
                                                                                                            .numberOfFollowers(5)
                                                                                                            .followerConfig(
                                                                                                                    FollowerConfig
                                                                                                                            .builder()
                                                                                                                            .alienCharacter(AlienCharacter.DRAGON_BODY)
                                                                                                                            .energy(1)
                                                                                                                            .speed(AlienSpeed.VERY_FAST)
                                                                                                                            .build())
                                                                                                            .followerPowerUps(
                                                                                                                    Arrays.asList(
                                                                                                                            PowerUpType.MISSILE_GUIDED,
                                                                                                                            PowerUpType.MISSILE_PARALLEL,
                                                                                                                            PowerUpType.MISSILE_SPRAY))
                                                                                                            .boundaries(
                                                                                                                    HunterBoundariesConfig.builder().build())
                                                                                                            .build())
                                                                                                .spwanedPowerUpType(PowerUpType.MISSILE_GUIDED)
                                                                                                .spawnDelayTime(2.25f)  // aligns to egg cracking animation 9 x 0.25f
                                                                                        .build()
                                                                                )
                                                                                .build())
                                                                .minimumSpawnDelayTime(1f)
                                                                .maximumAdditionalRandomSpawnDelayTime(5f)
                                                                .spwanedPowerUpTypes(
                                                                        Arrays.asList(
                                                                                PowerUpType.MISSILE_GUIDED,
                                                                                PowerUpType.MISSILE_FAST,
                                                                                PowerUpType.MISSILE_PARALLEL))
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY)
                                )
                        )
                );
                break;


            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
                // placeholders until real waves are created

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.BELL_CURVE,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.ZOGG)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.HELPER_BASES)
                                ),
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROID_FIELD,
                                        DescendingConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.ASTEROID)
                                                .energy(5)
                                                .speed(AlienSpeed.SLOW)
                                                .spinningConfig(
                                                        SpinningBySpeedConfig
                                                                .builder()
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.LOOPER_ATTACK,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.SKULL)
                                                .energy(1)
                                                .missileConfig(
                                                        MissileFiringConfig
                                                                .builder()
                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                .missileFrequency(6.5f)
                                                                .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                )
                        )
                );
                break;

            default:
                throw new IllegalArgumentException("Wave not recognised '" + wave + "'.");

        }

        return subWaves;
    }


    /**
     * Creates a list of aliens on a path using the supplied wave property.
     */
    private SubWave createSubWave(
            final SubWaveRepeatMode repeatedMode,
            final SubWaveConfig... subWaveConfigs) {

        List<IAlien> aliens = new ArrayList<>();

        for (SubWaveConfig config : subWaveConfigs) {

            switch (config.getType()) {
                case PATH:
                    SubWavePathConfig pathConfig = (SubWavePathConfig) config;
                    aliens.addAll(creationUtils.createPathAlienSubWave(pathConfig));
                    break;
                case NO_PATH:
                    SubWaveNoPathConfig noPathConfig = (SubWaveNoPathConfig) config;
                    aliens.addAll(creationUtils.createNoPathAlienSubWave(noPathConfig));
                    break;
                default:
                    throw new GalaxyForceException("Unknown sub-wave config type: " + config.getType().name());
            }
        }

        /*
         * Reverse order of aliens.
         *
         * Collision detection routines are required to iterate through aliens
         * in reverse so aliens on top are hit first.
         *
         * Any subsequent explosions on these aliens must also display on top so
         * reversed order is important for how aliens sprites are displayed.
         */
        List<IAlien> reversedAlienList = reverseAliens(aliens);

        // create subwave from list of aliens and set whether wave should repeat
        // until all destroyed
        return new SubWave(reversedAlienList, repeatedMode);
    }

    /**
     * Reverse order of aliens.
     * <p>
     * Collision detection routines are required to iterate through aliens in
     * reverse so aliens on top are hit first.
     * <p>
     * Any subsequent explosions on these aliens must also display on top so
     * reversed order is important for how aliens sprites are displayed.
     */
    private List<IAlien> reverseAliens(List<IAlien> aliens) {
        List<IAlien> reversedAlienList = new ArrayList<>();

        for (IAlien eachAlien : Reversed.reversed(aliens)) {
            reversedAlienList.add(eachAlien);
        }

        return reversedAlienList;
    }
}
