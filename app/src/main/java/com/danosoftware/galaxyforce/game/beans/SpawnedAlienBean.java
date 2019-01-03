package com.danosoftware.galaxyforce.game.beans;

import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;

import java.util.List;

/**
 * Spawned aliens bean holding the aliens and spawning sound effect.
 */
public class SpawnedAlienBean {
    private final List<IAlien> aliens;
    private final SoundEffect soundEffect;

    public SpawnedAlienBean(List<IAlien> aliens, SoundEffect soundEffect) {
        this.aliens = aliens;
        this.soundEffect = soundEffect;
    }

    public List<IAlien> getAliens() {
        return aliens;
    }

    public SoundEffect getSoundEffect() {
        return soundEffect;
    }
}
