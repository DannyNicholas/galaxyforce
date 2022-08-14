package com.danosoftware.galaxyforce.services.sound;

/**
 * Maps sound effect enums to the sound effect file name.
 */
public enum SoundEffect {

  // these sounds are used in the splash screen so must load first
  SHIELD_PULSE("shield-pulse.ogg"),
  BOOST("boost.ogg"),

  BASE_FIRE("fire.ogg"),
  ALIEN_FIRE("alienFire.ogg"),
  EXPLOSION("explosion.ogg"),
  BIG_EXPLOSION("bigExplosion.ogg"),
  POWER_UP_SPAWN("powerUpSpawn.ogg"),
  POWER_UP_COLLIDE("powerUpCollect.ogg"),
  ALIEN_SPAWN("explosion.ogg"),
  ALIEN_HIT("hit.ogg");

  // filename of sound effect
  private final String fileName;

  SoundEffect(String fileName) {
    this.fileName = fileName;
  }

  public String getFileName() {
    return fileName;
  }
}
