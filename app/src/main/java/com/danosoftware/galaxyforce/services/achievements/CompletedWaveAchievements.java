package com.danosoftware.galaxyforce.services.achievements;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompletedWaveAchievements {

  int wave;
  boolean noLivesLostInWave;
}
