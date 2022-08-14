package com.danosoftware.galaxyforce.flightpath.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents a list of Data Transfer Object paths. Each Path can represent a different concrete
 * path implementation. This allows complex paths to be held.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PathListDTO {

  @JsonProperty("pathData")
  private final List<PathDTO> pathList;

  @JsonCreator
  public PathListDTO(@JsonProperty("pathData") List<PathDTO> pathList) {
    this.pathList = pathList;
  }

  public List<PathDTO> getPathList() {
    return pathList;
  }
}
