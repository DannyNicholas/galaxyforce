package com.danosoftware.galaxyforce.services.sound;

/**
 * Maps sound effect enums to the sound effect file name.
 */
public enum SoundEffect {

    BASE_FIRE("fire.ogg"), ALIEN_FIRE("alienFire.ogg"), EXPLOSION("explosion.ogg"), POWER_UP_SPAWN("powerUp.ogg"), POWER_UP_COLLIDE(
            "powerUp.ogg"), BASE_FLIP("explosion.ogg"), ALIEN_SPAWN("explosion.ogg"), ALIEN_HIT("explosion.ogg"), BASE_HIT("explosion.ogg");

    // filename of sound effect
    private final String fileName;

    SoundEffect(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
