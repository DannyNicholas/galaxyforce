package com.danosoftware.galaxyforce.waves.config;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.rules.SubWaveIntroductoryPathRule;
import com.danosoftware.galaxyforce.waves.rules.SubWaveIntroductoryPathRuleProperties;
import java.util.List;

/**
 * Describes a sub-wave consisting of a set of path rules and an alien config
 */
public class SubWaveIntroductoryPathConfig extends SubWaveConfig {

  // list of properties for a sub-wave
  private final List<SubWaveIntroductoryPathRuleProperties> subWaveRuleProperties;

  public SubWaveIntroductoryPathConfig(
      final SubWaveIntroductoryPathRule subWaveRule,
      final AlienConfig alienConfig,
      final List<PowerUpType> powerUps) {

    super(Type.INTRODUCTORY_PATH, alienConfig, powerUps);
    this.subWaveRuleProperties = subWaveRule.subWaveProps();
  }

  public SubWaveIntroductoryPathConfig(
      final List<SubWaveIntroductoryPathRuleProperties> subWaveRuleProperties,
      final AlienConfig alienConfig,
      final List<PowerUpType> powerUps) {

    super(Type.INTRODUCTORY_PATH, alienConfig, powerUps);
    this.subWaveRuleProperties = subWaveRuleProperties;
  }

  public List<SubWaveIntroductoryPathRuleProperties> getSubWaveRuleProperties() {
    return subWaveRuleProperties;
  }
}
