package com.danosoftware.galaxyforce.waves.rules;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.enumerations.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Each sub-wave consists of one or more sub-wave properties.
 *
 * Each sub-wave property contains enough data to create a sub-wave
 * of aliens that follow some rules.
 */
public enum SubWaveRule
{

    /**
     * Asteroids that fall from top to bottom at random x positions and random
     * speeds
     */
    ASTEROIDS(
            new SubWaveRuleProperties(
                    true,
                    false,
                    0,
                    GameConstants.SCREEN_TOP,
                    30,
                    0.5f,
                    0f,
                    false,
                    Direction.DOWN
            )),

    /**
     * Asteroids that fall from bottom to top at random x positions and random
     * speeds
     */
    ASTEROIDS_REVERSE(
            new SubWaveRuleProperties(
                    true,
                    false,
                    0,
                    GameConstants.SCREEN_BOTTOM,
                    30,
                    0.5f,
                    0f,
                    false,
                    Direction.UP
            )),

    /**
     * Asteroids that form a path through an asteroid field.
     */
    ASTEROID_FIELD(asteroidFieldSubWave());

    // list of properties for a sub-wave
    private List<SubWaveRuleProperties> waveList;

    SubWaveRule(SubWaveRuleProperties... waveArray)
    {
        this.waveList = Arrays.asList(waveArray);
    }

    SubWaveRule(List<SubWaveRuleProperties> waveList)
    {
        this.waveList = waveList;
    }

    /**
     * Properties to create a sub-wave
     *
     * @return
     */
    public List<SubWaveRuleProperties> subWaveProps()
    {
        return waveList;
    }

    /*
     * ******************************************
     * HELPER METHODS TO BUILD SPECIFIC SUB-WAVES
     * ******************************************
     */

    private static List<SubWaveRuleProperties> asteroidFieldSubWave()
    {
        List<SubWaveRuleProperties> subWaves = new ArrayList<>();

        // distance between asteroids on same row. 6 asteroids
        // per row so 5 gaps between asteroids.
        int distBetweenAsteroid = (GameConstants.SCREEN_RIGHT_EDGE - GameConstants.SCREEN_LEFT_EDGE) / 5;

        for (int row = 0; row < 10; row++)
        {
            for (int col = 0; col < 6; col++)
            {
                subWaves.add(
                        new SubWaveRuleProperties(
                                false,
                                false,
                                GameConstants.SCREEN_LEFT_EDGE + (col * distBetweenAsteroid),
                                GameConstants.SCREEN_TOP,
                                1,
                                0,
                                row * 2,
                                false,
                                Direction.DOWN
                        ));
            }
        }

        return subWaves;
    }
}
