package com.danosoftware.galaxyforce.game.beans;

import com.danosoftware.galaxyforce.sound.Sound;
import com.danosoftware.galaxyforce.sprites.game.missiles.aliens.IAlienMissile;

import java.util.List;

/**
 * Alien missile bean holding the alien missiles and sound effects.
 */
public class AlienMissileBean {
    private final List<IAlienMissile> missiles;
    private final Sound soundEffect;

    public AlienMissileBean(List<IAlienMissile> missiles, Sound soundEffect) {
        this.missiles = missiles;
        this.soundEffect = soundEffect;
    }

    public List<IAlienMissile> getMissiles() {
        return missiles;
    }

    public Sound getSoundEffect() {
        return soundEffect;
    }
}
