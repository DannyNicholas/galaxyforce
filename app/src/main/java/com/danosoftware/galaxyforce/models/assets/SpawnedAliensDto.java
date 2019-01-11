package com.danosoftware.galaxyforce.models.assets;

import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.game.aliens.IAlien;

import java.util.List;

/**
 * Spawned aliens DTO holding the aliens and spawning sound effect.
 */
public class SpawnedAliensDto {
    private final List<IAlien> aliens;
    private final SoundEffect soundEffect;

    public SpawnedAliensDto(List<IAlien> aliens, SoundEffect soundEffect) {
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
