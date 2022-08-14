package com.danosoftware.galaxyforce.sprites.game.starfield;

import com.danosoftware.galaxyforce.models.screens.background.RgbColour;

public enum StarColour {

  RED(255f / 255f, 3f / 255f, 3f / 255f),
  BLUE(3f / 255f, 1f / 255f, 252f / 255f),
  WHITE(200f / 255f, 220f / 255f, 230f / 255f);

  private final RgbColour rgb;

  StarColour(float red, float green, float blue) {
    this.rgb = new RgbColour(red, green, blue);
  }

  public RgbColour getRgb() {
    return rgb;
  }
}
