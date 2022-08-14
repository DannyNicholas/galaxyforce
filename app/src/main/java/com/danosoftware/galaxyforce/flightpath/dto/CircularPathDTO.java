package com.danosoftware.galaxyforce.flightpath.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the Data Transfer Object of Circular Paths.
 */
public class CircularPathDTO extends PathDTO {

  private final PointDTO centre;
  private final Double piMultiplier;

  @JsonCreator
  public CircularPathDTO(
      @JsonProperty("centre") PointDTO centre,
      @JsonProperty("piMultiplier") Double piMultiplier) {

    super(PathType.CIRCULAR);
    this.centre = centre;
    this.piMultiplier = piMultiplier;
  }

  public PointDTO getCentre() {
    return centre;
  }

  public Double getPiMultiplier() {
    return piMultiplier;
  }
}
