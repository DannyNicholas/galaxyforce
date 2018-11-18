package com.danosoftware.galaxyforce.sprites.game.behaviours.explode;

import com.danosoftware.galaxyforce.sound.Sound;
import com.danosoftware.galaxyforce.sound.SoundEffect;
import com.danosoftware.galaxyforce.sound.SoundEffectBank;
import com.danosoftware.galaxyforce.sound.SoundEffectBankSingleton;
import com.danosoftware.galaxyforce.sound.SoundPlayer;
import com.danosoftware.galaxyforce.sound.SoundPlayerSingleton;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.vibration.VibrateTime;
import com.danosoftware.galaxyforce.vibration.Vibration;
import com.danosoftware.galaxyforce.vibration.VibrationSingleton;
import com.danosoftware.galaxyforce.view.Animation;

public class ExplodeSimple implements ExplodeBehaviour {
    // explosion animation
    // NOTE: not static. each instance needs it's own animation
    private final Animation animation = new Animation(0.15f, GameSpriteIdentifier.EXPLODE_01,
            GameSpriteIdentifier.EXPLODE_02, GameSpriteIdentifier.EXPLODE_03);

    /* initialise sound effects */
    private final static Sound EXPLOSION_SOUND;

    static {
        /* create reference to sound effects */
        SoundEffectBank soundBank = SoundEffectBankSingleton.getInstance();
        EXPLOSION_SOUND = soundBank.get(SoundEffect.EXPLOSION);
    }

    /* reference to sound player */
    private final SoundPlayer soundPlayer;

    /* reference to vibrator */
    private final Vibration vibrator;

    // time since explosion started
    private float explosionTime;

    public ExplodeSimple() {
        this.soundPlayer = SoundPlayerSingleton.getInstance();
        this.vibrator = VibrationSingleton.getInstance();
    }

    @Override
    public void startExplosion() {
        explosionTime = 0f;

        soundPlayer.playSound(EXPLOSION_SOUND);
        vibrator.vibrate(VibrateTime.TINY);
    }

    @Override
    public ISpriteIdentifier getExplosion(float deltaTime) {
        explosionTime += deltaTime;
        return animation.getKeyFrame(explosionTime, Animation.ANIMATION_NONLOOPING);
    }

    @Override
    public boolean finishedExploding() {
        return animation.isAnimationComplete();
    }
}
