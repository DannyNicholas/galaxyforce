package com.danosoftware.galaxyforce.sprites.game.bases.explode;

import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.common.AbstractSprite;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

public class BaseExplosion extends AbstractSprite implements IBaseExplosion {

    private final static Animation ANIMATION = new Animation(
            0.075f,
            GameSpriteIdentifier.EXPLODE_BIG_01,
            GameSpriteIdentifier.EXPLODE_BIG_02,
            GameSpriteIdentifier.EXPLODE_BIG_03,
            GameSpriteIdentifier.EXPLODE_BIG_04,
            GameSpriteIdentifier.EXPLODE_BIG_05);
    private final IBaseExploder exploder;

    public BaseExplosion(
            int x,
            int y,
            SoundPlayerService sounds,
            VibrationService vibrator) {
        super(
                ANIMATION.getKeyFrame(
                0,
                Animation.ANIMATION_LOOPING),
                x,
                y);
        this.exploder = new BaseExploderSimple(sounds, vibrator, ANIMATION);
        this.exploder.startExplosion();
    }

    @Override
    public void startExplosion() {
        this.exploder.startExplosion();
    }

    @Override
    public void animate(float deltaTime) {
        changeType(exploder.getExplosion(deltaTime));
    }

    @Override
    public boolean isFinished() {
        return exploder.finishedExploding();
    }
}
