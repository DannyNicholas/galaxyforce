package com.danosoftware.galaxyforce.sprites.game.missiles.bases;

import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenTop;

public class BaseMissileFast extends AbstractBaseMissile {

    /* missile sprite */
    private static final ISpriteIdentifier SPRITE = GameSpriteIdentifier.LASER_BASE;

    /* distance missile can move per cycle */
    private static final int BASE_MISSILE_MOVE_PIXELS = 7 * 60;

    /* how much energy will be lost by alien when this missile hits it */
    private static final int HIT_ENERGY = 1;

    public BaseMissileFast(int xStart, int yStart) {
        super(SPRITE, xStart, yStart, HIT_ENERGY);
    }

    @Override
    public void animate(float deltaTime) {
        moveYByDelta((int) (BASE_MISSILE_MOVE_PIXELS * deltaTime));

        // if missile is now off screen then destroy it
        if (offScreenTop(this)) {
            destroy();
        }
    }
}
