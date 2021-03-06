package com.danosoftware.galaxyforce.waves.utilities;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.flightpath.paths.Path;
import com.danosoftware.galaxyforce.flightpath.paths.PathSpeed;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.utilities.Reversed;
import com.danosoftware.galaxyforce.utilities.WaveUtilities;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.SubWave;
import com.danosoftware.galaxyforce.waves.config.SubWaveConfig;
import com.danosoftware.galaxyforce.waves.config.SubWaveNoPathConfig;
import com.danosoftware.galaxyforce.waves.config.SubWavePathConfig;
import com.danosoftware.galaxyforce.waves.config.SubWaveRepeatMode;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.DelayedFollowerExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.MultiExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.SpawningExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileFiringConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileMultiFiringConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnOnDemandConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawningAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawningAndExplodingAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawningLimitedAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningBySpeedConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.BoundariesConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.BoundaryLanePolicy;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DirectionalDestroyableConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DirectionalResettableConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.ExplodingConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.FollowableHunterConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.FollowerConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.HunterConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.PathConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.SplitterConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.StaticConfig;
import com.danosoftware.galaxyforce.waves.rules.SubWavePathRule;
import com.danosoftware.galaxyforce.waves.rules.SubWaveRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.SCREEN_MID_X;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.alienConfig;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.alienRowConfig;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.directionalAlienConfig;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.fallingSpinningSplittingConfig;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.followableHunterConfigBuilder;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.followerConfigBuilder;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.hunterAlienConfig;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.spawningPathAlienConfig;
import static com.danosoftware.galaxyforce.waves.utilities.MazePatternCreator.mazePatternOne;
import static com.danosoftware.galaxyforce.waves.utilities.MazePatternCreator.mazePatternThree;
import static com.danosoftware.galaxyforce.waves.utilities.MazePatternCreator.mazePatternTwo;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createCentralDroppers;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createDescendingDelayedRowSubWave;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createDescendingOffsetRowsSubWave;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createDiamondDroppers;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createLeftToRightDelayedRowSubWave;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createLeftToRightOffsetRowSubWave;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createRowDroppers;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createSideDroppers;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createStaggeredDroppers;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.scatteredTopStart;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.staggeredDroppers;
import static com.danosoftware.galaxyforce.waves.utilities.WaveAsteroidsHelper.createAsteroidField;
import static com.danosoftware.galaxyforce.waves.utilities.WaveAsteroidsHelper.createMiniDirectionalAsteroid;
import static com.danosoftware.galaxyforce.waves.utilities.WaveDriftingHelper.createDriftingWave;
import static com.danosoftware.galaxyforce.waves.utilities.WaveFactoryHelper.DOWNWARDS;
import static com.danosoftware.galaxyforce.waves.utilities.WaveFactoryHelper.HALF_PI;
import static com.danosoftware.galaxyforce.waves.utilities.WaveFactoryHelper.NO_POWER_UPS;
import static com.danosoftware.galaxyforce.waves.utilities.WaveFactoryHelper.QUARTER_PI;
import static com.danosoftware.galaxyforce.waves.utilities.WaveFactoryHelper.flatten;
import static com.danosoftware.galaxyforce.waves.utilities.WaveMazeHelper.asteroidHorizontalRows;
import static com.danosoftware.galaxyforce.waves.utilities.WaveMazeHelper.asteroidMazeSubWave;
import static com.danosoftware.galaxyforce.waves.utilities.WaveMazeHelper.createBarrierMaze;
import static com.danosoftware.galaxyforce.waves.utilities.WaveMazeHelper.createBarrierMazeWithGuards;


/**
 * Creates a wave of aliens based on the provided wave number. Each wave
 * property can contain multiple sub-waves, each consisting of a number of
 * aliens following a path
 */
public class WaveFactory {

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

            /**
             * Wave 1
             * Combination of wavey lines in both directions
             * and descending aliens.
             */
            case 1:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE,
                                        alienConfig(
                                                AlienCharacter.PAD,
                                                AlienMissileSpeed.MEDIUM,
                                                15f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createStaggeredDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                0.25f),
                                        alienConfig(
                                                AlienCharacter.PURPLE_MEANIE,
                                                AlienMissileSpeed.MEDIUM,
                                                15f),
                                        NO_POWER_UPS
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE_REVERSE,
                                        alienConfig(
                                                AlienCharacter.PAD,
                                                AlienMissileSpeed.MEDIUM,
                                                15f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createStaggeredDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                0.25f),
                                        alienConfig(
                                                AlienCharacter.PURPLE_MEANIE,
                                                AlienMissileSpeed.MEDIUM,
                                                15f),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE,
                                        alienConfig(
                                                AlienCharacter.PAD,
                                                AlienMissileSpeed.MEDIUM,
                                                15f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE_REVERSE_LOWER,
                                        alienConfig(
                                                AlienCharacter.PAD,
                                                AlienMissileSpeed.MEDIUM,
                                                15f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createStaggeredDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                0.25f),
                                        alienConfig(
                                                AlienCharacter.PURPLE_MEANIE,
                                                AlienMissileSpeed.MEDIUM,
                                                15f),
                                        Collections.singletonList(PowerUpType.MISSILE_BLAST)
                                )
                        )
                );
                break;

            /**
             * Wave 2
             * Rows of aliens moving left to right.
             * Descending aliens at edges of screen.
             */
            case 2:
                // 5 rows of aliens moving left-to-right and then back
                // firing infrequently
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createLeftToRightDelayedRowSubWave(
                                        Path.LEFT_AND_RIGHT,
                                        PathSpeed.NORMAL,
                                        new AlienRowConfig[] {
                                                alienRowConfig(
                                                        AlienCharacter.PINKO,
                                                        AlienMissileSpeed.SLOW,
                                                        15f,
                                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)),
                                                alienRowConfig(
                                                        AlienCharacter.WALKER,
                                                        AlienMissileSpeed.SLOW,
                                                        30f,
                                                        NO_POWER_UPS),
                                                alienRowConfig(
                                                        AlienCharacter.FROGGER,
                                                        AlienMissileSpeed.SLOW,
                                                        30f,
                                                        NO_POWER_UPS),
                                                alienRowConfig(
                                                        AlienCharacter.FROGGER,
                                                        AlienMissileSpeed.SLOW,
                                                        30f,
                                                        Collections.singletonList(PowerUpType.SHIELD))},
                                        5,
                                        GAME_HEIGHT - 230,
                                        0f)
                        )
                );
                // 2 columns of aliens moving slowly down the screen
                // firing directly at base
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        createSideDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                5,
                                                0f),
                                        alienConfig(
                                                AlienCharacter.GHOST,
                                                AlienMissileSpeed.MEDIUM,
                                                8f),
                                        NO_POWER_UPS
                                )
                        )
                );
                // 5 rows of aliens moving left-to-right and then back
                // firing more frequently than previous sub-wave
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createLeftToRightDelayedRowSubWave(
                                        Path.LEFT_AND_RIGHT,
                                        PathSpeed.NORMAL,
                                        new AlienRowConfig[] {
                                                alienRowConfig(
                                                        AlienCharacter.PINKO,
                                                        AlienMissileSpeed.SLOW,
                                                        8f,
                                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)),
                                                alienRowConfig(
                                                        AlienCharacter.PAD,
                                                        AlienMissileSpeed.SLOW,
                                                        15f,
                                                        NO_POWER_UPS),
                                                alienRowConfig(
                                                        AlienCharacter.PURPLE_MEANIE,
                                                        AlienMissileSpeed.SLOW,
                                                        15f,
                                                        NO_POWER_UPS),
                                                alienRowConfig(
                                                        AlienCharacter.PURPLE_MEANIE,
                                                        AlienMissileSpeed.SLOW,
                                                        15f,
                                                        Collections.singletonList(PowerUpType.SHIELD))},
                                        5,
                                        GAME_HEIGHT - 230,
                                        0f)
                        )
                );
                break;

            /**
             * Wave 3
             * Triangle attacks from each side.
             * Central descending aliens
             * Triangle attacks from both sides at same time
             */
            case 3:
                // triangular attack from left-to-right
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_TRIANGULAR,
                                        alienConfig(
                                                AlienCharacter.FROGGER,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
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
                                        alienConfig(
                                                AlienCharacter.FROGGER,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                )
                        )
                );
                // aliens drop from centre
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        createCentralDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                5,
                                                0f),
                                        alienConfig(
                                                AlienCharacter.GHOST,
                                                AlienMissileSpeed.FAST,
                                                5f),
                                        NO_POWER_UPS
                                )
                        )
                );
                // both triangular attacks at same time
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_TRIANGULAR,
                                        alienConfig(
                                                AlienCharacter.PURPLE_MEANIE,
                                                AlienMissileSpeed.MEDIUM,
                                                8f),
                                        Collections.singletonList(PowerUpType.MISSILE_BLAST)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_TRIANGULAR_REVERSED,
                                        alienConfig(
                                                AlienCharacter.PURPLE_MEANIE,
                                                AlienMissileSpeed.MEDIUM,
                                                8f),
                                        NO_POWER_UPS
                                )
                        )
                );
                break;

            /**
             * Wave 4
             * Descending aliens in diamond and flat formations.
             * Mini-asteroid field.
             */
            case 4:
                // diamond and row formations of aliens descend from the top of the screen
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        createDiamondDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                0f),
                                        alienConfig(
                                                AlienCharacter.DROOPY,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createRowDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                3f),
                                        alienConfig(
                                                AlienCharacter.DROOPY),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY)
                                ),
                                new SubWavePathConfig(
                                        createDiamondDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                5f),
                                        alienConfig(
                                                AlienCharacter.PAD,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createRowDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                8f),
                                        alienConfig(
                                                AlienCharacter.PAD),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createDiamondDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                10f),
                                        alienConfig(
                                                AlienCharacter.PINKO,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createRowDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                13f),
                                        alienConfig(
                                                AlienCharacter.PINKO),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                )
                        )
                );
                // mini-asteroid field
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        scatteredTopStart(
                                                5,
                                                0f,
                                                1.2f),
                                        fallingSpinningSplittingConfig(
                                                AlienCharacter.ASTEROID,
                                                AlienSpeed.MEDIUM,
                                                AlienCharacter.ASTEROID_MINI),
                                        NO_POWER_UPS
                                ),
                                new SubWaveNoPathConfig(
                                        scatteredTopStart(
                                                5,
                                                1f,
                                                1.5f),
                                        fallingSpinningSplittingConfig(
                                                AlienCharacter.ASTEROID,
                                                AlienSpeed.VERY_FAST,
                                                AlienCharacter.ASTEROID_MINI),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                )
                        )
                );
                break;

            /**
             * Wave 5
             * Cross-over attacks from left, right and then both sides
             */
            case 5:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.CROSSING_STEP_ATTACK_LEFT_SHORT,
                                        alienConfig(
                                                AlienCharacter.PAD,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL))
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.CROSSING_STEP_ATTACK_RIGHT_SHORT,
                                        alienConfig(
                                                AlienCharacter.PAD,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY))
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.CROSSING_STEP_ATTACK_BOTH,
                                        alienConfig(
                                                AlienCharacter.PAD,
                                                AlienMissileSpeed.MEDIUM,
                                                8f),
                                        Collections.singletonList(PowerUpType.MISSILE_BLAST))
                        )
                );
                break;

            /**
             * Wave 6
             * Insect Motherships move at top of screen while spawning aliens that drop downwards.
             * Staggered descending aliens.
             * Insect Motherships move at top of screen while spawning aliens that drop downwards.
             */
            case 6:
                // spawning insect mothership
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_MOTHERSHIP,
                                        spawningPathAlienConfig(
                                                AlienCharacter.INSECT_MOTHERSHIP,
                                                SpawningAlienConfig
                                                        .builder()
                                                        .spawnedAlienConfig(
                                                                directionalAlienConfig(
                                                                        AlienCharacter.INSECT,
                                                                        DOWNWARDS,
                                                                        AlienSpeed.SLOW,
                                                                        AlienMissileSpeed.MEDIUM,
                                                                        1.5f))
                                                        .minimumSpawnDelayTime(0.5f)
                                                        .maximumAdditionalRandomSpawnDelayTime(0.25f)
                                                        .spwanedPowerUpTypes(
                                                                Arrays.asList(
                                                                        PowerUpType.MISSILE_GUIDED,
                                                                        PowerUpType.MISSILE_FAST,
                                                                        PowerUpType.MISSILE_PARALLEL))
                                                        .build()
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY)
                                )
                        )
                );
                // aliens drop in staggered pattern from top
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        staggeredDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                5,
                                                0f),
                                        alienConfig(
                                                AlienCharacter.GHOST,
                                                AlienMissileSpeed.FAST,
                                                5f),
                                        NO_POWER_UPS
                                )
                        )
                );
                // spawning insect mothership
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_MOTHERSHIP,
                                        spawningPathAlienConfig(
                                                AlienCharacter.INSECT_MOTHERSHIP,
                                                SpawningAlienConfig
                                                        .builder()
                                                        .spawnedAlienConfig(
                                                                directionalAlienConfig(
                                                                        AlienCharacter.INSECT,
                                                                        DOWNWARDS,
                                                                        AlienSpeed.SLOW,
                                                                        AlienMissileSpeed.MEDIUM,
                                                                        1.5f))
                                                        .minimumSpawnDelayTime(0.5f)
                                                        .maximumAdditionalRandomSpawnDelayTime(0.25f)
                                                        .spwanedPowerUpTypes(
                                                                Arrays.asList(
                                                                        PowerUpType.MISSILE_GUIDED,
                                                                        PowerUpType.MISSILE_FAST,
                                                                        PowerUpType.MISSILE_PARALLEL))
                                                        .build()
                                        ),
                                        Collections.singletonList(PowerUpType.SHIELD)
                                )
                        )
                );
                break;

            /**
             * Wave 7
             * Mini-barrier maze.
             * Selections of aliens descending in formation and then bouncing back-up
             */
            case 7:
                // navigate through empty barrier maze
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createBarrierMaze(
                                        8,
                                        AlienSpeed.VERY_FAST,
                                        2)
                        )
                );
                // diamond and row formations of aliens bounce from the top of the screen
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        createDiamondDroppers(
                                                Path.BOUNCE_DOWN_AND_UP,
                                                PathSpeed.NORMAL,
                                                0f),
                                        alienConfig(
                                                AlienCharacter.SPINNER_GREEN,
                                                AlienMissileSpeed.FAST,
                                                8f),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                ),
                                new SubWavePathConfig(
                                        createRowDroppers(
                                                Path.BOUNCE_DOWN_AND_UP,
                                                PathSpeed.NORMAL,
                                                3f),
                                        alienConfig(
                                                AlienCharacter.WALKER,
                                                AlienMissileSpeed.FAST,
                                                8f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createDiamondDroppers(
                                                Path.BOUNCE_DOWN_AND_UP,
                                                PathSpeed.NORMAL,
                                                5f),
                                        alienConfig(
                                                AlienCharacter.SPINNER_GREEN,
                                                AlienMissileSpeed.FAST,
                                                8f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createRowDroppers(
                                                Path.BOUNCE_DOWN_AND_UP,
                                                PathSpeed.NORMAL,
                                                8f),
                                        alienConfig(
                                                AlienCharacter.WALKER,
                                                AlienMissileSpeed.FAST,
                                                8f),
                                        Collections.singletonList(PowerUpType.MISSILE_FAST)
                                ),
                                new SubWavePathConfig(
                                        createDiamondDroppers(
                                                Path.BOUNCE_DOWN_AND_UP,
                                                PathSpeed.NORMAL,
                                                10f),
                                        alienConfig(
                                                AlienCharacter.PAD,
                                                AlienMissileSpeed.FAST,
                                                8f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createRowDroppers(
                                                Path.BOUNCE_DOWN_AND_UP,
                                                PathSpeed.NORMAL,
                                                13f),
                                        alienConfig(
                                                AlienCharacter.PAD,
                                                AlienMissileSpeed.FAST,
                                                8f),
                                        Collections.singletonList(PowerUpType.HELPER_BASES)
                                )
                        )
                );
                break;

            /**
             * Wave 8
             * Group figure-of-eight attack
             * Selection of aliens move from left to right in formation
             * Group figure-of-eight attack
             */
            case 8:
                // group figure-of-eight attack
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createLeftToRightOffsetRowSubWave(
                                        Path.FIGURE_OF_EIGHT,
                                        PathSpeed.NORMAL,
                                        new AlienRowConfig[] {
                                                alienRowConfig(
                                                        AlienCharacter.PURPLE_MEANIE,
                                                        AlienMissileSpeed.FAST,
                                                        5f,
                                                        Collections.singletonList(PowerUpType.SHIELD)),
                                                alienRowConfig(
                                                        AlienCharacter.PURPLE_MEANIE,
                                                        AlienMissileSpeed.FAST,
                                                        5f,
                                                        NO_POWER_UPS)},
                                        2,
                                        0,
                                        0f,
                                        false)
                        )
                );
                // aliens crossing screen in rows with
                // group figure-of-eight attack at same time
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                flatten(
                                        createLeftToRightOffsetRowSubWave(
                                                Path.FIGURE_OF_EIGHT,
                                                PathSpeed.NORMAL,
                                                new AlienRowConfig[] {
                                                        alienRowConfig(
                                                                AlienCharacter.PURPLE_MEANIE,
                                                                AlienMissileSpeed.FAST,
                                                                15f,
                                                                Collections.singletonList(PowerUpType.SHIELD)),
                                                        alienRowConfig(
                                                                AlienCharacter.PURPLE_MEANIE,
                                                                AlienMissileSpeed.FAST,
                                                                15f,
                                                                NO_POWER_UPS)},
                                                2,
                                                0,
                                                2f,
                                                false),
                                        createLeftToRightDelayedRowSubWave(
                                                Path.LEFT_AND_RIGHT,
                                                PathSpeed.NORMAL,
                                                new AlienRowConfig[] {
                                                        alienRowConfig(
                                                                AlienCharacter.GHOST,
                                                                AlienMissileSpeed.FAST,
                                                                10f,
                                                                Arrays.asList(PowerUpType.MISSILE_PARALLEL,PowerUpType.MISSILE_FAST)),
                                                        alienRowConfig(
                                                                AlienCharacter.PINKO,
                                                                AlienMissileSpeed.FAST,
                                                                10f,
                                                                NO_POWER_UPS),
                                                        alienRowConfig(
                                                                AlienCharacter.PAD,
                                                                AlienMissileSpeed.FAST,
                                                                8f,
                                                                NO_POWER_UPS),
                                                        alienRowConfig(
                                                                AlienCharacter.FROGGER,
                                                                AlienMissileSpeed.FAST,
                                                                8f,
                                                                NO_POWER_UPS)},
                                                5,
                                                GAME_HEIGHT - 230,
                                                0f
                                        )
                                )
                        )
                );
                // group figure-of-eight attack
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createLeftToRightOffsetRowSubWave(
                                        Path.FIGURE_OF_EIGHT,
                                        PathSpeed.NORMAL,
                                        new AlienRowConfig[] {
                                                alienRowConfig(
                                                        AlienCharacter.GHOST,
                                                        AlienMissileSpeed.FAST,
                                                        5f,
                                                        NO_POWER_UPS),
                                                alienRowConfig(
                                                        AlienCharacter.GHOST,
                                                        AlienMissileSpeed.FAST,
                                                        5f,
                                                        NO_POWER_UPS)},
                                        2,
                                        0,
                                        0f,
                                        false)
                        )
                );
                break;

            /**
             * Wave 9
             * Square path of aliens moving clockwise around screen.
             * Diagonal crossing path of aliens
             */
            case 9:
                // Square path aroud edge of screen
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.AROUND_EDGE,
                                        alienConfig(
                                                AlienCharacter.GHOST,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL))));
                // diagonal paths from each corner in turn
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.DIAGONAL_CROSSOVER,
                                        alienConfig(
                                                AlienCharacter.PAD,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL))));
                // diagonal paths from each corner at same time crossing at centre
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.DIAGONAL_CROSSOVER_INTERLEAVED,
                                        alienConfig(
                                                AlienCharacter.PURPLE_MEANIE,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL))));

                break;

            /**
             * Wave 10
             * Space Invader descending attack from top.
             * Cross-over attack from both sides
             * Space Invader descending attack from bottom.
             */
            case 10:
                // Space invaders attack from top, moving from left/right and gradually descending.
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SPACE_INVADER,
                                        alienConfig(
                                                AlienCharacter.WALKER,
                                                AlienMissileSpeed.MEDIUM,
                                                10f),
                                        Collections.singletonList(PowerUpType.MISSILE_BLAST)
                                )
                        )
                );
                // cross-over attack from both sides
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.CROSSOVER_EXIT_ATTACK_SPACED,
                                        alienConfig(
                                                AlienCharacter.LADY_BIRD,
                                                AlienMissileSpeed.FAST,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY)
                                )
                        )
                );
                // Space invaders attack from bottom, moving from left/right and gradually ascending.
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SPACE_INVADER_REVERSE,
                                        alienConfig(
                                                AlienCharacter.WALKER,
                                                AlienMissileSpeed.MEDIUM,
                                                10f),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                )
                        )
                );
                break;

            /**
             * Wave 11
             * Aliens drops in symmetrical columns and then bounce up.
             */
            case 11:
                // Aliens drops in symmetrical columns and then bounce up
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.STAGGERED_SYMMETRIC_BOUNCE_ATTACK,
                                        alienConfig(
                                                AlienCharacter.PAD,
                                                AlienMissileSpeed.FAST,
                                                10f),
                                        Arrays.asList(
                                                PowerUpType.SHIELD,
                                                PowerUpType.MISSILE_FAST)
                                )
                        )
                );
                // triangular attack from left-to-right
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_TRIANGULAR,
                                        alienConfig(
                                                AlienCharacter.GHOST,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        NO_POWER_UPS
                                )
                        )
                );
                // diagonal paths from each corner in turn
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.DIAGONAL_CROSSOVER,
                                        alienConfig(
                                                AlienCharacter.PURPLE_MEANIE,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL))));
                break;

            /**
             * Wave 12
             * Single long-tailed dragon that chases base around the screen.
             */
            case 12:
                // Long-tailed hunter dragon
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.RANDOM_TOP,
                                        followableHunterConfigBuilder(
                                                AlienCharacter.DRAGON_HEAD,
                                                AlienSpeed.VERY_FAST,
                                                AlienMissileSpeed.FAST,
                                                4f,
                                                BoundariesConfig.builder().build(),
                                                20,
                                                followerConfigBuilder(
                                                        AlienCharacter.DRAGON_BODY,
                                                        AlienSpeed.VERY_FAST
                                                )
                                                        // delay body explosions to sync with head
                                                        .explosionConfig(
                                                                DelayedFollowerExplosionConfig
                                                                        .builder()
                                                                        .delayTime(1.5f)
                                                                        .build())
                                                        .build(),
                                                Arrays.asList(
                                                        PowerUpType.SHIELD,
                                                        PowerUpType.MISSILE_SPRAY)
                                        )
                                                .explosionConfig(MultiExplosionConfig.builder()
                                                        .numberOfExplosions(10)
                                                        .maximumExplosionStartTime(1.5f)
                                                        .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.LIFE)
                                )
                        )
                );
                break;

            case 13:

                // ??????

                /**
                 * Aliens descend in spiral patterns from top-to-bottom
                 */
            case 14:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SPIRAL,
                                        alienConfig(
                                                AlienCharacter.FIGHTER,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)

                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SPIRAL,
                                        alienConfig(
                                                AlienCharacter.MINION,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        NO_POWER_UPS
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.DOUBLE_SPIRAL,
                                        alienConfig(
                                                AlienCharacter.MINION,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.LIFE)
                                )
                        )
                );
                break;

            case 15:


                /**
                 * Single bouncing aliens.
                 * Wave of descending aliens in diamonds and rows.
                 * Double layer of bouncing aliens.
                 */
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.BOUNCING,
                                        alienConfig(
                                                AlienCharacter.SKULL,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        createDiamondDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                0f),
                                        alienConfig(
                                                AlienCharacter.ZOGG,
                                                AlienMissileSpeed.FAST,
                                                3f),
                                        Arrays.asList(
                                                PowerUpType.MISSILE_FAST,
                                                PowerUpType.MISSILE_FAST)
                                ),
                                new SubWavePathConfig(
                                        createRowDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                3f),
                                        alienConfig(
                                                AlienCharacter.ZOGG),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createDiamondDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                4f),
                                        alienConfig(
                                                AlienCharacter.ZOGG,
                                                AlienMissileSpeed.FAST,
                                                4.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY)
                                )));
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.BOUNCING,
                                        alienConfig(
                                                AlienCharacter.FROGGER,
                                                AlienMissileSpeed.MEDIUM,
                                                4.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.BOUNCING_HIGHER,
                                        alienConfig(
                                                AlienCharacter.JUMPER,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
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
                                        SubWavePathRule.WAVE_MOTHERSHIP,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.INSECT_MOTHERSHIP)
                                                .energy(10)
                                                .spawnConfig(
                                                        SpawningLimitedAlienConfig
                                                                .builder()
                                                                .maximumActiveSpawnedAliens(5)
                                                                .spawnedAlienConfig(
                                                                        HunterConfig
                                                                                .builder()
                                                                                .alienCharacter(AlienCharacter.INSECT)
                                                                                .energy(1)
                                                                                .speed(AlienSpeed.MEDIUM)
                                                                                .boundaries(BoundariesConfig
                                                                                        .builder()
                                                                                        .lanePolicy(BoundaryLanePolicy.VERTICAL)
                                                                                        .lanes(3)
                                                                                        .build())
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
                                                        SpawningLimitedAlienConfig
                                                                .builder()
                                                                .maximumActiveSpawnedAliens(5)
                                                                .spawnedAlienConfig(
                                                                        HunterConfig
                                                                                .builder()
                                                                                .alienCharacter(AlienCharacter.INSECT)
                                                                                .energy(1)
                                                                                .speed(AlienSpeed.MEDIUM)
                                                                                .boundaries(BoundariesConfig
                                                                                        .builder()
                                                                                        .lanePolicy(BoundaryLanePolicy.VERTICAL)
                                                                                        .lanes(3)
                                                                                        .build())
                                                                                .missileConfig(
                                                                                        MissileFiringConfig
                                                                                                .builder()
                                                                                                .missileType(AlienMissileType.DOWNWARDS)
                                                                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                                                                .missileFrequency(1.5f)
                                                                                                .build())
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
                break;

            /**
             * Aliens attack in descending valley attack and then an ascending valley attack.
             */
            case 17:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE,
                                        alienConfig(
                                                AlienCharacter.OCTOPUS,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_INTERLEAVED,
                                        alienConfig(
                                                AlienCharacter.MINION,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_FAST)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_FLIPPED,
                                        alienConfig(
                                                AlienCharacter.OCTOPUS,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_INTERLEAVED_FLIPPED,
                                        alienConfig(
                                                AlienCharacter.MINION,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                )
                        )
                );
                break;

            /**
             * Base chased by two flying books. One book occupies left half or screen.
             * Other book occupies right half of screen.
             */
            case 18:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.MIDDLE_TOP,
                                        HunterConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.BOOK)
                                                .energy(10)
                                                .speed(AlienSpeed.VERY_FAST)
                                                .boundaries(
                                                        BoundariesConfig
                                                                .builder()
                                                                .maxX(SCREEN_MID_X)
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                ),
                                new SubWaveNoPathConfig(
                                        SubWaveRule.MIDDLE_BOTTOM,
                                        HunterConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.BOOK)
                                                .energy(10)
                                                .speed(AlienSpeed.VERY_FAST)
                                                .boundaries(
                                                        BoundariesConfig
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

            case 19:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.BELL_CURVE,
                                        alienConfig(
                                                AlienCharacter.ZOGG,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Arrays.asList(
                                                PowerUpType.MISSILE_LASER,
                                                PowerUpType.SHIELD)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.DOUBLE_BELL_CURVE,
                                        alienConfig(
                                                AlienCharacter.ZOGG,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Arrays.asList(
                                                PowerUpType.MISSILE_LASER,
                                                PowerUpType.SHIELD,
                                                PowerUpType.MISSILE_SPRAY)
                                )
                        )
                );
                break;

            case 20:

                /**
                 * Alien drops from top to bottom spawning aliens that move diagnonally
                 * down the screen while firing missiles targeted at the base.
                 */
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SINGLE_SPIRAL,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.WHIRLPOOL)
                                                .energy(20)
                                                .spawnConfig(
                                                        SpawningAlienConfig
                                                        .builder()
                                                        .maximumAdditionalRandomSpawnDelayTime(1f)
                                                        .minimumSpawnDelayTime(0.5f)
                                                        .spwanedPowerUpTypes(Arrays.asList(
                                                                PowerUpType.MISSILE_LASER,
                                                                PowerUpType.SHIELD,
                                                                PowerUpType.MISSILE_SPRAY))
                                                        .spawnedAlienConfig(
                                                                SplitterConfig
                                                                        .builder()
                                                                        .alienConfigs(
                                                                                Arrays.asList(
                                                                                        (AlienConfig) directionalAlienConfig(
                                                                                                AlienCharacter.BEAR,
                                                                                                -HALF_PI - QUARTER_PI,
                                                                                                AlienSpeed.FAST,
                                                                                                AlienMissileSpeed.SLOW,
                                                                                                4f),
                                                                                        (AlienConfig) directionalAlienConfig(
                                                                                                AlienCharacter.JOKER,
                                                                                                -HALF_PI + QUARTER_PI,
                                                                                                AlienSpeed.FAST,
                                                                                                AlienMissileSpeed.SLOW,
                                                                                                4f)
                                                                                        )
                                                                        )
                                                                        .build())
                                                        .build())
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                )
                        )
                );
                break;

            case 21:

                /**
                 * Maze to navigate through.
                 * First maze is empty.
                 * Second maze has alien guards blocking the gaps.
                 */
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createBarrierMaze(
                                        15,
                                        AlienSpeed.VERY_FAST,
                                        2)
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createBarrierMazeWithGuards(
                                        15,
                                        AlienSpeed.VERY_FAST,
                                        2,
                                        AlienCharacter.ROTATOR,
                                        Collections.singletonList(PowerUpType.MISSILE_LASER))
                        )
                );
                break;

            case 22:

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
                                                                        DirectionalDestroyableConfig
                                                                                .builder()
                                                                                .alienCharacter(AlienCharacter.INSECT)
                                                                                .energy(1)
                                                                                .angle(DOWNWARDS)
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
                                                                        DirectionalDestroyableConfig
                                                                                .builder()
                                                                                .alienCharacter(AlienCharacter.INSECT)
                                                                                .energy(1)
                                                                                .angle(DOWNWARDS)
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


            case 23:

                /**
                 * slow looper attack from both sides firing directional missiles.
                 * fast bell curve attack.
                 * fast curving attack from top-to-bottom with aliens on both sides
                 */
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.LOOPER_ATTACK,
                                        alienConfig(
                                                AlienCharacter.ROTATOR,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY)),
                                new SubWavePathConfig(
                                        SubWavePathRule.LOOPER_ATTACK_REVERSE,
                                        alienConfig(
                                                AlienCharacter.ROTATOR,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL))));
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.BELL_CURVE,
                                        alienConfig(
                                                AlienCharacter.FRISBIE,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER))));
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE_BENDING_RIGHT,
                                        alienConfig(
                                                AlienCharacter.LEMMING,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.HELPER_BASES)),
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE_BENDING_LEFT,
                                        alienConfig(
                                                AlienCharacter.LEMMING,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_FAST))));
                break;

            case 24:
                /**
                 * Alien spawns an egg that cracks and then spawns a dragon!!
                 */
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
                                                                                                                        BoundariesConfig.builder().build())
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

            case 25:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                mazePatternOne(
                                        AlienSpeed.MEDIUM,
                                        NO_POWER_UPS)
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                mazePatternTwo(
                                        AlienSpeed.VERY_FAST,
                                        NO_POWER_UPS)
                        )
                );
                break;

            case 26:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createBarrierMazeWithGuards(
                                        15,
                                        AlienSpeed.VERY_FAST,
                                        2,
                                        AlienCharacter.ROTATOR,
                                        Collections.singletonList(PowerUpType.MISSILE_LASER))
                        )
                );
                break;

            case 27:

                // ??????? place-holder need real level
                // old wave 19
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                asteroidMazeSubWave(
                                        15,
                                        AlienSpeed.SLOW,
                                        3,
                                        Collections.singletonList(PowerUpType.MISSILE_LASER))
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                asteroidMazeSubWave(
                                        15,
                                        AlienSpeed.SLOW,
                                        3,
                                        Collections.singletonList(PowerUpType.SHIELD))
                        )
                );
                break;

            case 28:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROIDS,
                                        DirectionalDestroyableConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.MOLECULE)
                                                .energy(5)
                                                .angle(DOWNWARDS)
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

            case 29:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                flatten(
                                        createLeftToRightOffsetRowSubWave(
                                                Path.LEFT_AND_RIGHT,
                                                PathSpeed.NORMAL,
                                                new AlienRowConfig[] {
                                                        alienRowConfig(
                                                                AlienCharacter.YELLOW_BEARD,
                                                                AlienMissileSpeed.FAST,
                                                                15f,
                                                                Collections.singletonList(PowerUpType.SHIELD)),
                                                        alienRowConfig(
                                                                AlienCharacter.YELLOW_BEARD,
                                                                AlienMissileSpeed.FAST,
                                                                15f,
                                                                Collections.singletonList(PowerUpType.SHIELD))
                                                },
                                                3,
                                                GAME_HEIGHT - 230,
                                                5f,
                                                true),
                                        mazePatternThree(
                                                AlienSpeed.MEDIUM,
                                                NO_POWER_UPS)
                                )
                        )
                );
                break;

            case 30:

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

            case 31:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.STAGGERED_LEFT_AND_RIGHT,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.CLOUD)
                                                .explosionConfig(MultiExplosionConfig.builder()
                                                        .numberOfExplosions(10)
                                                        .maximumExplosionStartTime(0.5f)
                                                        .build())
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
                                                .explosionConfig(MultiExplosionConfig.builder()
                                                        .numberOfExplosions(10)
                                                        .maximumExplosionStartTime(0.5f)
                                                        .build())
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


            case 32:

                // ????????

            case 33:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createBarrierMazeWithGuards(
                                        15,
                                        AlienSpeed.VERY_FAST,
                                        2,
                                        AlienCharacter.ROTATOR,
                                        Collections.singletonList(PowerUpType.MISSILE_LASER))
                        )
                );
                break;
            case 34:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROIDS,
                                        DirectionalResettableConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.ASTEROID)
                                                .energy(2)
                                                .speed(AlienSpeed.MEDIUM)
                                                .angle(DOWNWARDS)
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
                                                                                .spawnedPowerUpTypes(
                                                                                        NO_POWER_UPS)
                                                                                .spawnedAlienConfig(SplitterConfig
                                                                                        .builder()
                                                                                        .alienConfigs(
                                                                                                Arrays.asList(
                                                                                                        createMiniDirectionalAsteroid(-HALF_PI - QUARTER_PI, AlienSpeed.MEDIUM),
                                                                                                        createMiniDirectionalAsteroid(-HALF_PI + QUARTER_PI, AlienSpeed.MEDIUM)))
                                                                                        .build())
                                                                                .build())
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                ),
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROIDS,
                                        DirectionalResettableConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.ASTEROID)
                                                .energy(2)
                                                .speed(AlienSpeed.VERY_FAST)
                                                .angle(DOWNWARDS)
                                                .spinningConfig(
                                                        SpinningBySpeedConfig
                                                                .builder()
                                                                .build()
                                                )
                                                .explosionConfig(
                                                        SpawningExplosionConfig
                                                                .builder()
                                                                .spawnConfig(
                                                                        SpawnOnDemandConfig
                                                                                .builder()
                                                                                .spawnedPowerUpTypes(
                                                                                        NO_POWER_UPS)
                                                                                .spawnedAlienConfig(SplitterConfig
                                                                                        .builder()
                                                                                        .alienConfigs(
                                                                                                Arrays.asList(
                                                                                                        createMiniDirectionalAsteroid(-HALF_PI - QUARTER_PI, AlienSpeed.VERY_FAST),
                                                                                                        createMiniDirectionalAsteroid(-HALF_PI + QUARTER_PI, AlienSpeed.VERY_FAST)))
                                                                                        .build())
                                                                                .build())
                                                                .build()
                                                )
                                                .build(),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                )
                        )
                );
                break;

            case 35:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.TEAR_DROP_ATTACK,
                                        alienConfig(
                                                AlienCharacter.INSECT,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.HELPER_BASES)
                                )
                        )
                );
                break;


            case 36:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.STAGGERED_LEFT_AND_RIGHT,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.CLOUD)
                                                .explosionConfig(MultiExplosionConfig.builder()
                                                        .numberOfExplosions(10)
                                                        .maximumExplosionStartTime(0.5f)
                                                        .build())
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
                                                .explosionConfig(MultiExplosionConfig.builder()
                                                        .numberOfExplosions(10)
                                                        .maximumExplosionStartTime(0.5f)
                                                        .build())
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


            case 37:
            case 38:

                /**
                 * Aliens rise on both sides of screen and the cross to the other side.
                 * Repeat in reverse direction. Very few gaps between aliens to attack.
                 */
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SINGLE_ARC_NO_REPEAT,
                                        alienConfig(
                                                AlienCharacter.INSECT_MOTHERSHIP,
                                                AlienMissileSpeed.VERY_FAST,
                                                1f
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.CROSSOVER_EXIT_ATTACK_REVERSE,
                                        alienConfig(
                                                AlienCharacter.LADY_BIRD,
                                                AlienMissileSpeed.FAST,
                                                6.5f
                                        ),
                                        Collections.singletonList(PowerUpType.HELPER_BASES)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SINGLE_ARC_NO_REPEAT,
                                        alienConfig(
                                                AlienCharacter.INSECT_MOTHERSHIP,
                                                AlienMissileSpeed.VERY_FAST,
                                                1f
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.CROSSOVER_EXIT_ATTACK,
                                        alienConfig(
                                                AlienCharacter.LADY_BIRD,
                                                AlienMissileSpeed.FAST,
                                                6.5f
                                        ),
                                        Collections.singletonList(PowerUpType.HELPER_BASES)
                                )
                        )
                );
                break;

            case 39:

                // asteroid field
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createAsteroidField(Arrays.asList(
                                        PowerUpType.MISSILE_GUIDED,
                                        PowerUpType.MISSILE_FAST,
                                        PowerUpType.MISSILE_LASER))
                        )
                );
                break;

            case 40:
            case 41:
            case 42:

                /*
                 * Single hunter bat that starts from top of screen.
                 * Aims fireballs at base.
                 */
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.MIDDLE_TOP,
                                        hunterAlienConfig(
                                                AlienCharacter.BATTY,
                                                AlienSpeed.VERY_FAST,
                                                BoundariesConfig
                                                        .builder()
                                                        .build(),
                                                AlienMissileSpeed.VERY_FAST,
                                                2f
                                        ),
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
                                        SubWaveRule.MIDDLE_LEFT,
                                        hunterAlienConfig(
                                                AlienCharacter.BATTY,
                                                AlienSpeed.VERY_FAST,
                                                BoundariesConfig
                                                        .builder()
                                                        .maxX(SCREEN_MID_X)
                                                        .build(),
                                                AlienMissileSpeed.VERY_FAST,
                                                2f
                                        ),
                                        Collections.singletonList(PowerUpType.HELPER_BASES)),
                                new SubWaveNoPathConfig(
                                        SubWaveRule.MIDDLE_RIGHT,
                                        hunterAlienConfig(
                                                AlienCharacter.BATTY,
                                                AlienSpeed.VERY_FAST,
                                                BoundariesConfig
                                                        .builder()
                                                        .minX(SCREEN_MID_X)
                                                        .build(),
                                                AlienMissileSpeed.VERY_FAST,
                                                2f
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY))
                        )
                );
                break;

            case 43:
            case 44:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SLIDE_FORMATION_LEFT_RIGHT,
                                        alienConfig(
                                                AlienCharacter.MINION,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f
                                        ),
                                        Collections.singletonList(PowerUpType.LIFE)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SLIDE_FORMATION_RIGHT_LEFT,
                                        alienConfig(
                                                AlienCharacter.MINION,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f
                                        ),
                                        Collections.singletonList(PowerUpType.LIFE)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SLIDE_FORMATION_LEFT_RIGHT,
                                        alienConfig(
                                                AlienCharacter.MINION,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f
                                        ),
                                        Collections.singletonList(PowerUpType.LIFE)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.SLIDE_FORMATION_RIGHT_LEFT,
                                        alienConfig(
                                                AlienCharacter.MINION,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f
                                        ),
                                        Collections.singletonList(PowerUpType.LIFE)
                                )
                        )
                );
                break;
            case 45:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.FIGURE_OF_EIGHT,
                                        alienConfig(
                                                AlienCharacter.OCTOPUS,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)
                                )
                        )
                );
                break;

            case 46:
            case 47:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createDescendingDelayedRowSubWave(
                                        Path.BOUNCE_DOWN_AND_UP,
                                        PathSpeed.NORMAL,
                                        new AlienRowConfig[] {
                                                alienRowConfig(
                                                        AlienCharacter.FISH,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        Collections.singletonList(PowerUpType.SHIELD)
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.FISH,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        Collections.singletonList(PowerUpType.SHIELD)
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.PILOT,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.PILOT,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.ALL_SEEING_EYE,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.ALL_SEEING_EYE,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                                )
                                        },
                                        6,
                                        0f,
                                        true,
                                        false
                                )
                        )
                );

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createDescendingOffsetRowsSubWave(
                                        Path.BOUNCE_DOWN_AND_UP,
                                        PathSpeed.NORMAL,
                                        new AlienRowConfig[] {
                                                alienRowConfig(
                                                        AlienCharacter.ALL_SEEING_EYE,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.ALL_SEEING_EYE,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.PILOT,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.PILOT,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.FISH,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        Collections.singletonList(PowerUpType.SHIELD)
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.FISH,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        Collections.singletonList(PowerUpType.SHIELD)
                                                )
                                        },
                                        6,
                                        0f,
                                        true,
                                        false)
                        )
                );

                // old wave 22
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createDescendingDelayedRowSubWave(
                                        Path.BOUNCE_DOWN_AND_UP,
                                        PathSpeed.NORMAL,
                                        new AlienRowConfig[] {
                                                alienRowConfig(
                                                        AlienCharacter.SKULL,
                                                        AlienMissileSpeed.FAST,
                                                        6.5f,
                                                        Collections.singletonList(PowerUpType.SHIELD)
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.GOBBY,
                                                        AlienMissileSpeed.FAST,
                                                        6.5f,
                                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.MINION,
                                                        AlienMissileSpeed.FAST,
                                                        6.5f,
                                                        Collections.singletonList(PowerUpType.HELPER_BASES)
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.CIRCUIT,
                                                        AlienMissileSpeed.FAST,
                                                        6.5f,
                                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)
                                                ),
                                        },
                                        6,
                                        0f,
                                        true,
                                        false)
                        )
                );
                break;

            case 48:

                // asteroid field
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createAsteroidField(Arrays.asList(
                                        PowerUpType.MISSILE_GUIDED,
                                        PowerUpType.MISSILE_FAST,
                                        PowerUpType.MISSILE_LASER))
                        )
                );
                break;

            case 49:

                // alien test wave to check alien animation
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_TRIANGULAR,
                                        alienConfig(
                                                AlienCharacter.SPINNER_PULSE_GREEN,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f
                                        ),
                                        Collections.singletonList(PowerUpType.LIFE)
                                )
                        )
                );
                break;

            case 50:
            case 51:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createDriftingWave(
                                        AlienCharacter.SPACE_STATION,
                                        2,
                                        AlienSpeed.SLOW,
                                        Arrays.asList(
                                                PowerUpType.MISSILE_GUIDED,
                                                PowerUpType.MISSILE_FAST,
                                                PowerUpType.MISSILE_LASER),
                                        SpinningBySpeedConfig
                                                .builder()
                                                .build(),
                                        MissileFiringConfig
                                                .builder()
                                                .missileType(AlienMissileType.ROTATED)
                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                .missileFrequency(10f)
                                                .build())
                        )
                );
                break;

            case 52:
            case 53:
            case 54:
            case 55:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.CROSSING_STEP_ATTACK_BOTH,
                                        alienConfig(
                                                AlienCharacter.OCTOPUS,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_BLAST)
                                )
                        )
                );
                break;

            case 56:
            case 57:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createDriftingWave(
                                        AlienCharacter.SPACE_STATION,
                                        2,
                                        AlienSpeed.SLOW,
                                        Arrays.asList(
                                                PowerUpType.MISSILE_GUIDED,
                                                PowerUpType.MISSILE_FAST,
                                                PowerUpType.MISSILE_LASER),
                                        SpinningBySpeedConfig
                                                .builder()
                                                .build(),
                                        MissileFiringConfig
                                                .builder()
                                                .missileType(AlienMissileType.ROTATED)
                                                .missileCharacter(AlienMissileCharacter.LASER)
                                                .missileSpeed(AlienMissileSpeed.MEDIUM)
                                                .missileFrequency(10f)
                                                .build())
                        )
                );
                break;

            case 58:

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
                                                        SpawningLimitedAlienConfig
                                                                .builder()
                                                                .spawnedAlienConfig(
                                                                        FollowableHunterConfig
                                                                                .builder()
                                                                                .alienCharacter(AlienCharacter.DRAGON_HEAD)
                                                                                .energy(10)
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
                                                                                        BoundariesConfig.builder().build())
                                                                                .build())
                                                                .minimumSpawnDelayTime(5f)
                                                                .maximumAdditionalRandomSpawnDelayTime(2f)
                                                                .maximumActiveSpawnedAliens(2)
                                                                .limitedCharacter(AlienCharacter.DRAGON_HEAD)// max 2 dragon heads at any time
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

            case 59:
            case 60:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_MOTHERSHIP,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.BIG_BOSS)
                                                .energy(1)
                                                .explosionConfig(MultiExplosionConfig.builder()
                                                        .numberOfExplosions(10)
                                                        .maximumExplosionStartTime(1.5f)
                                                        .build())
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
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_MOTHERSHIP,
                                        PathConfig
                                                .builder()
                                                .alienCharacter(AlienCharacter.BIG_BOSS)
                                                .energy(1)
                                                .explosionConfig(MultiExplosionConfig.builder()
                                                        .numberOfExplosions(10)
                                                        .maximumExplosionStartTime(1.5f)
                                                        .explosionConfig(SpawningExplosionConfig
                                                                .builder()
                                                                .spawnConfig(
                                                                        SpawningAlienConfig
                                                                                .builder()
                                                                                .spawnedAlienConfig(
                                                                                        DirectionalDestroyableConfig
                                                                                                .builder()
                                                                                                .alienCharacter(AlienCharacter.INSECT)
                                                                                                .energy(1)
                                                                                                .angle(DOWNWARDS)
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
                                                                .build())
                                                        .build())
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


                // placeholders until real waves are created


                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                flatten(
                                        new SubWavePathConfig[]{
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
                                        },
                                        asteroidHorizontalRows(
                                                10,
                                                AlienSpeed.SLOW,
                                                2,
                                                Collections.singletonList(PowerUpType.MISSILE_LASER))
                                ))
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
    private static List<IAlien> reverseAliens(List<IAlien> aliens) {
        List<IAlien> reversedAlienList = new ArrayList<>();

        for (IAlien eachAlien : Reversed.reversed(aliens)) {
            reversedAlienList.add(eachAlien);
        }

        return reversedAlienList;
    }
}
