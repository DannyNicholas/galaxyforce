package com.danosoftware.galaxyforce.models.assets;

import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;

import java.util.List;

/**
 * Alien missile DTO holding the alien missiles and sound effects.
 */
public class AlienMissilesDto {
    private final List<IAlienMissile> missiles;
    private final SoundEffect soundEffect;

    public AlienMissilesDto(List<IAlienMissile> missiles, SoundEffect soundEffect) {
        this.missiles = missiles;
        this.soundEffect = soundEffect;
    }

    public List<IAlienMissile> getMissiles() {
        return missiles;
    }

    public SoundEffect getSoundEffect() {
        return soundEffect;
    }
}
