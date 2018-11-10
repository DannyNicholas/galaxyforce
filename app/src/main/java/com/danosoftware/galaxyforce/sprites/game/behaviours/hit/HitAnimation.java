package com.danosoftware.galaxyforce.sprites.game.behaviours.hit;

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
    private static float HIT_TIME_SECONDS = 0.5f;

    // hit animation
    private final Animation hitAnimation;

    // stateTime to keep in sync with parent animation
    private float stateTime;

    private boolean hit;
    private float timeSinceHit;

    public HitAnimation(Animation hitAnimation) {
        this.hitAnimation = hitAnimation;
        this.stateTime = 0f;
        this.hit = false;
        this.timeSinceHit = 0f;
    }

    @Override
    public void startHit(float stateTime) {
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
        return hitAnimation.getKeyFrame(stateTime, Animation.ANIMATION_NONLOOPING);
    }
}
