package com.danosoftware.galaxyforce.game.beans;

import com.danosoftware.galaxyforce.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;

/**
 * Power-up bean holding the power-up and sound effects.
 */
public class PowerUpBean {
    private final IPowerUp powerUp;
    private final SoundEffect soundEffect;

    public PowerUpBean(IPowerUp powerUp, SoundEffect soundEffect) {
        this.powerUp = powerUp;
        this.soundEffect = soundEffect;
    }

    public IPowerUp getPowerUp() {
        return powerUp;
    }

    public SoundEffect getSoundEffect() {
        return soundEffect;
    }
}
