package com.danosoftware.galaxyforce.enumerations;

public enum BaseMissileSpeed {
  NORMAL(15 * 60);

  // missile speed in pixels per second
  private final int speed;

  BaseMissileSpeed(int speed) {
    this.speed = speed;
  }

  public int getSpeed() {
    return speed;
  }
}
