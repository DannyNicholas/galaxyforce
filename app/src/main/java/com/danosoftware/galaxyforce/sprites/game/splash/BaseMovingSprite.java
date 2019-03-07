package com.danosoftware.galaxyforce.sprites.game.splash;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.sprites.common.AbstractMovingSprite;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;

/**
 * Splash screen base sprte that moves small way up the screen, pauses and then
 * changes sprite and races up to the top.
 */
public class BaseMovingSprite extends AbstractMovingSprite {

    private enum BaseState {
        LAUNCH, PAUSE, ACCELERATE;
    }

    // variables to track base movements
    private static final int DISTANCE_PER_SECOND = 500;
    private static final int FAST_DISTANCE_PER_SECOND = 800;
    private static final float DELAY_IN_SECONDS_BEFORE_START = 2.5f;
    private static final float DELAY_IN_SECONDS_FOR_PAUSING = 0.25f;

    private static final int FINAL_BASE_Y_POS = GameConstants.SCREEN_TOP + (64 / 2);
    private static final int PAUSE_BASE_Y_POS = 200;

    // bsae sprites
    private static final ISpriteIdentifier BASE_FLAT = MenuSpriteIdentifier.BASE;
    private static final ISpriteIdentifier BASE_TILT = MenuSpriteIdentifier.BASE_TILT;

    private final int startPosition;
    private final int maxDistanceToTravel;
    private float timeElapsed;
    private float timeWhenPaused;

    private boolean baseTilted;
    private BaseState state;

    public BaseMovingSprite(int xStart, int yStart) {
        super(BASE_TILT, xStart, yStart);
        this.startPosition = yStart;
        this.maxDistanceToTravel = FINAL_BASE_Y_POS - yStart;
        this.timeElapsed = 0f;
        this.timeWhenPaused = 0f;
        this.baseTilted = true;
        this.state = BaseState.LAUNCH;
    }

    @Override
    public void animate(float deltaTime) {
        timeElapsed += deltaTime;


        switch (state) {

            case LAUNCH:
                int distance = (int) Math.min((timeElapsed - DELAY_IN_SECONDS_BEFORE_START) * DISTANCE_PER_SECOND, maxDistanceToTravel);
                moveY(startPosition + distance);
                if (baseTilted && y() > PAUSE_BASE_Y_POS) {
                    changeType(BASE_FLAT);
                    baseTilted = false;
                    timeWhenPaused = timeElapsed - DELAY_IN_SECONDS_BEFORE_START;
                    state = BaseState.PAUSE;
                }
                break;
            case PAUSE:
                float interimTime = (timeElapsed - DELAY_IN_SECONDS_BEFORE_START);
                float pauseTime = Math.min((interimTime - timeWhenPaused), DELAY_IN_SECONDS_FOR_PAUSING);
                int distance2 = (int) Math.min((interimTime - pauseTime) * DISTANCE_PER_SECOND, maxDistanceToTravel);
                moveY(startPosition + distance2);
                if (pauseTime >= DELAY_IN_SECONDS_FOR_PAUSING) {
                    state = BaseState.ACCELERATE;
                }
                break;
            case ACCELERATE:
                float interimTime2 = (timeElapsed - DELAY_IN_SECONDS_BEFORE_START);
                float pauseTime2 = Math.min((interimTime2 - timeWhenPaused), DELAY_IN_SECONDS_FOR_PAUSING);
                float originalDistance = (interimTime2 - pauseTime2) * DISTANCE_PER_SECOND;
                float acceleratedDistance = (interimTime2 - timeWhenPaused) * FAST_DISTANCE_PER_SECOND;

//            int distance = (int) Math.min((interimTime - pauseTime) * DISTANCE_PER_SECOND, maxDistanceToTravel);
                int distance3 = (int) Math.min(originalDistance + acceleratedDistance, maxDistanceToTravel);
                moveY(startPosition + distance3);
                break;
        }

//        if (baseTilted) {
//            int distance = (int) Math.min((timeElapsed - DELAY_IN_SECONDS_BEFORE_START) * DISTANCE_PER_SECOND, maxDistanceToTravel);
//            moveY(startPosition + distance);
//            if (baseTilted && y() > PAUSE_BASE_Y_POS) {
//                changeType(BASE_FLAT);
//                baseTilted = false;
//                timeWhenPaused = timeElapsed - DELAY_IN_SECONDS_BEFORE_START;
//            }
//        } else {
//            float interimTime = (timeElapsed - DELAY_IN_SECONDS_BEFORE_START);
//            float pauseTime = Math.min((interimTime - timeWhenPaused), DELAY_IN_SECONDS_FOR_PAUSING);
//            float originalDistance = (interimTime - pauseTime) * DISTANCE_PER_SECOND;
//            float acceleratedDistance = (interimTime - timeWhenPaused) * FAST_DISTANCE_PER_SECOND;
//
////            int distance = (int) Math.min((interimTime - pauseTime) * DISTANCE_PER_SECOND, maxDistanceToTravel);
//            int distance = (int) Math.min(originalDistance + acceleratedDistance, maxDistanceToTravel);
//            moveY(startPosition + distance);
//        }

    }
}
