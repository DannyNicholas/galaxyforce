package com.danosoftware.galaxyforce.sprites.game.splash;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.sprites.common.AbstractMovingSprite;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;

/**
 * Splash screen base sprite that moves small way up the screen, pauses and then
 * changes sprite and accelerates up to the top.
 */
public class BaseMovingSprite extends AbstractMovingSprite {

    private enum BaseState {
        LAUNCH, PAUSE, ACCELERATE, FINISHED;
    }

    // base speed
    private static final int LAUNCH_DISTANCE_PER_SECOND = 500;
    private static final int ACCELERATE_DISTANCE_PER_SECOND = 1500;

    // base pause times
    private static final float DELAY_IN_SECONDS_BEFORE_START = 2.0f;
    private static final float DELAY_IN_SECONDS_FOR_PAUSING = 0.75f;

    // base Y positions
    private static final int START_BASE_Y_POS = 0 - (128 / 2);
    private static final int FINAL_BASE_Y_POS = GameConstants.SCREEN_TOP + (128 / 2);
    private static final int PAUSE_BASE_Y_POS = 200;

    // base sprites
    private static final ISpriteIdentifier BASE_FLAT = MenuSpriteIdentifier.BASE;
    private static final ISpriteIdentifier BASE_TILT = MenuSpriteIdentifier.BASE_TILT;

    private boolean launchSoundPlaying;

    // total animation time
    private float timeElapsed;

    // paused variables
    private float timeWhenPaused;
    private int pausedPosition;

    // acceleration variables
    private int maxDistanceToTravel;
    private float timeWhenAccelerating;

    private BaseState state;

    private final SoundPlayerService sounds;

    public BaseMovingSprite(
            final SoundPlayerService sounds
    ) {
        super(BASE_TILT, GameConstants.SCREEN_MID_X, START_BASE_Y_POS);
        this.timeElapsed = 0f;
        this.state = BaseState.LAUNCH;
        this.sounds = sounds;
        this.launchSoundPlaying = false;
    }

    @Override
    public void animate(float deltaTime) {
        timeElapsed += deltaTime;

        if (!launchSoundPlaying && timeElapsed >= DELAY_IN_SECONDS_BEFORE_START) {
            sounds.play(SoundEffect.SHIELD_PULSE);
            launchSoundPlaying = true;
        }

        switch (state) {
            case LAUNCH:
                int launchDistance = (int) ((timeElapsed - DELAY_IN_SECONDS_BEFORE_START) * LAUNCH_DISTANCE_PER_SECOND);
                moveY(START_BASE_Y_POS + launchDistance);
                if (y() >= PAUSE_BASE_Y_POS) {
                    state = BaseState.PAUSE;
                    pausedPosition = START_BASE_Y_POS + launchDistance;
                    timeWhenPaused = timeElapsed;
                }
                break;
            case PAUSE:
                if ((timeElapsed - timeWhenPaused) >= DELAY_IN_SECONDS_FOR_PAUSING) {
                    state = BaseState.ACCELERATE;
                    changeType(BASE_FLAT);
                    maxDistanceToTravel = FINAL_BASE_Y_POS - pausedPosition;
                    timeWhenAccelerating = timeElapsed;
                    sounds.play(SoundEffect.BIG_EXPLOSION);
                }
                break;
            case ACCELERATE:
                int acceleratingDistance = (int) Math.min((timeElapsed - timeWhenAccelerating) * ACCELERATE_DISTANCE_PER_SECOND, maxDistanceToTravel);
                moveY(pausedPosition + acceleratingDistance);
                if (y() >= FINAL_BASE_Y_POS) {
                    state = BaseState.FINISHED;
                }
                break;
        }
    }
}
