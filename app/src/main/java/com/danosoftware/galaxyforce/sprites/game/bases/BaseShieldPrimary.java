package com.danosoftware.galaxyforce.sprites.game.bases;

import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.sprites.common.AbstractMovingSprite;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

public class BaseShieldPrimary extends AbstractMovingSprite implements IBaseShield {

    // shield animation that pulses every second
    private static final Animation SHIELD_PULSE = new Animation(
            0.25f,
            GameSpriteIdentifier.BASE_SHIELD_ONE,
            GameSpriteIdentifier.BASE_SHIELD_TWO,
            GameSpriteIdentifier.BASE_SHIELD_THREE,
            GameSpriteIdentifier.BASE_SHIELD_FOUR);
    private static final Animation LEFT_LEAN_SHIELD_PULSE = new Animation(
            0.25f,
            GameSpriteIdentifier.BASE_LEFT_SHIELD_ONE,
            GameSpriteIdentifier.BASE_LEFT_SHIELD_TWO,
            GameSpriteIdentifier.BASE_LEFT_SHIELD_THREE,
            GameSpriteIdentifier.BASE_LEFT_SHIELD_FOUR);
    private static final Animation RIGHT_LEAN_SHIELD_PULSE = new Animation(
            0.25f,
            GameSpriteIdentifier.BASE_RIGHT_SHIELD_ONE,
            GameSpriteIdentifier.BASE_RIGHT_SHIELD_TWO,
            GameSpriteIdentifier.BASE_RIGHT_SHIELD_THREE,
            GameSpriteIdentifier.BASE_RIGHT_SHIELD_FOUR);

    private final IBasePrimary base;

    // state time used to help select the current animation frame
    private float stateTime;

    public BaseShieldPrimary(IBasePrimary base, int xStart, int yStart, float syncTime) {
        super(
                getShieldAnimation(base)
                        .getKeyFrame(syncTime, Animation.ANIMATION_LOOPING),
                xStart,
                yStart);
        this.base = base;
        this.stateTime = syncTime;
    }

    @Override
    public void animate(float deltaTime) {
        // increase state time by delta
        stateTime += deltaTime;

        // set base sprite using animation loop and time through animation
        changeType(
                getShieldAnimation(base)
                        .getKeyFrame(stateTime, Animation.ANIMATION_LOOPING));
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

    /**
     * Return shield animation for current base lean
     */
    private static Animation getShieldAnimation(IBasePrimary base) {
        switch (base.getLean()) {
            case LEFT:
                return LEFT_LEAN_SHIELD_PULSE;
            case RIGHT:
                return RIGHT_LEAN_SHIELD_PULSE;
            case NONE:
                return SHIELD_PULSE;
        }
        throw new GalaxyForceException("Unknown Base Lean: " + base.getLean());
    }
}
