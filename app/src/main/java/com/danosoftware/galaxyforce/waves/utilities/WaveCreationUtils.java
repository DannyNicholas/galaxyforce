package com.danosoftware.galaxyforce.waves.utilities;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.flightpath.paths.PathFactory;
import com.danosoftware.galaxyforce.flightpath.paths.PathPoint;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.game.factories.AlienFactory;
import com.danosoftware.galaxyforce.waves.config.SubWaveNoPathConfig;
import com.danosoftware.galaxyforce.waves.config.SubWavePathConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.rules.SubWavePathRuleProperties;
import com.danosoftware.galaxyforce.waves.rules.SubWaveRuleProperties;
import java.util.ArrayList;
import java.util.List;

/**
 * Wave creation utilities
 */
public class WaveCreationUtils {

  private final AlienFactory alienFactory;
  private final PathFactory pathFactory;
  private final PowerUpAllocatorFactory powerUpAllocatorFactory;

  public WaveCreationUtils(
      AlienFactory alienFactory,
      PathFactory pathFactory,
      PowerUpAllocatorFactory powerUpAllocatorFactory) {
    this.alienFactory = alienFactory;
    this.pathFactory = pathFactory;
    this.powerUpAllocatorFactory = powerUpAllocatorFactory;
  }

  /**
   * Create a list of aliens that follow a path from the supplied config and model.
   *
   * @param config - contains sub-wave configuration
   * @return list of aliens
   */
  public List<IAlien> createPathAlienSubWave(
      final SubWavePathConfig config) {

    List<IAlien> aliens = new ArrayList<>();

    final AlienConfig alienConfig = config.getAlienConfig();
    final List<PowerUpType> powerUps = config.getPowerUps();
    final List<SubWavePathRuleProperties> properties = config.getSubWaveRuleProperties();

    // initialise power-up allocator
    int numberOfAliens = 0;
    for (SubWavePathRuleProperties props : properties) {
      numberOfAliens += props.getNumberOfAliens();
    }
    final PowerUpAllocator powerUpAllocator = powerUpAllocatorFactory.createAllocator(
        powerUps,
        numberOfAliens);

    for (SubWavePathRuleProperties props : properties) {

      // create path points (that alien will follow) for sub-wave
      List<PathPoint> path = pathFactory.createPath(
          props.getPath(),
          props.getTranslators(),
          props.getPathSpeed()
      );

      // create and add a sub-wave of aliens according to provided properties
      aliens.addAll(
          createAliens(alienConfig, powerUpAllocator, path, props)
      );
    }

    return aliens;
  }

  /**
   * Create a list of aliens from the supplied config and model. These aliens do not follow a normal
   * pre-defined path.
   *
   * @param config - contains sub-wave configuration
   * @return list of aliens
   */
  public List<IAlien> createNoPathAlienSubWave(
      final SubWaveNoPathConfig config) {

    List<IAlien> aliens = new ArrayList<>();

    final AlienConfig alienConfig = config.getAlienConfig();
    final List<PowerUpType> powerUps = config.getPowerUps();
    final List<SubWaveRuleProperties> properties = config.getSubWaveRuleProperties();

    // initialise power-up allocator
    int numberOfAliens = 0;
    for (SubWaveRuleProperties props : properties) {
      numberOfAliens += props.getNumberOfAliens();
    }
    final PowerUpAllocator powerUpAllocator = powerUpAllocatorFactory.createAllocator(
        powerUps,
        numberOfAliens);

    for (SubWaveRuleProperties props : properties) {

      for (int i = 0; i < props.getNumberOfAliens(); i++) {
        aliens.addAll(alienFactory.createAlien(
            alienConfig,
            powerUpAllocator.allocate(),
            props.isxRandom(),
            props.isyRandom(),
            props.getxStart(),
            props.getyStart(),
            (i * props.getDelayBetweenAliens()) + props.getDelayOffet(),
            props.isRestartImmediately()));
      }
    }

    return aliens;
  }

  /**
   * adds a wanted number of aliens with a path. each alien is spaced by the delay seconds
   * specified.
   */
  private List<IAlien> createAliens(
      final AlienConfig alienConfig,
      final PowerUpAllocator powerUpAllocator,
      final List<PathPoint> path,
      final SubWavePathRuleProperties props) {

    List<IAlien> aliensOnPath = new ArrayList<>();

    for (int i = 0; i < props.getNumberOfAliens(); i++) {
      aliensOnPath.addAll(
          alienFactory.createAlien(
              alienConfig,
              powerUpAllocator.allocate(),
              path,
              (i * props.getDelayBetweenAliens()) + props.getDelayOffet(),
              props.isRestartImmediately()
          ));
    }

    return aliensOnPath;
  }
}
