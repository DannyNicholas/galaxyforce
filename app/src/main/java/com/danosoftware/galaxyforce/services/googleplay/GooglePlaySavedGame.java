package com.danosoftware.galaxyforce.services.googleplay;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GooglePlaySavedGame {

  private final Integer highestWaveReached;

  @JsonCreator
  public GooglePlaySavedGame(
      @JsonProperty("highestWaveReached") Integer highestWaveReached) {

    this.highestWaveReached = highestWaveReached;
  }

  public Integer getHighestWaveReached() {
    return highestWaveReached;
  }
}
