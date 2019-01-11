package com.danosoftware.galaxyforce.models.assets;

import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.game.missiles.bases.IBaseMissile;

import java.util.List;

/**
 * Base missile DTO holding the base missiles and sound effects.
 */
public class BaseMissilesDto {
    private final List<IBaseMissile> missiles;
    private final SoundEffect soundEffect;

    public BaseMissilesDto(List<IBaseMissile> missiles, SoundEffect soundEffect) {
        this.missiles = missiles;
        this.soundEffect = soundEffect;
    }

    public List<IBaseMissile> getMissiles() {
        return missiles;
    }

    public SoundEffect getSoundEffect() {
        return soundEffect;
    }
}
