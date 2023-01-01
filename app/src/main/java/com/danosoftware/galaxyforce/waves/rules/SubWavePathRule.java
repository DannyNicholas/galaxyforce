package com.danosoftware.galaxyforce.waves.rules;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;
import static com.danosoftware.galaxyforce.constants.GameConstants.SCREEN_MID_Y;

import com.danosoftware.galaxyforce.flightpath.paths.Path;
import com.danosoftware.galaxyforce.flightpath.paths.PathSpeed;
import com.danosoftware.galaxyforce.flightpath.translators.FlipXPointTranslator;
import com.danosoftware.galaxyforce.flightpath.translators.FlipYPointTranslator;
import com.danosoftware.galaxyforce.flightpath.translators.OffsetXPointTranslator;
import com.danosoftware.galaxyforce.flightpath.translators.OffsetYPointTranslator;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;
import com.danosoftware.galaxyforce.flightpath.translators.RotatePointTranslator;
import java.util.Arrays;
import java.util.List;

/**
 * Each sub-wave consists of one or more sub-wave properties.
 * <p>
 * Each sub-wave property contains enough data to create a sub-wave of aliens that follow a path.
 */
public enum SubWavePathRule {

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

  FIGURE_OF_EIGHT_SHORT(
      new SubWavePathRuleProperties(
          Path.FIGURE_OF_EIGHT,
          PathSpeed.NORMAL,
          10,
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
          10,
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
          5,
          0.3f,
          0,
          false
      ),
      new SubWavePathRuleProperties(
          Path.BELL_CURVE,
          PathSpeed.NORMAL,
          5,
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
          PathSpeed.VERY_SLOW,
          10,
          1.6f,
          0,
          false
      )
  ),

  LOOPER_ATTACK_REVERSE(
      new SubWavePathRuleProperties(
          Path.LOOPER,
          PathSpeed.VERY_SLOW,
          10,
          1.6f,
          0,
          false,
          new PointTranslatorChain()
              .add(new FlipYPointTranslator(GAME_HEIGHT))
              .add(new FlipXPointTranslator(GAME_WIDTH))
      )
  ),

  /**
   * tear drop attack
   */
  TEAR_DROP_ATTACK(
      new SubWavePathRuleProperties(
          Path.TEAR_DROP,
          PathSpeed.NORMAL,
          5,
          0.3f,
          0,
          false
      )
  ),

  /**
   * crossing aliens that attack from the top, cross over then leave at the bottom
   */
  CROSSING_STEP_ATTACK_BOTH(
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
  CROSSING_STEP_ATTACK_LEFT_SHORT(
      new SubWavePathRuleProperties(
          Path.BEZIER_STEP_UP,
          PathSpeed.NORMAL,
          10,
          0.3f,
          0,
          false
      )
  ),
  CROSSING_STEP_ATTACK_RIGHT_SHORT(
      new SubWavePathRuleProperties(
          Path.BEZIER_STEP_UP,
          PathSpeed.NORMAL,
          10,
          0.3f,
          0,
          false,
          new PointTranslatorChain()
              .add(new FlipXPointTranslator(GAME_WIDTH))
      )
  ),

  /**
   * spaced out crossover attack where aliens drop from top, pause and then exit on opposite side of
   * screen
   */
  CROSSOVER_EXIT_ATTACK_SPACED(
      new SubWavePathRuleProperties(
          Path.EXIT_STAGE_RIGHT,
          PathSpeed.NORMAL,
          5,
          1.5f,
          0,
          false
      ),
      new SubWavePathRuleProperties(
          Path.EXIT_STAGE_RIGHT,
          PathSpeed.NORMAL,
          5,
          1.5f,
          0,
          false,
          new PointTranslatorChain()
              .add(new FlipXPointTranslator(GAME_WIDTH))
      )
  ),
  /**
   * spaced out crossover attack where aliens rise from bottom, pause and then exit on opposite side
   * of screen
   */
  CROSSOVER_EXIT_ATTACK_SPACED_REVERSE(
      new SubWavePathRuleProperties(
          Path.EXIT_STAGE_RIGHT,
          PathSpeed.NORMAL,
          5,
          1.5f,
          0,
          false,
          new PointTranslatorChain()
              .add(new FlipYPointTranslator(GAME_HEIGHT))
      ),
      new SubWavePathRuleProperties(
          Path.EXIT_STAGE_RIGHT,
          PathSpeed.NORMAL,
          5,
          1.5f,
          0,
          false,
          new PointTranslatorChain()
              .add(new FlipXPointTranslator(GAME_WIDTH))
              .add(new FlipYPointTranslator(GAME_HEIGHT))
      )
  ),

  /**
   * crossover attack where aliens drop from top, pause and then exit on opposite side of screen
   */
  CROSSOVER_EXIT_ATTACK(
      new SubWavePathRuleProperties(
          Path.EXIT_STAGE_RIGHT,
          PathSpeed.NORMAL,
          5,
          0.75f,
          0,
          false
      ),
      new SubWavePathRuleProperties(
          Path.EXIT_STAGE_RIGHT,
          PathSpeed.NORMAL,
          5,
          0.75f,
          0,
          false,
          new PointTranslatorChain()
              .add(new FlipXPointTranslator(GAME_WIDTH))
      )
  ),

  /**
   * crossover attack where aliens rise from bottom, pause and then exit on opposite side of screen
   */
  CROSSOVER_EXIT_ATTACK_REVERSE(
      new SubWavePathRuleProperties(
          Path.EXIT_STAGE_RIGHT,
          PathSpeed.NORMAL,
          5,
          0.75f,
          0,
          false,
          new PointTranslatorChain()
              .add(new FlipYPointTranslator(GAME_HEIGHT))
      ),
      new SubWavePathRuleProperties(
          Path.EXIT_STAGE_RIGHT,
          PathSpeed.NORMAL,
          5,
          0.75f,
          0,
          false,
          new PointTranslatorChain()
              .add(new FlipXPointTranslator(GAME_WIDTH))
              .add(new FlipYPointTranslator(GAME_HEIGHT))
      )
  ),

  /**
   * staggered attack where 5 columns of aliens attack from top to bottom and then bounce back up.
   * each adjacent column is delayed so each column is delayed compared to the previous one.
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

  STAGGERED_SYMMETRIC_BOUNCE_ATTACK(
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
              .add(new OffsetXPointTranslator(40 + 115))
      ),
      new SubWavePathRuleProperties(
          Path.BOUNCE_DOWN_AND_UP,
          PathSpeed.NORMAL,
          5,
          0.3f,
          1f,
          false,
          new PointTranslatorChain()
              .add(new OffsetXPointTranslator(40 + (115 * 2)))
      ),
      new SubWavePathRuleProperties(
          Path.BOUNCE_DOWN_AND_UP,
          PathSpeed.NORMAL,
          5,
          0.3f,
          0.5f,
          false,
          new PointTranslatorChain()
              .add(new OffsetXPointTranslator(40 + (115 * 3)))
      ),
      new SubWavePathRuleProperties(
          Path.BOUNCE_DOWN_AND_UP,
          PathSpeed.NORMAL,
          5,
          0.3f,
          0f,
          false,
          new PointTranslatorChain()
              .add(new OffsetXPointTranslator(40 + (115 * 4)))
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
      )
  ),

  WAVEY_LINE_REVERSE(
      new SubWavePathRuleProperties(
          Path.SINGLE_ARC,
          PathSpeed.NORMAL,
          10,
          0.5f,
          0,
          false,
          new PointTranslatorChain()
              .add(new FlipXPointTranslator(GAME_WIDTH))
      )
  ),

  // curves from top to bottom arcing to right
  WAVEY_LINE_BENDING_RIGHT(
      new SubWavePathRuleProperties(
          Path.SINGLE_ARC,
          PathSpeed.NORMAL,
          5,
          0.5f,
          0,
          false,
          new PointTranslatorChain()
              .add(new RotatePointTranslator(RotatePointTranslator.Rotation.CLOCKWISE))
      )
  ),
  // curves from top to bottom arcing to left
  WAVEY_LINE_BENDING_LEFT(
      new SubWavePathRuleProperties(
          Path.SINGLE_ARC,
          PathSpeed.NORMAL,
          5,
          0.5f,
          0,
          false,
          new PointTranslatorChain()
              .add(new RotatePointTranslator(RotatePointTranslator.Rotation.ANTI_CLOCKWISE))
              .add(new FlipYPointTranslator(GAME_HEIGHT))
      )
  ),

  WAVEY_LINE_REVERSE_LOWER(
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

  WAVEY_LINE_DOUBLE(
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
   * Drops from top-left down to valley and up again to top right. Use interleaved version to
   * interleave a different alien type.
   */
  VALLEY_DIVE(
      new SubWavePathRuleProperties(
          Path.VALLEY_DROP,
          PathSpeed.FAST,
          10,
          1f,
          0,
          false
      )
  ),
  VALLEY_DIVE_INTERLEAVED(
      new SubWavePathRuleProperties(
          Path.VALLEY_DROP,
          PathSpeed.FAST,
          10,
          1f,
          0.5f,
          false
      )
  ),

  VALLEY_DIVE_FLIPPED(
      new SubWavePathRuleProperties(
          Path.VALLEY_DROP,
          PathSpeed.FAST,
          10,
          1f,
          0,
          false,
          new PointTranslatorChain()
              .add(new FlipYPointTranslator(GAME_HEIGHT))
      )
  ),
  VALLEY_DIVE_INTERLEAVED_FLIPPED(
      new SubWavePathRuleProperties(
          Path.VALLEY_DROP,
          PathSpeed.FAST,
          10,
          1f,
          0.5f,
          false,
          new PointTranslatorChain()
              .add(new FlipYPointTranslator(GAME_HEIGHT))
      )
  ),

  NORMAL_ARC(
      new SubWavePathRuleProperties(
          Path.SINGLE_ARC,
          PathSpeed.NORMAL,
          1,
          0,
          0,
          true,
          new PointTranslatorChain()
              .add(new OffsetYPointTranslator(125))
      )
  ),

  NORMAL_ARC_LOWER(
      new SubWavePathRuleProperties(
          Path.SINGLE_ARC,
          PathSpeed.NORMAL,
          1,
          0,
          0,
          true,
          new PointTranslatorChain()
              .add(new OffsetYPointTranslator(-125))
      )
  ),

  DELAYED_SLOW_ARC(
      new SubWavePathRuleProperties(
          Path.SINGLE_ARC,
          PathSpeed.VERY_SLOW,
          1,
          0,
          2.5f,
          true,
          new PointTranslatorChain()
              .add(new OffsetYPointTranslator(125))
      )
  ),

  SINGLE_ARC_NO_REPEAT(
      new SubWavePathRuleProperties(
          Path.SINGLE_ARC,
          PathSpeed.VERY_SLOW,
          1,
          0,
          1f,
          false,
          new PointTranslatorChain()
              .add(new OffsetYPointTranslator(125))
      )
  ),

  BOMBER_RUN(
      new SubWavePathRuleProperties(
          Path.SPACE_INVADER,
          PathSpeed.NORMAL,
          1,
          0f,
          0,
          true
      )
  ),

  DOUBLE_BOMBER_RUN(
      new SubWavePathRuleProperties(
          Path.SPACE_INVADER,
          PathSpeed.NORMAL,
          2,
          6.25f,
          0,
          true
      )
  ),

  STAGGERED_LEFT_AND_RIGHT(
      new SubWavePathRuleProperties(
          Path.LEFT_AND_RIGHT,
          PathSpeed.NORMAL,
          1,
          0f,
          0.5f,
          false,
          new PointTranslatorChain()
              .add(new OffsetYPointTranslator(GAME_HEIGHT - 230))
      ),
      new SubWavePathRuleProperties(
          Path.LEFT_AND_RIGHT,
          PathSpeed.NORMAL,
          1,
          0f,
          0,
          false,
          new PointTranslatorChain()
              .add(new OffsetYPointTranslator(GAME_HEIGHT - 200))
      ),
      new SubWavePathRuleProperties(
          Path.LEFT_AND_RIGHT,
          PathSpeed.NORMAL,
          1,
          0f,
          1f,
          false,
          new PointTranslatorChain()
              .add(new OffsetYPointTranslator(GAME_HEIGHT - 200))
      )
  ),

  STAGGERED_LEFT_AND_RIGHT_REVERSED(
      new SubWavePathRuleProperties(
          Path.LEFT_AND_RIGHT,
          PathSpeed.NORMAL,
          1,
          0f,
          0.5f,
          false,
          new PointTranslatorChain()
              .add(new OffsetYPointTranslator(GAME_HEIGHT - 230))
      ),
      new SubWavePathRuleProperties(
          Path.LEFT_AND_RIGHT,
          PathSpeed.NORMAL,
          1,
          0f,
          0,
          false,
          new PointTranslatorChain()
              .add(new OffsetYPointTranslator(GAME_HEIGHT - 200))
      ),
      new SubWavePathRuleProperties(
          Path.LEFT_AND_RIGHT,
          PathSpeed.NORMAL,
          1,
          0f,
          1f,
          false,
          new PointTranslatorChain()
              .add(new OffsetYPointTranslator(GAME_HEIGHT - 200))
      ),
      new SubWavePathRuleProperties(
          Path.LEFT_AND_RIGHT,
          PathSpeed.NORMAL,
          1,
          0f,
          0.5f,
          false,
          new PointTranslatorChain()
              .add(new FlipXPointTranslator(GAME_WIDTH))
              .add(new OffsetYPointTranslator(GAME_HEIGHT - 430))
      ),
      new SubWavePathRuleProperties(
          Path.LEFT_AND_RIGHT,
          PathSpeed.NORMAL,
          1,
          0f,
          0,
          false,
          new PointTranslatorChain()
              .add(new FlipXPointTranslator(GAME_WIDTH))
              .add(new OffsetYPointTranslator(GAME_HEIGHT - 400))
      ),
      new SubWavePathRuleProperties(
          Path.LEFT_AND_RIGHT,
          PathSpeed.NORMAL,
          1,
          0f,
          1f,
          false,
          new PointTranslatorChain()
              .add(new FlipXPointTranslator(GAME_WIDTH))
              .add(new OffsetYPointTranslator(GAME_HEIGHT - 400))
      )
  ),

  BOUNCING(
      new SubWavePathRuleProperties(
          Path.SPIRAL,
          PathSpeed.FAST,
          10,
          1f,
          0,
          false,
          new PointTranslatorChain()
              .add(new RotatePointTranslator(RotatePointTranslator.Rotation.CLOCKWISE))
              .add(new FlipXPointTranslator(GAME_WIDTH))
              .add(new OffsetYPointTranslator(-SCREEN_MID_Y))
      )
  ),
  BOUNCING_REVERSE(
      new SubWavePathRuleProperties(
          Path.SPIRAL,
          PathSpeed.FAST,
          10,
          1f,
          0,
          false,
          new PointTranslatorChain()
              .add(new RotatePointTranslator(RotatePointTranslator.Rotation.CLOCKWISE))
              .add(new OffsetYPointTranslator(-SCREEN_MID_Y))
      )
  ),
  BOUNCING_HIGHER(
      new SubWavePathRuleProperties(
          Path.SPIRAL,
          PathSpeed.FAST,
          10,
          1f,
          0,
          false,
          new PointTranslatorChain()
              .add(new RotatePointTranslator(RotatePointTranslator.Rotation.CLOCKWISE))
              .add(new FlipXPointTranslator(GAME_WIDTH))
              .add(new OffsetYPointTranslator(-SCREEN_MID_Y + 200))
      )
  ),
  BOUNCING_HIGHER_REVERSE(
      new SubWavePathRuleProperties(
          Path.SPIRAL,
          PathSpeed.FAST,
          10,
          1f,
          0,
          false,
          new PointTranslatorChain()
              .add(new RotatePointTranslator(RotatePointTranslator.Rotation.CLOCKWISE))
              .add(new OffsetYPointTranslator(-SCREEN_MID_Y + 200))
      )
  ),

  /**
   * Triangular attack path
   */
  WAVE_TRIANGULAR(
      new SubWavePathRuleProperties(
          Path.TRIANGULAR,
          PathSpeed.FAST,
          10,
          0.25f,
          0f,
          false)
  ),
  WAVE_TRIANGULAR_REVERSED(
      new SubWavePathRuleProperties(
          Path.TRIANGULAR,
          PathSpeed.FAST,
          10,
          0.25f,
          0f,
          false,
          new PointTranslatorChain()
              .add(new FlipXPointTranslator(GAME_WIDTH)))
  ),

  /**
   * Aliens follow diagonal path from top-left
   */
  DIAGONAL(
      new SubWavePathRuleProperties(
          Path.DIAGONAL,
          PathSpeed.NORMAL,
          5,
          0.25f,
          0f,
          false)
  ),

  /**
   * Aliens move diagonally from top-left to bottom-right followed by top-right to bottom-left
   */
  DIAGONAL_CROSSOVER(
      new SubWavePathRuleProperties(
          Path.DIAGONAL,
          PathSpeed.NORMAL,
          5,
          0.25f,
          0f,
          false),
      new SubWavePathRuleProperties(
          Path.DIAGONAL,
          PathSpeed.NORMAL,
          5,
          0.25f,
          1.25f,
          false,
          new PointTranslatorChain()
              .add(new FlipXPointTranslator(GAME_WIDTH)))
  ),

  /**
   * Aliens move diagonally from top-left to bottom-right and from top-right to bottom-left.
   * <p>
   * Paths are timed so that they just miss each other at the cross-over.
   */
  DIAGONAL_CROSSOVER_INTERLEAVED(
      new SubWavePathRuleProperties(
          Path.DIAGONAL,
          PathSpeed.NORMAL,
          5,
          0.5f,
          0f,
          false),
      new SubWavePathRuleProperties(
          Path.DIAGONAL,
          PathSpeed.NORMAL,
          5,
          0.5f,
          0.25f,
          false,
          new PointTranslatorChain()
              .add(new FlipXPointTranslator(GAME_WIDTH)))
  ),

  /**
   * Aliens follow square path around edge of screen
   */
  AROUND_EDGE(
      new SubWavePathRuleProperties(
          Path.SQUARE,
          PathSpeed.FAST,
          10,
          0.25f,
          0f,
          false)
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
