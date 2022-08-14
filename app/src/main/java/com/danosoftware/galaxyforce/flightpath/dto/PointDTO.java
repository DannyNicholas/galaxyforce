package com.danosoftware.galaxyforce.flightpath.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the Data Transfer Object of a Point.
 */
public class PointDTO {

  private final Integer x;
  private final Integer y;

  public PointDTO(
      @JsonProperty("x") Integer x,
      @JsonProperty("y") Integer y) {
    this.x = x;
    this.y = y;
  }

  public Integer getX() {
    return x;
  }

  public Integer getY() {
    return y;
  }
}
