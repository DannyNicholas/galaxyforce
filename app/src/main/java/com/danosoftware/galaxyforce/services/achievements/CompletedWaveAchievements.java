package com.danosoftware.galaxyforce.services.achievements;

public final class CompletedWaveAchievements {

  private final int wave;
  private final boolean noLivesLostInWave;

  CompletedWaveAchievements(int wave, boolean noLivesLostInWave) {
    this.wave = wave;
    this.noLivesLostInWave = noLivesLostInWave;
  }

  public static CompletedWaveAchievementsBuilder builder() {
    return new CompletedWaveAchievementsBuilder();
  }

  public int getWave() {
    return this.wave;
  }

  public boolean isNoLivesLostInWave() {
    return this.noLivesLostInWave;
  }

  public static class CompletedWaveAchievementsBuilder {
    private int wave;
    private boolean noLivesLostInWave;

    CompletedWaveAchievementsBuilder() {
    }

    public CompletedWaveAchievements.CompletedWaveAchievementsBuilder wave(int wave) {
      this.wave = wave;
      return this;
    }

    public CompletedWaveAchievements.CompletedWaveAchievementsBuilder noLivesLostInWave(boolean noLivesLostInWave) {
      this.noLivesLostInWave = noLivesLostInWave;
      return this;
    }

    public CompletedWaveAchievements build() {
      return new CompletedWaveAchievements(wave, noLivesLostInWave);
    }
  }
}
