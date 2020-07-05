package com.danosoftware.galaxyforce.waves.rules;

import com.danosoftware.galaxyforce.constants.GameConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;
import static com.danosoftware.galaxyforce.waves.utilities.WaveFactoryHelper.createAlienSubWaveProperty;

/**
 * Each sub-wave consists of one or more sub-wave properties.
 * <p>
 * Each sub-wave property contains enough data to create a sub-wave
 * of aliens that follow some rules.
 */
public enum SubWaveRule {

    RANDOM_TOP(
            // top
            new SubWaveRuleProperties(
                    true,
                    false,
                    0,
                    GameConstants.SCREEN_TOP,
                    1,
                    0.5f,
                    new Random().nextFloat() * 2f,
                    false
            )),

    RANDOM_BOTTOM(
            // bottom
            new SubWaveRuleProperties(
                    true,
                    false,
                    0,
                    GameConstants.SCREEN_BOTTOM,
                    1,
                    0.5f,
                    new Random().nextFloat() * 2f,
                    false
            )),

    RANDOM_LEFT(
            // left
            new SubWaveRuleProperties(
                    false,
                    true,
                    GameConstants.SCREEN_LEFT,
                    0,
                    1,
                    0.5f,
                    new Random().nextFloat() * 2f,
                    false
            )),

    RANDOM_RIGHT(
            // right
            new SubWaveRuleProperties(
                    false,
                    true,
                    GameConstants.SCREEN_RIGHT,
                    0,
                    1,
                    0.5f,
                    new Random().nextFloat() * 2f,
                    false
            )),

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
    ASTEROID_FIELD(asteroidHorizontalSubWave(10, 2)),

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
                    0f,
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
    HUNTER_LEFT(
            new SubWaveRuleProperties(
                    false,
                    false,
                    GameConstants.SCREEN_LEFT,
                    GameConstants.SCREEN_MID_Y,
                    1,
                    0f,
                    0f,
                    false
            )),
    HUNTER_RIGHT(
            new SubWaveRuleProperties(
                    false,
                    false,
                    GameConstants.SCREEN_RIGHT,
                    GameConstants.SCREEN_MID_Y,
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

    // asteroid options
    private static final int ASTEROIDS_PER_ROW = 6;
    private static final int ASTEROIDS_SPRITE_WIDTH = 64;

    private static List<SubWaveRuleProperties> asteroidHorizontalSubWave(
            int totalRows,
            int delayBetweenRows) {
        return horizontalSubWave(
                totalRows,
                delayBetweenRows,
                ASTEROIDS_PER_ROW,
                ASTEROIDS_SPRITE_WIDTH);
    }

    /**
     * Creates subwave of multiple rows.
     * Each row containing multiple aliens.
     */
    private static List<SubWaveRuleProperties> horizontalSubWave(
            int totalRows,
            int delayBetweenRows,
            int numberOfColumns,
            int alienWidth) {

        List<SubWaveRuleProperties> subWaves = new ArrayList<>();

        // calculates min/max x positions for aliens per row
        final int minX = alienWidth / 2;
        final int maxX = GAME_WIDTH - (alienWidth / 2);

        final int distanceBetweenAliens = (maxX - minX) / (numberOfColumns - 1);

        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                final int xPos = minX + (col * distanceBetweenAliens);
                subWaves.add(createAlienSubWaveProperty(row, xPos, delayBetweenRows));
            }
        }
        return subWaves;
    }
}
