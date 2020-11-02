package com.danosoftware.galaxyforce.waves.config.aliens.types;

import lombok.Builder;
import lombok.Getter;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;

@Builder
@Getter
public class BoundariesConfig {
    @Builder.Default
    private final int minX = 0;
    @Builder.Default
    private final int maxX = GAME_WIDTH;
    @Builder.Default
    private final int minY = 0;
    @Builder.Default
    private final int maxY = GAME_HEIGHT;
    @Builder.Default
    private final BoundaryLanePolicy lanePolicy = BoundaryLanePolicy.NONE;
    @Builder.Default
    private final int lanes = 1;
}
