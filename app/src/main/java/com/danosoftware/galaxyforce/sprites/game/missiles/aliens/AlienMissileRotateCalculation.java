package com.danosoftware.galaxyforce.sprites.game.missiles.aliens;

import lombok.Builder;
import lombok.Getter;

/**
 * Holds the calculated angle of travel and sprite rotation of an alien missile.
 */
@Builder
@Getter
public final class AlienMissileRotateCalculation {

  private final float angle;
  private final float rotation;
}
