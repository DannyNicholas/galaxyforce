package com.danosoftware.galaxyforce.models.assets;

import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;

/**
 * Power-up DTO holding the power-up and sound effects.
 */
public class PowerUpsDto {
    private final IPowerUp powerUp;
    private final SoundEffect soundEffect;

    public PowerUpsDto(IPowerUp powerUp, SoundEffect soundEffect) {
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
