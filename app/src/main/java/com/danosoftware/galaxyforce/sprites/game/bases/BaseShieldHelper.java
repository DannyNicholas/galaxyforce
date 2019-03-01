package com.danosoftware.galaxyforce.sprites.game.bases;

import com.danosoftware.galaxyforce.sprites.common.AbstractMovingSprite;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

public class BaseShieldHelper extends AbstractMovingSprite implements IBaseShield {

    // shield animation that pulses every second
    private static final Animation SHIELD_PULSE = new Animation(
            0.25f,
            GameSpriteIdentifier.HELPER_SHIELD_ONE,
            GameSpriteIdentifier.HELPER_SHIELD_TWO,
            GameSpriteIdentifier.HELPER_SHIELD_THREE,
            GameSpriteIdentifier.HELPER_SHIELD_FOUR);

    // state time used to help select the current animation frame
    private float stateTime;

    public BaseShieldHelper(int xStart, int yStart, float syncTime) {
        super(SHIELD_PULSE.getKeyFrame(syncTime, Animation.ANIMATION_LOOPING), xStart, yStart);
        this.stateTime = syncTime;
    }

    @Override
    public void animate(float deltaTime) {
        // increase state time by delta
        stateTime += deltaTime;

        // set base sprite using animation loop and time through animation
        changeType(SHIELD_PULSE.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING));
    }

    /**
     * Shields can be added at different times. A base with a shield may gain
     * some shielded helpers. In order to keep all shields animating in sync, it
     * should be possible to share the state time used for synchronisation.
     * <p>
     * Normally the helper bases will be synchronised to the primary base.
     *
     * @return synchronisation timing
     */
    @Override
    public float getSynchronisation() {
        return stateTime;
    }
}
