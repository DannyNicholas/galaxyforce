package com.danosoftware.galaxyforce.sprites.game.starfield;

/**
 * Star sprite that drifts down the screen over time and animates. When a star reaches the bottom of
 * the screen, it returns to the top. Many stars will be contained within an animated star-field.
 */
public class Star {

  // star's position and colour
  // allow public access for improved drawing performance
  public float x, y;
  public StarColour colour;

  public Star(
      float x,
      float y,
      StarColour colour) {
    this.x = x;
    this.y = y;
    this.colour = colour;
  }
}
