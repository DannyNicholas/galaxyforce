package com.danosoftware.galaxyforce.flightpath.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the Data Transfer Object of a paused paths.
 */
public class PausePathDTO extends PathDTO {

  private final PointDTO position;
  private final Float pauseTime;

  @JsonCreator
  public PausePathDTO(
      @JsonProperty("pauseTime") Float pauseTime,
      @JsonProperty("position") PointDTO position) {

    super(PathType.PAUSE);
    this.pauseTime = pauseTime;
    this.position = position;
  }

  public PointDTO getPosition() {
    return position;
  }

  public Float getPauseTime() {
    return pauseTime;
  }
}
