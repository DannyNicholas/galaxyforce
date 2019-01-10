package com.danosoftware.galaxyforce.sprites.game.powerups;

import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.sprites.common.AbstractCollidingSprite;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenBottom;

public class PowerUp extends AbstractCollidingSprite implements IPowerUp {

    // distance power-up can move per second
    private static final int POWER_UP_MOVE_PIXELS = 2 * 60;

    // power up type
    private final PowerUpType powerUpType;

    private boolean isDestroyed;

    public PowerUp(
            ISpriteIdentifier spriteId,
            int x,
            int y,
            PowerUpType powerUpType) {
        super(spriteId, x, y);
        this.isDestroyed = false;
        this.powerUpType = powerUpType;
    }

    @Override
    public PowerUpType getPowerUpType() {
        return powerUpType;
    }

    @Override
    public void animate(float deltaTime) {
        move(
                x(),
                y() - (int) (POWER_UP_MOVE_PIXELS * deltaTime));

        // if power-up is now off screen then destory it
        if (offScreenBottom(this)) {
            destroy();
        }
    }

    @Override
    public void destroy() {
        isDestroyed = true;
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }
}
