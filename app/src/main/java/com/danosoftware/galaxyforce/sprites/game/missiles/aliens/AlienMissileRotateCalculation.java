package com.danosoftware.galaxyforce.sprites.game.missiles.aliens;

import lombok.Builder;
import lombok.Value;

/**
 * Holds the calculated angle of travel and sprite rotation of an alien missile.
 */
@Value
@Builder
public class AlienMissileRotateCalculation {

    private final float angle;
    private final int rotation;

}
