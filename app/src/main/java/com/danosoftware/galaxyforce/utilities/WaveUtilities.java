package com.danosoftware.galaxyforce.utilities;

import com.danosoftware.galaxyforce.constants.GameConstants;

public class WaveUtilities {

  private WaveUtilities() {
    // stop any construction
  }

  /**
   * Is wave number a valid wave?
   */
  public static boolean isValidWave(int wave) {
    return (wave >= 1 && wave <= GameConstants.MAX_WAVES);
  }

}
