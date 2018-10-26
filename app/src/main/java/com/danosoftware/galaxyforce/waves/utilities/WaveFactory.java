package com.danosoftware.galaxyforce.waves.utilities;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.game.handlers.GameHandler;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.utilities.Reversed;
import com.danosoftware.galaxyforce.utilities.WaveUtilities;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.SubWave;
import com.danosoftware.galaxyforce.waves.config.SubWaveConfig;
import com.danosoftware.galaxyforce.waves.config.SubWaveNoPathConfig;
import com.danosoftware.galaxyforce.waves.config.SubWavePathConfig;
import com.danosoftware.galaxyforce.waves.config.SubWaveRepeatMode;
import com.danosoftware.galaxyforce.waves.rules.SubWavePathRule;
import com.danosoftware.galaxyforce.waves.rules.SubWaveRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.danosoftware.galaxyforce.waves.utilities.WaveCreationUtils.createNoPathAlienSubWave;
import static com.danosoftware.galaxyforce.waves.utilities.WaveCreationUtils.createPathAlienSubWave;

/**
 * Creates a wave of aliens based on the provided wave number. Each wave
 * property can contain multiple sub-waves, each consisting of a number of
 * aliens following a path
 */
public class WaveFactory {
    private final GameHandler model;

    public WaveFactory(GameHandler model) {
        this.model = model;
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

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_TRIANGULAR,
                                        AlienType.OCTOPUS,
                                        Arrays.asList(PowerUpType.MISSILE_BLAST, PowerUpType.LIFE)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE,
                                        AlienType.OCTOPUS,
                                        Arrays.asList(PowerUpType.ENERGY)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_INTERLEAVED,
                                        AlienType.MINION,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.OCTOPUS,
                                        Arrays.asList(PowerUpType.ENERGY)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_INTERLEAVED,
                                        AlienType.MINION,
                                        Arrays.asList(PowerUpType.ENERGY)
                                )
                        )
                );
                break;

            case 3:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE,
                                        AlienType.OCTOPUS,
                                        Arrays.asList(PowerUpType.ENERGY)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.VALLEY_DIVE_INTERLEAVED,
                                        AlienType.MINION,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.OCTOPUS,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.MOTHERSHIP,
                                        Arrays.asList(PowerUpType.ENERGY)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVE_MOTHERSHIP,
                                        AlienType.MOTHERSHIP,
                                        Arrays.asList(PowerUpType.ENERGY)
                                )
                        )
                );
                break;

            case 6:

                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.WAVEY_LINE,
                                        AlienType.OCTOPUS,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.ASTEROID,
                                        Arrays.asList(PowerUpType.ENERGY)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROIDS_REVERSE,
                                        AlienType.ASTEROID,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.GOBBY,
                                        Arrays.asList(PowerUpType.MISSILE_BLAST, PowerUpType.LIFE)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.SPACE_INVADER_REVERSE,
                                        AlienType.GOBBY,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.OCTOPUS,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.DRAGON,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.OCTOPUS,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.OCTOPUS,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.OCTOPUS,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.STORK,
                                        Arrays.asList(PowerUpType.ENERGY)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.DOUBLE_BELL_CURVE,
                                        AlienType.STORK,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.DROID,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.INSECT,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.ASTEROID_SIMPLE,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.DROID,
                                        Arrays.asList(PowerUpType.ENERGY)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWavePathConfig(
                                        SubWavePathRule.DOUBLE_SPIRAL,
                                        AlienType.DROID,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.ASTEROID_SIMPLE,
                                        Arrays.asList(PowerUpType.ENERGY)
                                )
                        )
                );
                subWaves.add(
                        createSubWave(
                                SubWaveRepeatMode.REPEAT_UNTIL_DESTROYED,
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROID_MAZE,
                                        AlienType.ASTEROID_SIMPLE,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.HUNTER,
                                        Arrays.asList(PowerUpType.ENERGY)
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
                                        AlienType.STORK,
                                        Arrays.asList(PowerUpType.ENERGY)
                                ),
                                new SubWaveNoPathConfig(
                                        SubWaveRule.ASTEROID_FIELD,
                                        AlienType.ASTEROID_SIMPLE,
                                        Arrays.asList(PowerUpType.ENERGY)
                                ),
                                new SubWavePathConfig(
                                        SubWavePathRule.LOOPER_ATTACK,
                                        AlienType.DROID,
                                        Arrays.asList(PowerUpType.ENERGY)
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
     *
     * @param repeatedMode
     * @param subWaveConfigs
     * @return list of alien sprites
     */
    private SubWave createSubWave(
            final SubWaveRepeatMode repeatedMode,
            final SubWaveConfig... subWaveConfigs) {

        List<IAlien> aliens = new ArrayList<>();

        for (SubWaveConfig config : subWaveConfigs) {

            switch (config.getType()) {
                case PATH:
                    SubWavePathConfig pathConfig = (SubWavePathConfig) config;
                    aliens.addAll(createPathAlienSubWave(pathConfig, model));
                    break;
                case NO_PATH:
                    SubWaveNoPathConfig noPathConfig = (SubWaveNoPathConfig) config;
                    aliens.addAll(createNoPathAlienSubWave(noPathConfig, model));
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
        SubWave subWave = new SubWave(reversedAlienList, repeatedMode);

        return subWave;

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
