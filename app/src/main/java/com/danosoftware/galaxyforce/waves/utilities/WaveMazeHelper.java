package com.danosoftware.galaxyforce.waves.utilities;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;
import static com.danosoftware.galaxyforce.waves.utilities.AlienConfigBuilder.directionalAlienConfig;
import static com.danosoftware.galaxyforce.waves.utilities.WaveFactoryHelper.DOWNWARDS;
import static com.danosoftware.galaxyforce.waves.utilities.WaveFactoryHelper.NO_POWER_UPS;
import static com.danosoftware.galaxyforce.waves.utilities.WaveFactoryHelper.createAlienSubWaveProperty;

import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.config.SubWaveConfig;
import com.danosoftware.galaxyforce.waves.config.SubWaveNoPathConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningBySpeedConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DirectionalDestroyableConfig;
import com.danosoftware.galaxyforce.waves.rules.SubWaveRuleProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaveMazeHelper {

  private static final Random rand = new Random();

  // barrier options
  private static final int BARRIERS_PER_ROW = 5;
  private static final int BARRIERS_SPRITE_WIDTH = 108;

  // asteroid options
  private static final int ASTEROIDS_PER_ROW = 6;
  private static final int ASTEROIDS_SPRITE_WIDTH = 64;

  // maze block options
  private static final int BLOCKS_PER_ROW = 7;
  private static final int BLOCKS_SPRITE_WIDTH = 64;

  /**
   * Descending rows of asteroids with no gaps
   */
  public static SubWaveConfig[] asteroidHorizontalRows(
      int totalRows,
      final AlienSpeed speed,
      int delayBetweenRows,
      final List<PowerUpType> powerUps) {

    return new SubWaveConfig[]{
        new SubWaveNoPathConfig(
            rowsWithoutGaps(
                totalRows,
                delayBetweenRows,
                ASTEROIDS_PER_ROW,
                ASTEROIDS_SPRITE_WIDTH),
            DirectionalDestroyableConfig
                .builder()
                .alienCharacter(AlienCharacter.ASTEROID)
                .energy(5)
                .angle(DOWNWARDS)
                .speed(speed)
                .spinningConfig(
                    SpinningBySpeedConfig
                        .builder()
                        .build()
                )
                .build(),
            powerUps)
    };
  }

  /**
   * Maze of asteroids with a gap in each row.
   */
  public static SubWaveConfig[] asteroidMazeSubWave(
      int totalRows,
      final AlienSpeed speed,
      int delayBetweenRows,
      final List<PowerUpType> powerUps) {

    // create random gaps in maze - 1 gap per row
    List<Integer> mazeGaps = mazePositions(totalRows, ASTEROIDS_PER_ROW);

    return new SubWaveConfig[]{
        new SubWaveNoPathConfig(
            mazeSubWave(
                mazeGaps,
                delayBetweenRows,
                ASTEROIDS_PER_ROW,
                ASTEROIDS_SPRITE_WIDTH),
            DirectionalDestroyableConfig
                .builder()
                .alienCharacter(AlienCharacter.ASTEROID)
                .energy(5)
                .angle(DOWNWARDS)
                .speed(speed)
                .spinningConfig(
                    SpinningBySpeedConfig
                        .builder()
                        .build()
                )
                .build(),
            powerUps)
    };
  }

  /**
   * Create barrier maze dropping down from top with random gaps
   */
  public static SubWaveConfig[] createBarrierMaze(
      final int totalRows,
      final AlienSpeed speed,
      final float delayBetweenRows) {

    // create random gaps in maze - 1 gap per row
    List<Integer> mazeGaps = mazePositions(totalRows, BARRIERS_PER_ROW);

    return new SubWaveConfig[]{
        new SubWaveNoPathConfig(
            mazeSubWave(
                mazeGaps,
                delayBetweenRows,
                BARRIERS_PER_ROW,
                BARRIERS_SPRITE_WIDTH),
            directionalAlienConfig(
                AlienCharacter.BARRIER,
                DOWNWARDS,
                speed),
            NO_POWER_UPS)
    };
  }

  /**
   * Create barrier maze dropping down from top with random gaps
   */
  public static SubWaveConfig[] createBarrierMazeWithGuards(
      final int totalRows,
      final AlienSpeed speed,
      final int delayBetweenRows,
      final AlienCharacter alienGuard,
      final List<PowerUpType> powerUps) {

    // create random gaps in maze - 1 gap per row
    List<Integer> mazeGaps = mazePositions(totalRows, BARRIERS_PER_ROW);

    return new SubWaveConfig[]{
        new SubWaveNoPathConfig(
            mazeSubWave(
                mazeGaps,
                delayBetweenRows,
                BARRIERS_PER_ROW,
                BARRIERS_SPRITE_WIDTH),
            directionalAlienConfig(
                AlienCharacter.BARRIER,
                DOWNWARDS,
                speed),
            NO_POWER_UPS),
        new SubWaveNoPathConfig(
            mazeGuardsSubWave(
                mazeGaps,
                delayBetweenRows,
                BARRIERS_PER_ROW,
                BARRIERS_SPRITE_WIDTH),
            directionalAlienConfig(
                alienGuard,
                DOWNWARDS,
                speed),
            powerUps)
    };
  }


  /**
   * Creates a maze of multiple rows where a gap exists in each row.
   */
  private static List<SubWaveRuleProperties> mazeSubWave(
      List<Integer> mazeGaps,
      float delayBetweenRows,
      int columnsPerRow,
      int alienWidth) {

    List<SubWaveRuleProperties> subWaves = new ArrayList<>();

    // calculates min/max x positions for aliens per row
    final int minX = alienWidth / 2;
    final int maxX = GAME_WIDTH - (alienWidth / 2);

    final int distanceBetweenAliens = (maxX - minX) / (columnsPerRow - 1);

    int row = 0;
    for (int rowGap : mazeGaps) {
      for (int col = 0; col < columnsPerRow; col++) {
        // add maze element if this is not a gap
        if (col != rowGap) {
          final int xPos = minX + (col * distanceBetweenAliens);
          subWaves.add(createAlienSubWaveProperty(row, xPos, delayBetweenRows));
        }
      }
      row++;
    }
    return subWaves;
  }

  /**
   * Creates alien guards that fill the gaps in a maze.
   */
  private static List<SubWaveRuleProperties> mazeGuardsSubWave(
      List<Integer> mazeGaps,
      int delayBetweenRows,
      int columnsPerRow,
      int alienWidth) {

    List<SubWaveRuleProperties> subWaves = new ArrayList<>();

    // calculates min/max x positions for aliens per row
    final int minX = alienWidth / 2;
    final int maxX = GAME_WIDTH - (alienWidth / 2);

    final int distanceBetweenAliens = (maxX - minX) / (columnsPerRow - 1);

    int row = 0;
    for (int rowGap : mazeGaps) {
      // add alien guard into our maze gap
      final int xPos = minX + (rowGap * distanceBetweenAliens);
      subWaves.add(createAlienSubWaveProperty(row, xPos, delayBetweenRows));
      row++;
    }
    return subWaves;
  }

  /**
   * Creates a list of random positions. Each position can represent where the maze gap or block
   * exists for each row.
   */
  private static List<Integer> mazePositions(
      int totalRows,
      int columnsPerRow) {

    List<Integer> gaps = new ArrayList<>();

    for (int row = 0; row < totalRows; row++) {
      gaps.add(rand.nextInt(columnsPerRow));
    }
    return gaps;
  }

  /**
   * Creates subwave of multiple rows. Each row containing multiple aliens.
   */
  private static List<SubWaveRuleProperties> rowsWithoutGaps(
      int totalRows,
      int delayBetweenRows,
      int columnsPerRow,
      int alienWidth) {

    List<SubWaveRuleProperties> subWaves = new ArrayList<>();

    // calculates min/max x positions for aliens per row
    final int minX = alienWidth / 2;
    final int maxX = GAME_WIDTH - (alienWidth / 2);

    final int distanceBetweenAliens = (maxX - minX) / (columnsPerRow - 1);

    for (int row = 0; row < totalRows; row++) {
      for (int col = 0; col < columnsPerRow; col++) {
        final int xPos = minX + (col * distanceBetweenAliens);
        subWaves.add(createAlienSubWaveProperty(row, xPos, delayBetweenRows));
      }
    }
    return subWaves;
  }

  /**
   * Create maze dropping down from top with random blocks (1 per row).
   */
  public static SubWaveConfig[] createRandomBlockMaze(
      final int totalRows,
      final AlienSpeed speed,
      final float delayBetweenRows) {

    // create random blocks in maze - 1 block per row
    List<Integer> mazeBlocks = mazePositions(totalRows, BLOCKS_PER_ROW);

    return new SubWaveConfig[]{
        new SubWaveNoPathConfig(
            mazeBlockSubWave(
                mazeBlocks,
                delayBetweenRows,
                BLOCKS_PER_ROW,
                BLOCKS_SPRITE_WIDTH),
            directionalAlienConfig(
                AlienCharacter.BLOCK,
                DOWNWARDS,
                speed),
            NO_POWER_UPS)
    };
  }

  /**
   * Creates a maze of multiple rows where one block exists in each row.
   */
  private static List<SubWaveRuleProperties> mazeBlockSubWave(
      List<Integer> mazeBlocks,
      float delayBetweenRows,
      int columnsPerRow,
      int alienWidth) {

    List<SubWaveRuleProperties> subWaves = new ArrayList<>();

    // calculates min/max x positions for aliens per row
    final int minX = alienWidth / 2;
    final int maxX = GAME_WIDTH - (alienWidth / 2);

    final int distanceBetweenAliens = (maxX - minX) / (columnsPerRow - 1);

    int row = 0;
    for (int blockPosition : mazeBlocks) {
      final int xPos = minX + (blockPosition * distanceBetweenAliens);
      subWaves.add(createAlienSubWaveProperty(row, xPos, delayBetweenRows));
      row++;
    }
    return subWaves;
  }
}
