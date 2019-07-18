package com.danosoftware.galaxyforce.waves.rules;

import com.danosoftware.galaxyforce.constants.GameConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Each sub-wave consists of one or more sub-wave properties.
 * <p>
 * Each sub-wave property contains enough data to create a sub-wave
 * of aliens that follow some rules.
 */
public enum SubWaveRule {

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
                    false
            )),

    /**
     * Asteroids that form a path through an asteroid field.
     */
    ASTEROID_FIELD(asteroidFieldSubWave(10, 2)),

    /**
     * Asteroids maze that forms a random path through an asteroid field.
     */
    ASTEROID_MAZE(asteroidMazeSubWave(15, 2)),

    /**
     * Asteroids maze that forms a random path through an asteroid field with large gaps between rows.
     */
    ASTEROID_MAZE_EASY(asteroidMazeSubWave(15, 3)),

    /**
     * Hunters that start at different positions and chase the base.
     */
    HUNTER_TOP(
            new SubWaveRuleProperties(
                    false,
                    false,
                    GameConstants.SCREEN_MID_X,
                    GameConstants.SCREEN_TOP,
                    1,
                    0.5f,
                    0f,
                    false
            )),
    HUNTER_BOTTOM(
            new SubWaveRuleProperties(
                    false,
                    false,
                    GameConstants.SCREEN_MID_X,
                    GameConstants.SCREEN_BOTTOM,
                    1,
                    0f,
                    0f,
                    false
            )),

    /**
     * Dragon that chases the base.
     */
    DRAGON_CHASE(
            new SubWaveRuleProperties(
                    true,
                    false,
                    0,
                    GameConstants.SCREEN_TOP,
                    1,
                    0f,
                    0f,
                    false
            )
    );


    // list of properties for a sub-wave
    private final List<SubWaveRuleProperties> waveList;

    SubWaveRule(SubWaveRuleProperties... waveArray) {
        this.waveList = Arrays.asList(waveArray);
    }

    SubWaveRule(List<SubWaveRuleProperties> waveList) {
        this.waveList = waveList;
    }

    /**
     * Properties to create a sub-wave
     */
    public List<SubWaveRuleProperties> subWaveProps() {
        return waveList;
    }

    /*
     * ******************************************
     * HELPER METHODS TO BUILD SPECIFIC SUB-WAVES
     * ******************************************
     */

    // number of asteroids on a row
    private static final int ASTEROIDS_PER_ROW = 6;

    // distance between asteroids on same row
    private static final int DISTANCE_BETWEEN_ASTEROIDS = (GameConstants.SCREEN_RIGHT_EDGE - GameConstants.SCREEN_LEFT_EDGE) / (ASTEROIDS_PER_ROW - 1);

    /**
     * Creates an asteroid field
     */
    private static List<SubWaveRuleProperties> asteroidFieldSubWave(int totalRows, int delayBetweenRows) {
        List<SubWaveRuleProperties> subWaves = new ArrayList<>();

        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < ASTEROIDS_PER_ROW; col++) {
                subWaves.add(createAsteroid(row, col, delayBetweenRows));
            }
        }

        return subWaves;
    }

    /**
     * Creates an asteroid maze where a gap exists in each row
     */
    private static List<SubWaveRuleProperties> asteroidMazeSubWave(int totalRows, int delayBetweenRows) {
        List<SubWaveRuleProperties> subWaves = new ArrayList<>();

        // random gap generator
        final Random rand = new Random();


        for (int row = 0; row < totalRows; row++) {

            int gap = rand.nextInt(ASTEROIDS_PER_ROW);

            for (int col = 0; col < 6; col++) {

                // add asteroid if this is not a gap
                if (col != gap) {
                    subWaves.add(createAsteroid(row, col, delayBetweenRows));
                }
            }
        }

        return subWaves;
    }

    /**
     * Create an asteroid at the wanted position
     */
    private static SubWaveRuleProperties createAsteroid(int row, int col, int delayBetweenRows) {
        return new SubWaveRuleProperties(
                false,
                false,
                GameConstants.SCREEN_LEFT_EDGE + (col * DISTANCE_BETWEEN_ASTEROIDS),
                GameConstants.SCREEN_TOP,
                1,
                0,
                row * delayBetweenRows,
                false
        );
    }
}
