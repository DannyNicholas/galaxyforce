package com.danosoftware.galaxyforce.sprites.game.behaviours.explode;

import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.services.sound.SoundPlayerService;
import com.danosoftware.galaxyforce.services.vibration.VibrateTime;
import com.danosoftware.galaxyforce.services.vibration.VibrationService;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.view.Animation;

public class ExplodeSimple implements ExplodeBehaviour {

    // explosion animation
    private final Animation animation;

    // reference to sound player
    private final SoundPlayerService sounds;

    // reference to vibrator
    private final VibrationService vibrator;

    // time since explosion started
    private float explosionTime;

    public ExplodeSimple(
            SoundPlayerService sounds,
            VibrationService vibrator,
            Animation animation) {
        this.animation = animation;
        this.sounds = sounds;
        this.vibrator = vibrator;
    }

    @Override
    public void startExplosion(IAlien alien) {
        explosionTime = 0f;
        sounds.play(SoundEffect.EXPLOSION);
        vibrator.vibrate(VibrateTime.TINY);
    }

    @Override
    public void startExplosionSilently() {
        explosionTime = 0f;
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
