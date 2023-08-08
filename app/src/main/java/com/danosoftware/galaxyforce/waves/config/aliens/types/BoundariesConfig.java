package com.danosoftware.galaxyforce.waves.config.aliens.types;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;

public class BoundariesConfig {

  private final int minX;
  private final int maxX;
  private final int minY;
  private final int maxY;
  private final BoundaryLanePolicy lanePolicy;
  private final int lanes;

  BoundariesConfig(int minX, int maxX, int minY, int maxY, BoundaryLanePolicy lanePolicy, int lanes) {
    this.minX = minX;
    this.maxX = maxX;
    this.minY = minY;
    this.maxY = maxY;
    this.lanePolicy = lanePolicy;
    this.lanes = lanes;
  }

  public static BoundariesConfigBuilder builder() {
    return new BoundariesConfigBuilder();
  }

  public int getMinX() {
    return this.minX;
  }

  public int getMaxX() {
    return this.maxX;
  }

  public int getMinY() {
    return this.minY;
  }

  public int getMaxY() {
    return this.maxY;
  }

  public BoundaryLanePolicy getLanePolicy() {
    return this.lanePolicy;
  }

  public int getLanes() {
    return this.lanes;
  }

  public static class BoundariesConfigBuilder {

    private int minX = 0;
    private int maxX = GAME_WIDTH;
    private int minY = 0;
    private int maxY = GAME_HEIGHT;
    private BoundaryLanePolicy lanePolicy = BoundaryLanePolicy.NONE;
    private int lanes = 1;

    BoundariesConfigBuilder() {
    }

    public BoundariesConfig.BoundariesConfigBuilder minX(int minX) {
      this.minX = minX;
      return this;
    }

    public BoundariesConfig.BoundariesConfigBuilder maxX(int maxX) {
      this.maxX = maxX;
      return this;
    }

    public BoundariesConfig.BoundariesConfigBuilder minY(int minY) {
      this.minY = minY;
      return this;
    }

    public BoundariesConfig.BoundariesConfigBuilder maxY(int maxY) {
      this.maxY = maxY;
      return this;
    }

    public BoundariesConfig.BoundariesConfigBuilder lanePolicy(BoundaryLanePolicy lanePolicy) {
      this.lanePolicy = lanePolicy;
      return this;
    }

    public BoundariesConfig.BoundariesConfigBuilder lanes(int lanes) {
      this.lanes = lanes;
      return this;
    }

    public BoundariesConfig build() {
      return new BoundariesConfig(minX, maxX, minY, maxY, lanePolicy, lanes);
    }
  }
}
