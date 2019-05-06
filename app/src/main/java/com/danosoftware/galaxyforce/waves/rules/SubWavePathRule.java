package com.danosoftware.galaxyforce.waves.rules;

import com.danosoftware.galaxyforce.flightpath.paths.Path;
import com.danosoftware.galaxyforce.flightpath.paths.PathSpeed;
import com.danosoftware.galaxyforce.flightpath.translators.FlipXPointTranslator;
import com.danosoftware.galaxyforce.flightpath.translators.FlipYPointTranslator;
import com.danosoftware.galaxyforce.flightpath.translators.OffsetXPointTranslator;
import com.danosoftware.galaxyforce.flightpath.translators.OffsetYPointTranslator;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;

import java.util.Arrays;
import java.util.List;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;
import static com.danosoftware.galaxyforce.constants.GameConstants.SCREEN_MID_X;

/**
 * Each sub-wave consists of one or more sub-wave properties.
 * <p>
 * Each sub-wave property contains enough data to create a sub-wave
 * of aliens that follow a path.
 */
public enum SubWavePathRule {

    /**
     * space invader style attack
     */
    SPACE_INVADER(
            new SubWavePathRuleProperties(
                    Path.SPACE_INVADER,
                    PathSpeed.NORMAL,
                    20,
                    0.3f,
                    0,
                    false
            )
    ),

    /**
     * space invader attack in reverse (i.e. from bottom to top)
     */
    SPACE_INVADER_REVERSE(
            new SubWavePathRuleProperties(
                    Path.SPACE_INVADER,
                    PathSpeed.NORMAL,
                    20,
                    0.3f,
                    0,
                    false,
                    new PointTranslatorChain()
                            .add(new FlipXPointTranslator(GAME_WIDTH))
                            .add(new FlipYPointTranslator(GAME_HEIGHT))
            )
    ),

    /**
     * figure of eight path
     */
    FIGURE_OF_EIGHT(
            new SubWavePathRuleProperties(
                    Path.FIGURE_OF_EIGHT,
                    PathSpeed.NORMAL,
                    20,
                    0.3f,
                    0,
                    false
            )
    ),

    /**
     * bell curve attack
     */
    BELL_CURVE(
            new SubWavePathRuleProperties(
                    Path.BELL_CURVE,
                    PathSpeed.NORMAL,
                    20,
                    0.3f,
                    0,
                    false
            )
    ),

    /**
     * bell curve attack from top and bottom
     */
    DOUBLE_BELL_CURVE(
            new SubWavePathRuleProperties(
                    Path.BELL_CURVE,
                    PathSpeed.NORMAL,
                    20,
                    0.3f,
                    0,
                    false
            ),
            new SubWavePathRuleProperties(
                    Path.BELL_CURVE,
                    PathSpeed.NORMAL,
                    20,
                    0.3f,
                    0,
                    false,
                    new PointTranslatorChain()
                            .add(new FlipYPointTranslator(GAME_HEIGHT))
            )
    ),

    /**
     * twisted loop attack
     */
    LOOPER_ATTACK(
            new SubWavePathRuleProperties(
                    Path.LOOPER,
                    PathSpeed.NORMAL,
                    20,
                    0.3f,
                    0,
                    false
            )
    ),

    /**
     * multiple tear drop attacks
     */
    TEAR_DROP_ATTACK(
            new SubWavePathRuleProperties(
                    Path.TEAR_DROP,
                    PathSpeed.NORMAL,
                    20,
                    0.3f,
                    0,
                    false,
                    new PointTranslatorChain()
                            .add(new OffsetXPointTranslator(-200))
            ),
            new SubWavePathRuleProperties(
                    Path.TEAR_DROP,
                    PathSpeed.NORMAL,
                    20,
                    0.3f,
                    0,
                    false
            ),
            new SubWavePathRuleProperties(
                    Path.TEAR_DROP,
                    PathSpeed.NORMAL,
                    20,
                    0.3f,
                    0,
                    false,
                    new PointTranslatorChain()
                            .add(new OffsetXPointTranslator(200))
            )
    ),

    /**
     * crossing aliens that attack from the top, cross over then leave at the
     * bottom
     */
    CROSSING_STEP_ATTACK(
            new SubWavePathRuleProperties(
                    Path.BEZIER_STEP_UP,
                    PathSpeed.NORMAL,
                    20,
                    0.3f,
                    0,
                    false
            ),
            new SubWavePathRuleProperties(
                    Path.BEZIER_STEP_UP,
                    PathSpeed.NORMAL,
                    20,
                    0.3f,
                    0,
                    false,
                    new PointTranslatorChain()
                            .add(new FlipXPointTranslator(GAME_WIDTH))
            )
    ),

    /**
     * crossover attack where aliens drop from top, pause and then exit on
     * opposite side of screen
     */
    CROSSOVER_EXIT_ATTACK(
            new SubWavePathRuleProperties(
                    Path.EXIT_STAGE_RIGHT,
                    PathSpeed.NORMAL,
                    20,
                    0.3f,
                    0,
                    false
            ),
            new SubWavePathRuleProperties(
                    Path.EXIT_STAGE_RIGHT,
                    PathSpeed.NORMAL,
                    20,
                    0.3f,
                    0,
                    false,
                    new PointTranslatorChain()
                            .add(new FlipXPointTranslator(GAME_WIDTH))
            )
    ),

    /**
     * staggered attack where 5 columns of aliens attack from top to bottom and
     * then bounce back up. each adjacent column is delayed so each column is
     * delayed compared to the previous one.
     */
    STAGGERED_BOUNCE_ATTACK(
            new SubWavePathRuleProperties(
                    Path.BOUNCE_DOWN_AND_UP,
                    PathSpeed.NORMAL,
                    5,
                    0.3f,
                    0,
                    false,
                    new PointTranslatorChain()
                            .add(new OffsetXPointTranslator(40))
            ),
            new SubWavePathRuleProperties(
                    Path.BOUNCE_DOWN_AND_UP,
                    PathSpeed.NORMAL,
                    5,
                    0.3f,
                    0.5f,
                    false,
                    new PointTranslatorChain()
                            .add(new OffsetXPointTranslator(40 + 92))
            ),
            new SubWavePathRuleProperties(
                    Path.BOUNCE_DOWN_AND_UP,
                    PathSpeed.NORMAL,
                    5,
                    0.3f,
                    1f,
                    false,
                    new PointTranslatorChain()
                            .add(new OffsetXPointTranslator(40 + (92 * 2)))
            ),
            new SubWavePathRuleProperties(
                    Path.BOUNCE_DOWN_AND_UP,
                    PathSpeed.NORMAL,
                    5,
                    0.3f,
                    1.5f,
                    false,
                    new PointTranslatorChain()
                            .add(new OffsetXPointTranslator(40 + (92 * 3)))
            ),
            new SubWavePathRuleProperties(
                    Path.BOUNCE_DOWN_AND_UP,
                    PathSpeed.NORMAL,
                    5,
                    0.3f,
                    2f,
                    false,
                    new PointTranslatorChain()
                            .add(new OffsetXPointTranslator(40 + (92 * 4)))
            ),
            new SubWavePathRuleProperties(
                    Path.BOUNCE_DOWN_AND_UP,
                    PathSpeed.NORMAL,
                    5,
                    0.3f,
                    2.5f,
                    false,
                    new PointTranslatorChain()
                            .add(new OffsetXPointTranslator(40 + (92 * 5)))
            )
    ),

    WAVEY_LINE(
            new SubWavePathRuleProperties(
                    Path.SINGLE_ARC,
                    PathSpeed.NORMAL,
                    10,
                    0.5f,
                    0,
                    false
            ),
            new SubWavePathRuleProperties(
                    Path.SINGLE_ARC,
                    PathSpeed.NORMAL,
                    10,
                    0.5f,
                    0,
                    false,
                    new PointTranslatorChain()
                            .add(new FlipXPointTranslator(GAME_WIDTH))
                            .add(new OffsetYPointTranslator(-200))
            )
    ),

    /**
     * Drops from top-left down to valley and up again to top right.
     * Use interleaved version to interleave a different alien type.
     */
    VALLEY_DIVE(
            new SubWavePathRuleProperties(
                    Path.VALLEY_DROP,
                    PathSpeed.NORMAL,
                    10,
                    1f,
                    0,
                    false
            )
    ),
    VALLEY_DIVE_INTERLEAVED(
            new SubWavePathRuleProperties(
                    Path.VALLEY_DROP,
                    PathSpeed.NORMAL,
                    10,
                    1f,
                    0.5f,
                    false
            )
    ),

    WAVE_MOTHERSHIP(
            new SubWavePathRuleProperties(
                    Path.SINGLE_ARC,
                    PathSpeed.NORMAL,
                    1,
                    0,
                    0,
                    true
            )
    ),

    /**
     * One spiral path from top to bottom
     */
    SPIRAL(
            new SubWavePathRuleProperties(
                    Path.SPIRAL,
                    PathSpeed.NORMAL,
                    10,
                    1f,
                    0,
                    false
            )
    ),

    /**
     * Two side-by-side spiral path from top to bottom
     */
    DOUBLE_SPIRAL(
            new SubWavePathRuleProperties(
                    Path.SPIRAL,
                    PathSpeed.NORMAL,
                    10,
                    1f,
                    0,
                    false,
                    new PointTranslatorChain()
                            .add(new OffsetXPointTranslator(SCREEN_MID_X / 2))
            ),
            new SubWavePathRuleProperties(
                    Path.SPIRAL,
                    PathSpeed.NORMAL,
                    10,
                    1f,
                    0,
                    false,
                    new PointTranslatorChain()
                            .add(new OffsetXPointTranslator(-SCREEN_MID_X / 2))
            )
    ),

    /**
     * Triangular attack path
     */
    WAVE_TRIANGULAR(
            new SubWavePathRuleProperties(
                    Path.TRIANGULAR,
                    PathSpeed.VERY_FAST,
                    10,
                    1f,
                    0f,
                    false
            )
    );

    // list of properties for a sub-wave
    private final List<SubWavePathRuleProperties> subWaveProps;

    SubWavePathRule(SubWavePathRuleProperties... subWaveProps) {
        this.subWaveProps = Arrays.asList(subWaveProps);
    }

    /**
     * Properties to create a sub-wave
     */
    public List<SubWavePathRuleProperties> subWaveProps() {
        return subWaveProps;
    }
}
