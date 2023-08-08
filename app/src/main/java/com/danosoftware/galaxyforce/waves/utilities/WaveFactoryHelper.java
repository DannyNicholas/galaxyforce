package com.danosoftware.galaxyforce.waves.utilities;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.config.SubWaveConfig;
import com.danosoftware.galaxyforce.waves.rules.SubWaveRuleProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WaveFactoryHelper {

  public static final List<PowerUpType> NO_POWER_UPS = Collections.emptyList();
  public static final float HALF_PI = (float) (Math.PI / 2f);
  public static final float DOWNWARDS = -HALF_PI;
  public static final float PI = (float) (Math.PI);
  public static final float QUARTER_PI = (float) (Math.PI / 4f);

  /*
   * Flatten an array of sub-wave configs into a single array
   */
  public static SubWaveConfig[] flatten(SubWaveConfig[]... configs) {
    List<SubWaveConfig> list = new ArrayList<>();
    for (SubWaveConfig[] array : configs) {
      list.addAll(Arrays.asList(array));
    }
    SubWaveConfig[] itemsArray = new SubWaveConfig[list.size()];
    return list.toArray(itemsArray);
  }

  /**
   * Create an subwave property for alien at the wanted position
   */
  public static SubWaveRuleProperties createAlienSubWaveProperty(
      final int row,
      final int xPos,
      final float delayBetweenRows) {
    return new SubWaveRuleProperties(
        false,
        false,
        xPos,
        GameConstants.SCREEN_TOP,
        1,
        0,
        row * delayBetweenRows,
        false);
  }

  /**
   * Create an subwave property for alien at the wanted position
   */
  public static SubWaveRuleProperties createAlienSubWaveProperty(
      final int xPos,
      final int yPos,
      final boolean restartImmediately) {
    return new SubWaveRuleProperties(
        false,
        false,
        xPos,
        yPos,
        1,
        0,
        0,
        restartImmediately);
  }

  public static SubWaveRuleProperties randomStartFromTop(float maxDelay) {
    return new SubWaveRuleProperties(
        true,
        false,
        0,
        GameConstants.SCREEN_TOP,
        1,
        0f,
        new Random().nextFloat() * maxDelay,
        false
    );
  }

  public static SubWaveRuleProperties randomStartFromBottom(float maxDelay) {
    return new SubWaveRuleProperties(
        true,
        false,
        0,
        GameConstants.SCREEN_BOTTOM,
        1,
        0f,
        new Random().nextFloat() * maxDelay,
        false
    );
  }

  public static SubWaveRuleProperties randomStartFromLeft(float maxDelay) {
    return new SubWaveRuleProperties(
        false,
        true,
        GameConstants.SCREEN_LEFT,
        0,
        1,
        0f,
        new Random().nextFloat() * maxDelay,
        false
    );
  }

  public static SubWaveRuleProperties randomStartFromRight(float maxDelay) {
    return new SubWaveRuleProperties(
        false,
        true,
        GameConstants.SCREEN_RIGHT,
        0,
        1,
        0f,
        new Random().nextFloat() * maxDelay,
        false
    );
  }

  /**
   * A subwave rule that starts multiple aliens from top of the screen. Each subsequent alien is
   * delayed by a supplied time delay.
   *
   * @param numberOfAliens     - number of aliens in wave
   * @param delayBetweenAliens - delay between aliens
   * @param initialDelay       - delay before first alien starts
   * @return - subwave rule
   */
  public static List<SubWaveRuleProperties> multipleDelayedStartFromTop(
      int numberOfAliens,
      float delayBetweenAliens,
      float initialDelay) {

    List<SubWaveRuleProperties> alienRules = new ArrayList<>(numberOfAliens);

    for (int i = 0; i < numberOfAliens; i++) {
      alienRules.add(
          new SubWaveRuleProperties(
              true,
              false,
              0,
              GameConstants.SCREEN_TOP,
              1,
              0f,
              initialDelay + (delayBetweenAliens * i),
              false
          )
      );
    }
    return alienRules;
  }
}
