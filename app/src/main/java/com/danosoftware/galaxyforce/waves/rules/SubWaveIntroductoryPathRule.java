package com.danosoftware.galaxyforce.waves.rules;

import com.danosoftware.galaxyforce.flightpath.paths.Path;
import com.danosoftware.galaxyforce.flightpath.paths.PathSpeed;
import java.util.Arrays;
import java.util.List;

/**
 * Each sub-wave consists of one or more sub-wave properties.
 * <p>
 * Each sub-wave property contains enough data to create a sub-wave of aliens that follow a path.
 */
public enum SubWaveIntroductoryPathRule {

  /**
   * Eye of Horus attack
   */
  EYE_OF_HORUS(
      new SubWaveIntroductoryPathRuleProperties(
          Path.EYE_OF_HORUS_INTRO,
          Path.EYE_OF_HORUS,
          PathSpeed.SLOW,
          1,
          0f,
          0
      )
  );


  // list of properties for a sub-wave
  private final List<SubWaveIntroductoryPathRuleProperties> subWaveProps;

  SubWaveIntroductoryPathRule(SubWaveIntroductoryPathRuleProperties... subWaveProps) {
    this.subWaveProps = Arrays.asList(subWaveProps);
  }

  /**
   * Properties to create a sub-wave
   */
  public List<SubWaveIntroductoryPathRuleProperties> subWaveProps() {
    return subWaveProps;
  }
}
