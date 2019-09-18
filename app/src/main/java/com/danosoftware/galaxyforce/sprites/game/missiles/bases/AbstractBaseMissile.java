package com.danosoftware.galaxyforce.sprites.game.missiles.bases;

import com.danosoftware.galaxyforce.sprites.common.AbstractCollidingSprite;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.view.Animation;


public abstract class AbstractBaseMissile extends AbstractCollidingSprite implements IBaseMissile {

    // how much energy will be lost by alien when this missile hits it
    private final int hitEnergy;

    private boolean isDestroyed;

    private final Animation animation;

    // state time used to select the current animation frame
    private float stateTime;

    AbstractBaseMissile(
            Animation animation,
            int x,
            int y,
            int hitEnergy) {

        // adjust missile starting position by half the missile's height
        super(
                animation.getKeyFrame(
                        0,
                        Animation.ANIMATION_LOOPING),
                x,
                y + (
                        animation.getKeyFrame(
                                0,
                                Animation.ANIMATION_LOOPING))
                        .getProperties()
                        .getHeight() / 2);
        this.hitEnergy = hitEnergy;
        this.isDestroyed = false;
        this.animation = animation;
        this.stateTime = 0f;
    }

    // by default, most base missiles will only hit an alien once and destroy themselves
    @Override
    public boolean hitBefore(IAlien alien) {
        return false;
    }

    @Override
    public int energyDamage() {
        return hitEnergy;
    }

    @Override
    public void destroy() {
        this.isDestroyed = true;
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    public void animate(float deltaTime) {
        stateTime += deltaTime;
        changeType(animation.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING));
    }
}
