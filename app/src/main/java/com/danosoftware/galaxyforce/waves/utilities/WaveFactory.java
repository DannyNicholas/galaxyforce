package com.danosoftware.galaxyforce.waves.utilities;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.flightpath.paths.Path;
import com.danosoftware.galaxyforce.flightpath.paths.PathSpeed;
import com.danosoftware.galaxyforce.flightpath.translators.FlipXPointTranslator;
import com.danosoftware.galaxyforce.flightpath.translators.OffsetYPointTranslator;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;
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
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawningLimitedActiveAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawningLimitedAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningBySpeedConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.BoundariesConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.BoundaryLanePolicy;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DirectionalDestroyableConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DirectionalResettableConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.ExplodingConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.FollowableHunterConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.FollowerConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.PathConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.SplitterConfig;
import com.danosoftware.galaxyforce.waves.rules.SubWavePathRule;
import com.danosoftware.galaxyforce.waves.rules.SubWaveRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;
import static com.danosoftware.galaxyforce.constants.GameConstants.SCREEN_MID_X;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.alienConfig;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.alienRowConfig;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.directionalAlienConfig;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.fallingSpinningSplittingConfig;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.followableHunterConfigBuilder;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.followerConfigBuilder;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.hunterAlienConfig;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.spawningPathAlienConfig;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.spawningStaticAlienConfig;
import static com.danosoftware.galaxyforce.waves.utilities.MazePatternCreator.MAZE_FOUR;
import static com.danosoftware.galaxyforce.waves.utilities.MazePatternCreator.MAZE_ONE;
import static com.danosoftware.galaxyforce.waves.utilities.MazePatternCreator.MAZE_THREE;
import static com.danosoftware.galaxyforce.waves.utilities.MazePatternCreator.MAZE_TWO;
import static com.danosoftware.galaxyforce.waves.utilities.MazePatternCreator.createMaze;
import static com.danosoftware.galaxyforce.waves.utilities.MazePatternCreator.mazeDoubler;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createBoxDroppers;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createCentralDroppers;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createDescendingDelayedRowSubWave;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createDescendingOffsetRowsSubWave;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createDiamondDroppers;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createDoubleDroppers;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createDoubleStaggeredDroppers;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createLeftToRightDelayedRowSubWave;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createLeftToRightOffsetRowSubWave;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createRowDroppers;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createSideDroppers;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.createStaggeredDroppers;
import static com.danosoftware.galaxyforce.waves.utilities.PathWaveHelper.scatteredTopStart;
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
                                        0f,
                                        false,
                                        new PointTranslatorChain()
                                                .add(new OffsetYPointTranslator(GAME_HEIGHT - 230)))
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
                                        0f,
                                        false,
                                        new PointTranslatorChain()
                                                .add(new OffsetYPointTranslator(GAME_HEIGHT - 230)))
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
                                        SubWavePathRule.NORMAL_ARC,
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
                                        createDoubleStaggeredDroppers(
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
                                        SubWavePathRule.NORMAL_ARC,
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
                                                0f,
                                                false,
                                                new PointTranslatorChain()
                                                        .add(new OffsetYPointTranslator(GAME_HEIGHT - 230))
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

            /**
             * Wave 13
             * Single column spiral attack from top
             * Interleaved rows of aliens moving in different directions.
             * Double column spiral attack from top
             */
            case 13:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        createCentralDroppers(
                                                Path.SPIRAL,
                                                PathSpeed.FAST,
                                                10,
                                                0f,
                                                1f,
                                                false
                                        ),
                                        alienConfig(
                                                AlienCharacter.FIGHTER,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_FAST)
                                )
                        )
                );
                // 4 rows of aliens moving horizontally
                // rows 1 and 3 start from left hand-side
                // rows 2 and 4 start from right hand-side
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                flatten(
                                        createLeftToRightDelayedRowSubWave(
                                                Path.LEFT_AND_RIGHT,
                                                PathSpeed.NORMAL,
                                                new AlienRowConfig[]{
                                                        alienRowConfig(
                                                                AlienCharacter.FOXY,
                                                                AlienMissileSpeed.SLOW,
                                                                8f,
                                                                NO_POWER_UPS)
                                                },
                                                5,
                                                0f,
                                                false,
                                                new PointTranslatorChain()
                                                        .add(new OffsetYPointTranslator(GAME_HEIGHT - 230))
                                        ),
                                        createLeftToRightDelayedRowSubWave(
                                                Path.LEFT_AND_RIGHT,
                                                PathSpeed.NORMAL,
                                                new AlienRowConfig[]{
                                                        alienRowConfig(
                                                                AlienCharacter.HOPPER,
                                                                AlienMissileSpeed.SLOW,
                                                                15f,
                                                                NO_POWER_UPS)
                                                },
                                                5,
                                                0f,
                                                false,
                                                new PointTranslatorChain()
                                                        .add(new OffsetYPointTranslator(GAME_HEIGHT - 230 - 80))
                                                        .add(new FlipXPointTranslator(GAME_WIDTH))
                                        ),
                                        createLeftToRightDelayedRowSubWave(
                                                Path.LEFT_AND_RIGHT,
                                                PathSpeed.NORMAL,
                                                new AlienRowConfig[]{
                                                        alienRowConfig(
                                                                AlienCharacter.CHEEKY,
                                                                AlienMissileSpeed.SLOW,
                                                                15f,
                                                                NO_POWER_UPS)
                                                },
                                                5,
                                                0f,
                                                false,
                                                new PointTranslatorChain()
                                                        .add(new OffsetYPointTranslator(GAME_HEIGHT - 230 - 80 - 80))
                                        ),
                                        createLeftToRightDelayedRowSubWave(
                                                Path.LEFT_AND_RIGHT,
                                                PathSpeed.NORMAL,
                                                new AlienRowConfig[]{
                                                        alienRowConfig(
                                                                AlienCharacter.PINCER,
                                                                AlienMissileSpeed.SLOW,
                                                                15f,
                                                                NO_POWER_UPS)
                                                },
                                                5,
                                                0f,
                                                false,
                                                new PointTranslatorChain()
                                                        .add(new OffsetYPointTranslator(GAME_HEIGHT - 230 - 80 - 80 - 80))
                                                        .add(new FlipXPointTranslator(GAME_WIDTH))
                                        )
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        createDoubleDroppers(
                                                Path.SPIRAL,
                                                PathSpeed.FAST,
                                                10,
                                                0f,
                                                1f,
                                                false
                                        ),
//                                        SubWavePathRule.DOUBLE_SPIRAL,
                                        alienConfig(
                                                AlienCharacter.FIGHTER,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)
                                )
                        )
                );
                break;

            /**
             * Wave 14
             * Easy maze of blocks descending from the top.
             */
            case 14:
                // easy maze of blocks
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        createMaze(
                                                mazeDoubler(MAZE_FOUR)),
                                        directionalAlienConfig(
                                                AlienCharacter.BLOCK,
                                                DOWNWARDS,
                                                AlienSpeed.VERY_FAST),
                                        NO_POWER_UPS)
                        )
                );
                break;

            /**
             * Wave 15
             * Single layer of bouncing aliens.
             * Wave of descending aliens in diamonds and rows.
             * Double layer of bouncing aliens.
             */
            case 15:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.BOUNCING_HIGHER,
                                        alienConfig(
                                                AlienCharacter.HOPPER,
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
                                                AlienCharacter.CHEEKY,
                                                AlienMissileSpeed.FAST,
                                                4.5f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createRowDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                3f),
                                        alienConfig(
                                                AlienCharacter.PINCER),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createDiamondDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.SLOW,
                                                4f),
                                        alienConfig(
                                                AlienCharacter.TINY_DANCER,
                                                AlienMissileSpeed.FAST,
                                                4.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_FAST)
                                )));
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.BOUNCING_REVERSE,
                                        alienConfig(
                                                AlienCharacter.HOPPER,
                                                AlienMissileSpeed.MEDIUM,
                                                5f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.BOUNCING_HIGHER,
                                        alienConfig(
                                                AlienCharacter.FOXY,
                                                AlienMissileSpeed.MEDIUM,
                                                5f),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)
                                )
                        )
                );
                break;

            /**
             * Wave 16
             * Insect mothership spawns (up to 5) hunter insects that attack in 3 vertical lanes but do not fire
             * Vertically descending rows of aliens
             * Insect mothership spawns (up to 5) hunter insects that attack in 3 vertical lanes and do fire
             */
            case 16:
                // insect mothership spawns hunter insects that attack in 3 vertical lanes but do not fire
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.NORMAL_ARC,
                                        spawningPathAlienConfig(
                                                AlienCharacter.INSECT_MOTHERSHIP,
                                                SpawningLimitedAlienConfig
                                                        .builder()
                                                        .limitedCharacter(AlienCharacter.INSECT)
                                                        .maximumActiveSpawnedAliens(5)
                                                        .minimumSpawnDelayTime(0.5f)
                                                        .spwanedPowerUpTypes(Arrays.asList(
                                                                PowerUpType.MISSILE_GUIDED,
                                                                PowerUpType.MISSILE_FAST,
                                                                PowerUpType.MISSILE_PARALLEL))
                                                        .maximumAdditionalRandomSpawnDelayTime(0.25f)
                                                        .spawnedAlienConfig(
                                                                hunterAlienConfig(
                                                                        AlienCharacter.INSECT,
                                                                        AlienSpeed.MEDIUM,
                                                                        BoundariesConfig
                                                                                .builder()
                                                                                .lanePolicy(BoundaryLanePolicy.VERTICAL)
                                                                                .lanes(3)
                                                                                .build()))
                                                        .build()),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createDescendingDelayedRowSubWave(
                                        Path.BOUNCE_DOWN_AND_UP,
                                        PathSpeed.NORMAL,
                                        new AlienRowConfig[]{
                                                alienRowConfig(
                                                        AlienCharacter.HOPPER,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        NO_POWER_UPS
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.CHEEKY,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        NO_POWER_UPS
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.FOXY,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        NO_POWER_UPS
                                                ),
                                        },
                                        6,
                                        0f,
                                        true,
                                        false
                                )
                        )
                );
                // insect mothership spawns hunter insects that attack in 3 vertical lanes and do fire
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.NORMAL_ARC,
                                        spawningPathAlienConfig(
                                                AlienCharacter.INSECT_MOTHERSHIP,
                                                SpawningLimitedAlienConfig
                                                        .builder()
                                                        .limitedCharacter(AlienCharacter.INSECT)
                                                        .maximumActiveSpawnedAliens(5)
                                                        .minimumSpawnDelayTime(0.5f)
                                                        .spwanedPowerUpTypes(Arrays.asList(
                                                                PowerUpType.MISSILE_GUIDED,
                                                                PowerUpType.MISSILE_FAST,
                                                                PowerUpType.MISSILE_PARALLEL))
                                                        .maximumAdditionalRandomSpawnDelayTime(0.25f)
                                                        .spawnedAlienConfig(
                                                                hunterAlienConfig(
                                                                        AlienCharacter.INSECT,
                                                                        AlienSpeed.MEDIUM,
                                                                        BoundariesConfig
                                                                                .builder()
                                                                                .lanePolicy(BoundaryLanePolicy.VERTICAL)
                                                                                .lanes(3)
                                                                                .build(),
                                                                        AlienMissileSpeed.MEDIUM,
                                                                        2.5f)
                                                        )
                                                        .build()),
                                        NO_POWER_UPS
                                )
                        )
                );
                break;

            /**
             * Wave 17
             * Valley dive attack interleaved aliens from top.
             * Mini asteroid field
             * Valley dive attack interleaved aliens from bottom.
             */
            case 17:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE,
                                        alienConfig(
                                                AlienCharacter.FIGHTER,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_INTERLEAVED,
                                        alienConfig(
                                                AlienCharacter.PAD,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.HELPER_BASES)
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
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_FLIPPED,
                                        alienConfig(
                                                AlienCharacter.FIGHTER,
                                                AlienMissileSpeed.MEDIUM,
                                                4.5f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_INTERLEAVED_FLIPPED,
                                        alienConfig(
                                                AlienCharacter.PAD,
                                                AlienMissileSpeed.MEDIUM,
                                                4.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_FAST)
                                )
                        )
                );
                break;

            /**
             * Wave 18
             * Base chased by two flying books. One book occupies left half or screen.
             * Other book occupies right half of screen.
             */
            case 18:
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.MIDDLE_TOP,
                                        hunterAlienConfig(
                                                AlienCharacter.BOOK,
                                                AlienSpeed.VERY_FAST,
                                                BoundariesConfig
                                                        .builder()
                                                        .maxX(SCREEN_MID_X)
                                                        .build()),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                ),
                                new SubWaveNoPathConfig(
                                        SubWaveRule.MIDDLE_BOTTOM,
                                        hunterAlienConfig(
                                                AlienCharacter.BOOK,
                                                AlienSpeed.VERY_FAST,
                                                BoundariesConfig
                                                        .builder()
                                                        .minX(SCREEN_MID_X)
                                                        .build()),
                                        Collections.singletonList(PowerUpType.SHIELD)
                                )
                        )
                );
                break;

            /**
             * Wave 19
             * Bell curve wave with aliens flying from left-to-right
             * Maze with aliens flying in formation and firing at top
             */
            case 19:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.BELL_CURVE,
                                        alienConfig(
                                                AlienCharacter.CHEEKY,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(
                                                PowerUpType.MISSILE_LASER)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                flatten(
                                        createLeftToRightDelayedRowSubWave(
                                                Path.LEFT_AND_RIGHT,
                                                PathSpeed.NORMAL,
                                                new AlienRowConfig[]{
                                                        alienRowConfig(
                                                                AlienCharacter.PINCER,
                                                                AlienMissileSpeed.FAST,
                                                                4f,
                                                                NO_POWER_UPS)
                                                },
                                                5,
                                                0.75f,
                                                true,
                                                new PointTranslatorChain()
                                                        .add(new OffsetYPointTranslator(GAME_HEIGHT - 230))
                                        ),
                                        createBarrierMaze(
                                                24,
                                                AlienSpeed.VERY_FAST,
                                                1.5f)
                                )
                        )
                );
                break;

            /**
             * Wave 20
             * Figure of eight formation attack.
             * Alien spirals down centre of screen. Spawns multiple aliens that travel
             * diagonally across screen (left-down and right-down) while firing.
             */
            case 20:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.FIGURE_OF_EIGHT_SHORT,
                                        alienConfig(
                                                AlienCharacter.FOXY,
                                                AlienMissileSpeed.MEDIUM,
                                                8f),
                                        NO_POWER_UPS
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        createCentralDroppers(
                                                Path.SPIRAL,
                                                PathSpeed.NORMAL,
                                                1,
                                                0f,
                                                0f,
                                                true
                                        ),
                                        spawningPathAlienConfig(
                                                AlienCharacter.SAUCER,
                                                SpawningAlienConfig
                                                        .builder()
                                                        .maximumAdditionalRandomSpawnDelayTime(0f)
                                                        .minimumSpawnDelayTime(3f)
                                                        .spwanedPowerUpTypes(Arrays.asList(
                                                                PowerUpType.MISSILE_GUIDED,
                                                                PowerUpType.SHIELD))
                                                        .spawnedAlienConfig(
                                                                SplitterConfig
                                                                        .builder()
                                                                        .alienConfigs(
                                                                                Arrays.asList(
                                                                                        (AlienConfig) directionalAlienConfig(
                                                                                                AlienCharacter.HOPPER,
                                                                                                -HALF_PI - QUARTER_PI,
                                                                                                AlienSpeed.MEDIUM,
                                                                                                AlienMissileSpeed.FAST,
                                                                                                2.5f),
                                                                                        (AlienConfig) directionalAlienConfig(
                                                                                                AlienCharacter.TINY_DANCER,
                                                                                                -HALF_PI + QUARTER_PI,
                                                                                                AlienSpeed.MEDIUM,
                                                                                                AlienMissileSpeed.FAST,
                                                                                                2.5f)
                                                                                )
                                                                        )
                                                                        .build())

                                                        .build()),
                                        NO_POWER_UPS
                                )
                        )
                );
                break;

            /**
             * Wave 21
             * Descending aliens staggered in 2 columns.
             * Fast firing alien protected in a box of defender aliens.
             */
            case 21:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        createDoubleStaggeredDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                4,
                                                0),
                                        alienConfig(
                                                AlienCharacter.CHEEKY,
                                                AlienMissileSpeed.FAST,
                                                2f),
                                        NO_POWER_UPS
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        createRowDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                0f
                                        ),
                                        alienConfig(
                                                AlienCharacter.CHEEKY),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createBoxDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                0.5f
                                        ),
                                        alienConfig(
                                                AlienCharacter.PINCER),
                                        Collections.singletonList(
                                                PowerUpType.MISSILE_SPRAY)
                                ),
                                new SubWavePathConfig(
                                        createCentralDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                1,
                                                1f
                                        ),
                                        alienConfig(
                                                AlienCharacter.FOXY,
                                                AlienMissileSpeed.VERY_FAST,
                                                0.5f),
                                        Collections.singletonList(
                                                PowerUpType.MISSILE_LASER)
                                ),
                                new SubWavePathConfig(
                                        createRowDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                2f
                                        ),
                                        alienConfig(
                                                AlienCharacter.CHEEKY),
                                        NO_POWER_UPS
                                )
                        )
                );
                break;

            /**
             * Wave 22
             * 2 alien travels down centre of screen spawing multiple aliens that travel diagonally
             * across screen (left-down and right-down) while firing.
             * 2 alien travel in row down screen spawing multiple aliens that travel diagonally
             * across screen (left-down and right-down).
             */
            case 22:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        createCentralDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                2,
                                                0f,
                                                1.75f,
                                                true
                                        ),
                                        spawningPathAlienConfig(
                                                AlienCharacter.SAUCER,
                                                SpawningAlienConfig
                                                        .builder()
                                                        .maximumAdditionalRandomSpawnDelayTime(0f)
                                                        .minimumSpawnDelayTime(3f)
                                                        .spwanedPowerUpTypes(Arrays.asList(
                                                                PowerUpType.MISSILE_GUIDED,
                                                                PowerUpType.SHIELD))
                                                        .spawnedAlienConfig(
                                                                SplitterConfig
                                                                        .builder()
                                                                        .alienConfigs(
                                                                                Arrays.asList(
                                                                                        (AlienConfig) directionalAlienConfig(
                                                                                                AlienCharacter.HOPPER,
                                                                                                -HALF_PI - QUARTER_PI,
                                                                                                AlienSpeed.MEDIUM),
                                                                                        (AlienConfig) directionalAlienConfig(
                                                                                                AlienCharacter.TINY_DANCER,
                                                                                                -HALF_PI + QUARTER_PI,
                                                                                                AlienSpeed.MEDIUM)
                                                                                )
                                                                        )
                                                                        .build())

                                                        .build()),
                                        NO_POWER_UPS
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        createDoubleDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                1,
                                                0f,
                                                1.75f,
                                                true
                                        ),
                                        spawningPathAlienConfig(
                                                AlienCharacter.SAUCER,
                                                SpawningAlienConfig
                                                        .builder()
                                                        .maximumAdditionalRandomSpawnDelayTime(0f)
                                                        .minimumSpawnDelayTime(3f)
                                                        .spwanedPowerUpTypes(Arrays.asList(
                                                                PowerUpType.MISSILE_GUIDED,
                                                                PowerUpType.SHIELD))
                                                        .spawnedAlienConfig(
                                                                SplitterConfig
                                                                        .builder()
                                                                        .alienConfigs(
                                                                                Arrays.asList(
                                                                                        (AlienConfig) directionalAlienConfig(
                                                                                                AlienCharacter.HOPPER,
                                                                                                -HALF_PI - QUARTER_PI,
                                                                                                AlienSpeed.MEDIUM,
                                                                                                AlienMissileSpeed.FAST,
                                                                                                4f),
                                                                                        (AlienConfig) directionalAlienConfig(
                                                                                                AlienCharacter.TINY_DANCER,
                                                                                                -HALF_PI + QUARTER_PI,
                                                                                                AlienSpeed.MEDIUM,
                                                                                                AlienMissileSpeed.FAST,
                                                                                                4f)
                                                                                )
                                                                        )
                                                                        .build())

                                                        .build()),
                                        NO_POWER_UPS
                                )
                        )
                );
                break;

            /**
             * Wave 23
             * slow looper attack from both sides firing directional missiles.
             * fast bell curve attack.
             * fast curving attack from top-to-bottom with aliens on both sides
             */
            case 23:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.LOOPER_ATTACK,
                                        alienConfig(
                                                AlienCharacter.FIGHTER,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        NO_POWER_UPS),
                                new SubWavePathConfig(
                                        SubWavePathRule.LOOPER_ATTACK_REVERSE,
                                        alienConfig(
                                                AlienCharacter.FIGHTER,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.HELPER_BASES))));
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.BELL_CURVE,
                                        alienConfig(
                                                AlienCharacter.HOPPER,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER))));
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE_BENDING_RIGHT,
                                        alienConfig(
                                                AlienCharacter.TINY_DANCER,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        NO_POWER_UPS),
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE_BENDING_LEFT,
                                        alienConfig(
                                                AlienCharacter.TINY_DANCER,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        NO_POWER_UPS)));
                break;

            /**
             * Wave 24
             * Hunter dragon chases base and lays eggs that spawn baby dragons.
             * A maximum of two baby dragons can be spawned at one time.
             */
            case 24:
                // Long-tailed hunter dragon
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.RANDOM_TOP,
                                        followableHunterConfigBuilder(
                                                AlienCharacter.DRAGON_HEAD,
                                                AlienSpeed.FAST,
                                                AlienMissileSpeed.FAST,
                                                4f,
                                                BoundariesConfig
                                                        .builder()
                                                        .build(),
                                                5,
                                                followerConfigBuilder(
                                                        AlienCharacter.DRAGON_BODY,
                                                        AlienSpeed.FAST
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
                                                .spawnConfig(
                                                        SpawningLimitedActiveAlienConfig
                                                                .builder()
                                                                .maximumActiveSpawnedAliens(2)
                                                                // eggs will spawn into dragons so we limit on number of active dragons
                                                                .limitedCharacter(AlienCharacter.BABY_DRAGON_HEAD)
                                                                .minimumSpawnDelayTime(3f)
                                                                .maximumAdditionalRandomSpawnDelayTime(2f)
                                                                .spwanedPowerUpTypes(
                                                                        Arrays.asList(
                                                                                PowerUpType.MISSILE_GUIDED,
                                                                                PowerUpType.MISSILE_PARALLEL,
                                                                                PowerUpType.HELPER_BASES))
                                                                .spawnedAlienConfig(
                                                                        spawningStaticAlienConfig(
                                                                                AlienCharacter.EGG,
                                                                                SpawningAndExplodingAlienConfig
                                                                                        .builder()
                                                                                        .spawnedPowerUpType(PowerUpType.MISSILE_FAST)
                                                                                        .spawnDelayTime(2.25f)  // aligns to egg cracking animation 9 x 0.25f
                                                                                        .spawnedAlienConfig(
                                                                                                followableHunterConfigBuilder(
                                                                                                        AlienCharacter.BABY_DRAGON_HEAD,
                                                                                                        AlienSpeed.VERY_FAST,
                                                                                                        BoundariesConfig
                                                                                                                .builder()
                                                                                                                .lanePolicy(BoundaryLanePolicy.VERTICAL)
                                                                                                                .lanes(2)
                                                                                                                .build(),
                                                                                                        5,
                                                                                                        followerConfigBuilder(
                                                                                                                AlienCharacter.BABY_DRAGON_BODY,
                                                                                                                AlienSpeed.VERY_FAST
                                                                                                        )
                                                                                                                .build(),
                                                                                                        NO_POWER_UPS)
                                                                                                        .build())
                                                                                        .build()))
                                                                .build()
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

            case 25:
                // diamond and row formations of aliens descend from the top of the screen
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        createDiamondDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                0f),
                                        alienConfig(
                                                AlienCharacter.CONFUSER,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createRowDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                3f),
                                        alienConfig(
                                                AlienCharacter.JUMPER),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY)
                                ),
                                new SubWavePathConfig(
                                        createDiamondDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                5f),
                                        alienConfig(
                                                AlienCharacter.CHARLIE,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createRowDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                8f),
                                        alienConfig(
                                                AlienCharacter.CONFUSER),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createDiamondDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                10f),
                                        alienConfig(
                                                AlienCharacter.JUMPER,
                                                AlienMissileSpeed.MEDIUM,
                                                6.5f),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        createRowDroppers(
                                                Path.STRAIGHT_DOWN,
                                                PathSpeed.NORMAL,
                                                13f),
                                        alienConfig(
                                                AlienCharacter.CHARLIE),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                createDescendingDelayedRowSubWave(
                                        Path.BOUNCE_DOWN_AND_UP,
                                        PathSpeed.NORMAL,
                                        new AlienRowConfig[]{
                                                alienRowConfig(
                                                        AlienCharacter.CHARLIE,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        NO_POWER_UPS
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.ALL_SEEING_EYE,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        NO_POWER_UPS
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.PISTON,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        NO_POWER_UPS
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.JUMPER,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        NO_POWER_UPS
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.ROTATOR,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        NO_POWER_UPS
                                                ),
                                                alienRowConfig(
                                                        AlienCharacter.WHIRLPOOL,
                                                        AlienMissileSpeed.FAST,
                                                        15f,
                                                        NO_POWER_UPS
                                                ),
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
                                new SubWaveNoPathConfig(
                                        createMaze(
                                                MAZE_ONE),
                                        directionalAlienConfig(
                                                AlienCharacter.BLOCK,
                                                DOWNWARDS,
                                                AlienSpeed.MEDIUM),
                                        NO_POWER_UPS)
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                    createMaze(
                                            MAZE_TWO),
                                    directionalAlienConfig(
                                            AlienCharacter.BLOCK,
                                            DOWNWARDS,
                                            AlienSpeed.VERY_FAST),
                                        NO_POWER_UPS)
                        )
                );
                break;

            case 26:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        createMaze(
                                                MAZE_ONE),
                                        directionalAlienConfig(
                                                AlienCharacter.BLOCK,
                                                DOWNWARDS,
                                                AlienSpeed.MEDIUM),
                                        NO_POWER_UPS)
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

            case 27:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.NORMAL_ARC,
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
                                        SubWavePathRule.NORMAL_ARC,
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
                                        new SubWaveConfig[]{
                                                new SubWaveNoPathConfig(
                                                        createMaze(
                                                                MAZE_THREE),
                                                        fallingSpinningSplittingConfig(
                                                                AlienCharacter.ASTEROID,
                                                                AlienSpeed.MEDIUM,
                                                                AlienCharacter.ASTEROID_MINI),
                                                        NO_POWER_UPS)
                                        }
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

            case 33:

                /**
                 * Maze to navigate through.
                 * First maze is empty.
                 * Second maze has alien guards blocking the gaps.
                 * Moved from old wave 21
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
                /* end */
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

                /**
                 * Alien drops from top to bottom spawning aliens that move diagnonally
                 * down the screen while firing missiles targeted at the base.
                 */
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        createCentralDroppers(
                                                Path.SPIRAL,
                                                PathSpeed.FAST,
                                                1,
                                                0f,
                                                0f,
                                                false
                                        ),
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
                                        SubWavePathRule.NORMAL_ARC,
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
                                        SubWavePathRule.NORMAL_ARC,
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
