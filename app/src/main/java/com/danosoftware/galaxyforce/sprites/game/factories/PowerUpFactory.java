package com.danosoftware.galaxyforce.sprites.game.factories;

import static com.danosoftware.galaxyforce.waves.utilities.Randomiser.random;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.models.assets.PowerUpsDto;
import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.game.powerups.PowerUp;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;

public class PowerUpFactory {

  /**
   * Return any random power-up.
   */
  public static PowerUpsDto generateRandomPowerUp(float x, float y) {
    // create array of possible power-ups
    PowerUpType[] powerUps = PowerUpType.values();

    // use method requiring a power-up type array to generate powerup
    return generateRandomPowerUp(x, y, powerUps);
  }

  /**
   * Return a random power-up from the supplied array of power-up types.
   */
  public static PowerUpsDto generateRandomPowerUp(float x, float y, PowerUpType... powerUps) {
    // select a random powerup type and generate the power-up
    int index = (int) (random() * powerUps.length);

    return newPowerUp(powerUps[index], x, y);
  }

  /**
   * Return a power-up using the supplied power-up type.
   */
  public static PowerUpsDto newPowerUp(PowerUpType powerUpType, float x, float y) {

    final SpriteDetails spriteId;

    switch (powerUpType) {
      case HELPER_BASES:
        spriteId = SpriteDetails.POWERUP_HELPER_BASES;
        break;
      case LIFE:
        spriteId = SpriteDetails.POWERUP_LIFE;
        break;
      case MISSILE_BLAST:
        spriteId = SpriteDetails.POWERUP_MISSILE_BLAST;
        break;
      case MISSILE_FAST:
        spriteId = SpriteDetails.POWERUP_MISSILE_FAST;
        break;
      case MISSILE_GUIDED:
        spriteId = SpriteDetails.POWERUP_MISSILE_GUIDED;
        break;
      case MISSILE_LASER:
        spriteId = SpriteDetails.POWERUP_MISSILE_LASER;
        break;
      case MISSILE_PARALLEL:
        spriteId = SpriteDetails.POWERUP_MISSILE_PARALLEL;
        break;
      case MISSILE_SPRAY:
        spriteId = SpriteDetails.POWERUP_MISSILE_SPRAY;
        break;
      case SHIELD:
        spriteId = SpriteDetails.POWERUP_SHIELD;
        break;
      default:
        throw new IllegalArgumentException("Unsupported PowerUpType: '" + powerUpType + "'.");
    }

    IPowerUp powerUp = new PowerUp(spriteId, x, y, powerUpType);

    return new PowerUpsDto(powerUp, SoundEffect.POWER_UP_SPAWN);
  }
}
