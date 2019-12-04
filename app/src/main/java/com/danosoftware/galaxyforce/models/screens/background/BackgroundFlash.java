package com.danosoftware.galaxyforce.models.screens.background;

import static com.danosoftware.galaxyforce.constants.GameConstants.BACKGROUND_BLUE;
import static com.danosoftware.galaxyforce.constants.GameConstants.BACKGROUND_GREEN;
import static com.danosoftware.galaxyforce.constants.GameConstants.BACKGROUND_RED;
import static com.danosoftware.galaxyforce.constants.GameConstants.DEFAULT_BACKGROUND_COLOUR;

/**
 * Provides the colour progression to provide a background flash.
 * Starts with a white flash for a short duration and then
 * fades to the normal background colour.
 */
public class BackgroundFlash {

    private final static float FLASH_TIME = 0.05f;
    private final static float FADE_TRANSITION_TIME = 0.12f;
    private final static float FLASH_RED = 1f;
    private final static float FLASH_GREEN = 1f;
    private final static float FLASH_BLUE = 1f;
    private final static RgbColour FLASH_COLOUR = new RgbColour(
            FLASH_RED,
            FLASH_GREEN,
            FLASH_BLUE);

    private final float redDelta;
    private final float greenDelta;
    private final float blueDelta;

    private float totalTime;

    public BackgroundFlash() {
        this.redDelta = (FLASH_RED - BACKGROUND_RED) / FADE_TRANSITION_TIME;
        this.greenDelta = (FLASH_GREEN - BACKGROUND_GREEN) / FADE_TRANSITION_TIME;
        this.blueDelta = (FLASH_BLUE - BACKGROUND_BLUE) / FADE_TRANSITION_TIME;
        this.totalTime = 0f;
    }

    public void update(float deltaTime) {
        totalTime += deltaTime;
    }

    public RgbColour background() {
        if (totalTime <= FLASH_TIME) {
            return FLASH_COLOUR;
        }

        if (totalTime >= (FLASH_TIME + FADE_TRANSITION_TIME)) {
            return DEFAULT_BACKGROUND_COLOUR;
        }

        final float transitionTime = totalTime - FLASH_TIME;
        return new RgbColour(
                FLASH_RED - (redDelta * transitionTime),
                FLASH_GREEN - (greenDelta * transitionTime),
                FLASH_BLUE - (blueDelta * transitionTime));
    }
}
