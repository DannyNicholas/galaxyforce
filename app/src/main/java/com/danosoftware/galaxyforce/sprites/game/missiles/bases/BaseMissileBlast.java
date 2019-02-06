package com.danosoftware.galaxyforce.sprites.game.missiles.bases;

import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenAnySide;

public class BaseMissileBlast extends AbstractBaseMissile {

    /* missile sprite */
    private static final ISpriteIdentifier SPRITE = GameSpriteIdentifier.LASER_BASE;

    /* distance missile can move in pixels each second */
    private static final int BASE_MISSILE_MOVE_PIXELS = 7 * 60;

    /* how much energy will be lost by alien when this missile hits it */
    private static final int HIT_ENERGY = 2;

    /* offset applied to x and y every move */
    private final int xDelta;
    private final int yDelta;

    public BaseMissileBlast(int xStart, int yStart, float angle) {
        super(SPRITE, xStart, yStart, HIT_ENERGY);

        // convert angle to degrees for sprite rotation.
        // needs to be adjusted by 90 deg for correct rotation.
        rotate((int) ((angle - Math.PI / 2f) * (180f / Math.PI)));

        // calculate the deltas to be applied each move
        this.xDelta = (int) (BASE_MISSILE_MOVE_PIXELS * (float) Math.cos(angle));
        this.yDelta = (int) (BASE_MISSILE_MOVE_PIXELS * (float) Math.sin(angle));
    }

    @Override
    public void animate(float deltaTime) {
        // move missile by calculated deltas
        moveByDelta(
                (int) (xDelta * deltaTime),
                (int) (yDelta * deltaTime));

        // if missile is now off screen then destroy it
        if (offScreenAnySide(this)) {
            destroy();
        }
    }
}
