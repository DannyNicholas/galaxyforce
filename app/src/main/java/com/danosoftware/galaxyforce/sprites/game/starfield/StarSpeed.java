package com.danosoftware.galaxyforce.sprites.game.starfield;

/**
 * Represents the different possible star speeds. Each enum holds the numeric pixelsPerSecond
 * (pixels moved per second).
 */
public enum StarSpeed {

  SLOW(60 * 2),
  MEDIUM(60 * 4),
  FAST(60 * 6);

  private final int pixelsPerSecond;

  StarSpeed(int pixelsPerSecond) {
    this.pixelsPerSecond = pixelsPerSecond;
  }

  public int getPixelsPerSecond() {
    return pixelsPerSecond;
  }
}
