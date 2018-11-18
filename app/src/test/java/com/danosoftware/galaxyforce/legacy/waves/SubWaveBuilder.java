package com.danosoftware.galaxyforce.legacy.waves;

import com.danosoftware.galaxyforce.legacy.flightpath.Path;
import com.danosoftware.galaxyforce.legacy.flightpath.PathFactory;
import com.danosoftware.galaxyforce.waves.AlienType;

import java.util.Arrays;
import java.util.List;

public enum SubWaveBuilder {

    /*
     * Each sub-wave consists of one or more sub-wave properties. Each sub-wave
     * property has sprites, paths, x-invert, y-invert, x-Offset, y-Offset,
     * number of aliens, delay between each alien, delay offset and restart
     * immediately
     */

    WAVE_01(true,
            new SubWaveProperty(
                    AlienType.OCTOPUS,
                    Path.VALLEY_DROP,
                    PathFactory.X_INVERT_FALSE,
                    PathFactory.Y_INVERT_FALSE,
                    0,
                    0,
                    10,
                    0.5f,
                    0,
                    false
            ),
            new SubWaveProperty(
                    AlienType.OCTOPUS,
                    Path.VALLEY_DROP,
                    PathFactory.X_INVERT_TRUE,
                    PathFactory.Y_INVERT_FALSE,
                    0,
                    100,
                    10,
                    0.5f,
                    0,
                    false)),

    /**
     * space invader style attack
     */
    SPACE_INVADER(true, new SubWaveProperty(AlienType.GOBBY, Path.SPACE_INVADER, PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE,
            0, 0, 20, 0.3f, 0, false)),

    /**
     * space invader attack in reverse (i.e. from bottom to top)
     */
    SPACE_INVADER_REVERSE(true, new SubWaveProperty(AlienType.GOBBY, Path.SPACE_INVADER, PathFactory.X_INVERT_TRUE,
            PathFactory.Y_INVERT_TRUE, 0, 0, 20, 0.3f, 0, false)),

    /**
     * figure of eight path
     */
    FIGURE_OF_EIGHT(true, new SubWaveProperty(AlienType.OCTOPUS, Path.FIGURE_OF_EIGHT, PathFactory.X_INVERT_FALSE,
            PathFactory.Y_INVERT_FALSE, 0, 0, 20, 0.3f, 0, false)),

    /**
     * bell curve attack
     */
    BELL_CURVE(true, new SubWaveProperty(AlienType.STORK, Path.BELL_CURVE, PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE, 0, 0,
            20, 0.3f, 0, false)),

    /**
     * bell curve attack from top and bottom
     */
    DOUBLE_BELL_CURVE(true, new SubWaveProperty(AlienType.STORK, Path.BELL_CURVE, PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE,
            0, 0, 20, 0.3f, 0, false), new SubWaveProperty(AlienType.STORK, Path.BELL_CURVE, PathFactory.X_INVERT_FALSE,
            PathFactory.Y_INVERT_TRUE, 0, 0, 20, 0.3f, 0, false)),

    /**
     * twisted loop attack
     */
    LOOPER_ATTACK(true, new SubWaveProperty(AlienType.DROID, Path.LOOPER, PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE, 0, 0, 20,
            0.3f, 0, false)),

    /**
     * multiple tear drop attacks
     */
    TEAR_DROP_ATTACK(true, new SubWaveProperty(AlienType.INSECT, Path.TEAR_DROP, PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE,
            -200, 0, 20, 0.3f, 0, false), new SubWaveProperty(AlienType.INSECT, Path.TEAR_DROP, PathFactory.X_INVERT_FALSE,
            PathFactory.Y_INVERT_FALSE, 0, 0, 20, 0.3f, 0, false), new SubWaveProperty(AlienType.INSECT, Path.TEAR_DROP,
            PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE, 200, 0, 20, 0.3f, 0, false)),

    /**
     * crossing aliens that attack from the top, cross over then leave at the
     * bottom
     */
    CROSSING_STEP_ATTACK(true, new SubWaveProperty(AlienType.OCTOPUS, Path.BEZIER_STEP_UP, PathFactory.X_INVERT_FALSE,
            PathFactory.Y_INVERT_FALSE, 0, 0, 20, 0.3f, 0, false), new SubWaveProperty(AlienType.OCTOPUS, Path.BEZIER_STEP_UP,
            PathFactory.X_INVERT_TRUE, PathFactory.Y_INVERT_FALSE, 0, 0, 20, 0.3f, 0, false)),

    /**
     * crossover attack where aliens drop from top, pause and then exit on
     * opposite side of screen
     */
    CROSSOVER_EXIT_ATTACK(true, new SubWaveProperty(AlienType.OCTOPUS, Path.EXIT_STAGE_RIGHT, PathFactory.X_INVERT_FALSE,
            PathFactory.Y_INVERT_FALSE, 0, 0, 20, 0.3f, 0, false), new SubWaveProperty(AlienType.OCTOPUS, Path.EXIT_STAGE_RIGHT,
            PathFactory.X_INVERT_TRUE, PathFactory.Y_INVERT_FALSE, 0, 0, 20, 0.3f, 0, false)),

    /**
     * staggered attack where 5 columns of aliens attack from top to bottom and
     * then bounce back up. each adjacent column is delayed so each column is
     * delayed compared to the previous one.
     */
    STAGGERED_BOUNCE_ATTACK(true, new SubWaveProperty(AlienType.OCTOPUS, Path.BOUNCE_DOWN_AND_UP, PathFactory.X_INVERT_FALSE,
            PathFactory.Y_INVERT_FALSE, 40, 0, 5, 0.3f, 0, false),

            new SubWaveProperty(AlienType.OCTOPUS, Path.BOUNCE_DOWN_AND_UP, PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE, 40 + 92, 0, 5,
                    0.3f, 0.5f, false),

            new SubWaveProperty(AlienType.OCTOPUS, Path.BOUNCE_DOWN_AND_UP, PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE, 40 + (92 * 2),
                    0, 5, 0.3f, 1f, false),

            new SubWaveProperty(AlienType.OCTOPUS, Path.BOUNCE_DOWN_AND_UP, PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE, 40 + (92 * 3),
                    0, 5, 0.3f, 1.5f, false),

            new SubWaveProperty(AlienType.OCTOPUS, Path.BOUNCE_DOWN_AND_UP, PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE, 40 + (92 * 4),
                    0, 5, 0.3f, 2f, false),

            new SubWaveProperty(AlienType.OCTOPUS, Path.BOUNCE_DOWN_AND_UP, PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE, 40 + (92 * 5),
                    0, 5, 0.3f, 2.5f, false)),

    /**
     * Dragon attack - only head needs to be created. body parts will be spawned
     * by the head.
     */
    DRAGON_ATTACK(true, new SubWaveProperty(AlienType.DRAGON, Path.TRIANGLULAR_PATH, PathFactory.X_INVERT_FALSE,
            PathFactory.Y_INVERT_FALSE, 0, 0, 1, 0f, 0f, false)),

    WAVEY_LINE(true, new SubWaveProperty(AlienType.OCTOPUS, Path.SINGLE_ARC, PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE, 0, 0,
            10, 0.5f, 0, false), new SubWaveProperty(AlienType.OCTOPUS, Path.SINGLE_ARC, PathFactory.X_INVERT_TRUE,
            PathFactory.Y_INVERT_FALSE, 0, -200, 10, 0.5f, 0, false)),

    WAVE_02(true, new SubWaveProperty(AlienType.OCTOPUS, Path.BEZIER_DEMO, PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE, 0, 0,
            10, 0.5f, 0, false), new SubWaveProperty(AlienType.OCTOPUS, Path.BEZIER_DEMO, PathFactory.X_INVERT_TRUE,
            PathFactory.Y_INVERT_FALSE, 0, 0, 10, 0.5f, 0, false)),

    WAVE_03(true, new SubWaveProperty(AlienType.OCTOPUS, Path.CIRCULAR_DEMO, PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE, 0, 0,
            10, 0.5f, 0, false), new SubWaveProperty(AlienType.OCTOPUS, Path.CIRCULAR_DEMO, PathFactory.X_INVERT_TRUE,
            PathFactory.Y_INVERT_FALSE, 0, 0, 10, 0.5f, 0, false)),

    /**
     * Drops from top-left down to valley and up again to top right. Alternates
     * alien types.
     */
    VALLEY_DIVE(true, new SubWaveProperty(AlienType.OCTOPUS, Path.VALLEY_DROP, PathFactory.X_INVERT_FALSE, PathFactory.Y_INVERT_FALSE, 0,
            0, 10, 1f, 0, false), new SubWaveProperty(AlienType.MINION, Path.VALLEY_DROP, PathFactory.X_INVERT_FALSE,
            PathFactory.Y_INVERT_FALSE, 0, 0, 10, 1f, 0.5f, false)),

    WAVE_MOTHERSHIP(true, new SubWaveProperty(AlienType.MOTHERSHIP, Path.WAVEY_HORIZONTAL, PathFactory.X_INVERT_FALSE,
            PathFactory.Y_INVERT_FALSE, 0, 0, 1, 0, 0, true)),

    /**
     * Triangular attack path
     */
    WAVE_TRIANGULAR(true, new SubWaveProperty(AlienType.OCTOPUS, Path.TRIANGLULAR_PATH, PathFactory.X_INVERT_FALSE,
            PathFactory.Y_INVERT_FALSE, 0, 0, 10, 1f, 0f, false));

    /* references list of sub-waves */
    private List<SubWaveProperty> waveList;

    /* repeat sub-wave until all destroyed */
    private boolean repeatSubWave;

    /**
     * construct wave
     */
    SubWaveBuilder(boolean repeatSubWave, SubWaveProperty... waveArray) {
        this.waveList = Arrays.asList(waveArray);
        this.repeatSubWave = repeatSubWave;
    }

    public List<SubWaveProperty> getWaveList() {
        return waveList;
    }

    public boolean isRepeatSubWave() {
        return repeatSubWave;
    }
}
