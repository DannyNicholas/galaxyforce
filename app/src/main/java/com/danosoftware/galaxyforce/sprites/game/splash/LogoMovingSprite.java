package com.danosoftware.galaxyforce.sprites.game.splash;

import com.danosoftware.galaxyforce.sprites.common.AbstractMovingSprite;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

import static com.danosoftware.galaxyforce.constants.GameConstants.LOGO_Y_POS;

public class LogoMovingSprite extends AbstractMovingSprite {

    // variables to track logo movements
    private static final int DISTANCE_PER_SECOND = 100;

    private final int startPosition;
    private final int maxDistanceToTravel;
    private float timeElapsed;

    public LogoMovingSprite(int xStart, int yStart, ISpriteIdentifier spriteId) {
        super(spriteId, xStart, yStart);
        this.startPosition = yStart;
        this.maxDistanceToTravel = yStart - LOGO_Y_POS;
        this.timeElapsed = 0f;
    }

    @Override
    public void animate(float deltaTime) {
        timeElapsed += deltaTime;
        int distance = (int) Math.min(timeElapsed * DISTANCE_PER_SECOND, maxDistanceToTravel);
        moveY(startPosition - distance);
    }
}
