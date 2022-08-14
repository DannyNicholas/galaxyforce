package com.danosoftware.galaxyforce.flightpath.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the Data Transfer Object of Linear Paths.
 */
public class LinearPathDTO extends PathDTO {

  private final Integer pathPoints;
  private final PointDTO start;
  private final PointDTO finish;

  @JsonCreator
  public LinearPathDTO(
      @JsonProperty("pathPoints") Integer pathPoints,
      @JsonProperty("start") PointDTO start,
      @JsonProperty("finish") PointDTO finish) {

    super(PathType.LINEAR);
    this.pathPoints = pathPoints;
    this.start = start;
    this.finish = finish;
  }

  public Integer getPathPoints() {
    return pathPoints;
  }

  public PointDTO getStart() {
    return start;
  }

  public PointDTO getFinish() {
    return finish;
  }
}
