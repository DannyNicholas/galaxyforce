package com.danosoftware.galaxyforce.waves.config.aliens.exploding;

import lombok.NonNull;

public class DelayedFollowerExplosionConfig extends ExplosionConfig {

  private final Float delayTime;

  public DelayedFollowerExplosionConfig(
      @NonNull final Float delayTime
  ) {
    super(ExplosionConfigType.FOLLOWER_DELAYED);
    this.delayTime = delayTime;
  }

  public static DelayedFollowerExplosionConfigBuilder builder() {
    return new DelayedFollowerExplosionConfigBuilder();
  }

  public Float getDelayTime() {
    return this.delayTime;
  }

  public static class DelayedFollowerExplosionConfigBuilder {
    private @NonNull Float delayTime;

    DelayedFollowerExplosionConfigBuilder() {
    }

    public DelayedFollowerExplosionConfig.DelayedFollowerExplosionConfigBuilder delayTime(@NonNull Float delayTime) {
      this.delayTime = delayTime;
      return this;
    }

    public DelayedFollowerExplosionConfig build() {
      return new DelayedFollowerExplosionConfig(delayTime);
    }
  }
}
