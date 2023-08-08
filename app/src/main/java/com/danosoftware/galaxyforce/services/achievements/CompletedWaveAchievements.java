package com.danosoftware.galaxyforce.services.achievements;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class CompletedWaveAchievements {

  private final int wave;
  private final boolean noLivesLostInWave;
}
