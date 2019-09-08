package com.danosoftware.galaxyforce.sprites.game.behaviours.hit;

import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrateTime;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * Animated hit behaviour that will cause an animation sequence to be
 * displayed (normally when hit by a missile).
 * <p>
 * The sequence will run for a set time and then reset.
 * <p>
 * The parent sprite's stateTime is passed in to keep the animation
 * in sync with the parent.
 */
public class HitAnimation implements HitBehaviour {

    // max time to display hit
    private static final float HIT_TIME_SECONDS = 0.2f;

    // hit animation
    private final Animation hitAnimation;

    // reference to sound player
    private final SoundPlayerService sounds;

    // reference to vibrator
    private final VibrationService vibrator;

    // stateTime to keep in sync with parent animation
    private float stateTime;

    private boolean hit;
    private float timeSinceHit;

    public HitAnimation(
            SoundPlayerService sounds,
            VibrationService vibrator,
            Animation hitAnimation) {
        this.sounds = sounds;
        this.vibrator = vibrator;
        this.hitAnimation = hitAnimation;
        this.stateTime = 0f;
        this.hit = false;
        this.timeSinceHit = 0f;
    }

    @Override
    public void startHit(float stateTime) {
        startHitSilently(stateTime);
        sounds.play(SoundEffect.ALIEN_HIT);
        vibrator.vibrate(VibrateTime.TINY);
    }

    @Override
    public void startHitSilently(float stateTime) {
        this.hit = true;
        this.stateTime = stateTime;
        this.timeSinceHit = 0f;
    }

    @Override
    public boolean isHit() {
        return hit;
    }

    @Override
    public ISpriteIdentifier getHit(float deltaTime) {
        stateTime += deltaTime;
        timeSinceHit += deltaTime;
        if (timeSinceHit > HIT_TIME_SECONDS) {
            this.hit = false;
        }
        return hitAnimation.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING);
    }
}
