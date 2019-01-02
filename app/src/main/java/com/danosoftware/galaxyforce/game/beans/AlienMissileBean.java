package com.danosoftware.galaxyforce.game.beans;

import com.danosoftware.galaxyforce.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;

import java.util.List;

/**
 * Alien missile bean holding the alien missiles and sound effects.
 */
public class AlienMissileBean {
    private final List<IAlienMissile> missiles;
    private final SoundEffect soundEffect;

    public AlienMissileBean(List<IAlienMissile> missiles, SoundEffect soundEffect) {
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
