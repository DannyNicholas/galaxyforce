package com.danosoftware.galaxyforce.waves.utilities;

import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.utilities.Reversed;
import com.danosoftware.galaxyforce.utilities.WaveUtilities;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.SubWave;
import com.danosoftware.galaxyforce.waves.config.AlienWithMissileConfig;
import com.danosoftware.galaxyforce.waves.config.AlienWithoutMissileConfig;
import com.danosoftware.galaxyforce.waves.config.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.SubWaveConfig;
import com.danosoftware.galaxyforce.waves.config.SubWaveNoPathConfig;
import com.danosoftware.galaxyforce.waves.config.SubWavePathConfig;
import com.danosoftware.galaxyforce.waves.config.SubWaveRepeatMode;
import com.danosoftware.galaxyforce.waves.rules.SubWavePathRule;
import com.danosoftware.galaxyforce.waves.rules.SubWaveRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Creates a wave of aliens based on the provided wave number. Each wave
 * property can contain multiple sub-waves, each consisting of a number of
 * aliens following a path
 */
public class WaveFactory {

    private static final List<PowerUpType> NO_POWER_UPS = Collections.emptyList();

    private final WaveCreationUtils creationUtils;


    public WaveFactory(
            WaveCreationUtils creationUtils) {
        this.creationUtils = creationUtils;
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

        List<SubWave> subWaves = new ArrayList<>();

        switch (wave) {

            case 1:

                // triangular attack from left-to-right
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_TRIANGULAR,
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_BLAST)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_TRIANGULAR_REVERSED,
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_INTERLEAVED,
                                        new AlienWithMissileConfig(
                                                AlienType.MINION,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_FAST)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_FLIPPED,
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
                                        NO_POWER_UPS
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_INTERLEAVED_FLIPPED,
                                        new AlienWithMissileConfig(
                                                AlienType.MINION,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
                                        NO_POWER_UPS
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE_REVERSE,
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
                                        NO_POWER_UPS
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE_DOUBLE,
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)

                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SPIRAL,
                                        new AlienWithMissileConfig(
                                                AlienType.MINION,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
                                        NO_POWER_UPS
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.DOUBLE_SPIRAL,
                                        new AlienWithMissileConfig(
                                                AlienType.MINION,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithoutMissileConfig(
                                                AlienType.MOTHERSHIP,
                                                10
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_SPRAY)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_MOTHERSHIP,
                                        new AlienWithoutMissileConfig(
                                                AlienType.MOTHERSHIP,
                                                10
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithoutMissileConfig(
                                                AlienType.ASTEROID,
                                                5
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.GOBBY,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
                                        Arrays.asList(PowerUpType.MISSILE_BLAST, PowerUpType.LIFE)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SPACE_INVADER_REVERSE,
                                        new AlienWithMissileConfig(
                                                AlienType.GOBBY,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.DRAGON,
                                                20,
                                                new MissileConfig(
                                                        AlienMissileType.ROTATED,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.OCTOPUS,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.STORK,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.DOUBLE_BELL_CURVE,
                                        new AlienWithMissileConfig(
                                                AlienType.STORK,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.DROID,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.INSECT,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithoutMissileConfig(
                                                AlienType.ASTEROID_SIMPLE,
                                                5
                                        ),
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
                                        new AlienWithMissileConfig(
                                                AlienType.DROID,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_PARALLEL)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.DOUBLE_SPIRAL,
                                        new AlienWithMissileConfig(
                                                AlienType.DROID,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
                                        new AlienWithoutMissileConfig(
                                                AlienType.ASTEROID_SIMPLE,
                                                5
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROID_MAZE,
                                        new AlienWithoutMissileConfig(
                                                AlienType.ASTEROID_SIMPLE,
                                                5
                                        ),
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
                                        SubWaveRule.HUNTER_PAIR,
                                        new AlienWithoutMissileConfig(
                                                AlienType.HUNTER,
                                                10
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_GUIDED)
                                )
                        )
                );
                break;

            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
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
                                        new AlienWithMissileConfig(
                                                AlienType.STORK,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
                                        Collections.singletonList(PowerUpType.HELPER_BASES)
                                ),
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROID_FIELD,
                                        new AlienWithoutMissileConfig(
                                                AlienType.ASTEROID_SIMPLE,
                                                5
                                        ),
                                        Collections.singletonList(PowerUpType.MISSILE_LASER)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.LOOPER_ATTACK,
                                        new AlienWithMissileConfig(
                                                AlienType.DROID,
                                                1,
                                                new MissileConfig(
                                                        AlienMissileType.SIMPLE,
                                                        AlienMissileSpeed.MEDIUM,
                                                        6.5f
                                                )
                                        ),
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
