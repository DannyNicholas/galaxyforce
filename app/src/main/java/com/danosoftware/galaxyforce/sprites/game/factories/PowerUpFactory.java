package com.danosoftware.galaxyforce.sprites.game.factories;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.game.beans.PowerUpBean;
import com.danosoftware.galaxyforce.services.sound.SoundEffect;
import com.danosoftware.galaxyforce.sprites.game.powerups.IPowerUp;
import com.danosoftware.galaxyforce.sprites.game.powerups.PowerUp;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

public class PowerUpFactory {

    /**
     * Return any random power-up.
     */
    public static PowerUpBean generateRandomPowerUp(int x, int y) {
        // create array of possible power-ups
        PowerUpType[] powerUps = PowerUpType.values();

        // use method requiring a power-up type array to generate powerup
        return generateRandomPowerUp(x, y, powerUps);
    }

    /**
     * Return a random power-up from the supplied array of power-up types.
     */
    public static PowerUpBean generateRandomPowerUp(int x, int y, PowerUpType... powerUps) {
        // select a random powerup type and generate the power-up
        int index = (int) (Math.random() * powerUps.length);

        return newPowerUp(powerUps[index], x, y);
    }

    /**
     * Return a power-up using the supplied power-up type.
     */
    public static PowerUpBean newPowerUp(PowerUpType powerUpType, int x, int y) {

        final ISpriteIdentifier spriteId;

        switch (powerUpType) {
            case ENERGY:
                spriteId = GameSpriteIdentifier.POWERUP_BATTERY;
                break;
            case HELPER_BASES:
                spriteId = GameSpriteIdentifier.POWERUP_HELPER_BASES;
                break;
            case LIFE:
                spriteId = GameSpriteIdentifier.POWERUP_LIFE;
                break;
            case MISSILE_BLAST:
                spriteId = GameSpriteIdentifier.POWERUP_MISSILE_BLAST;
                break;
            case MISSILE_FAST:
                spriteId = GameSpriteIdentifier.POWERUP_MISSILE_FAST;
                break;
            case MISSILE_GUIDED:
                spriteId = GameSpriteIdentifier.POWERUP_MISSILE_GUIDED;
                break;
            case MISSILE_LASER:
                spriteId = GameSpriteIdentifier.POWERUP_MISSILE_LASER;
                break;
            case MISSILE_PARALLEL:
                spriteId = GameSpriteIdentifier.POWERUP_MISSILE_PARALLEL;
                break;
            case MISSILE_SPRAY:
                spriteId = GameSpriteIdentifier.POWERUP_MISSILE_SPRAY;
                break;
            case SHIELD:
                spriteId = GameSpriteIdentifier.POWERUP_SHIELD;
                break;
            default:
                throw new IllegalArgumentException("Unsupported PowerUpType: '" + powerUpType + "'.");
        }

        IPowerUp powerUp = new PowerUp(spriteId, x, y, powerUpType);

        return new PowerUpBean(powerUp, SoundEffect.POWER_UP_SPAWN);
    }
}
