package com.danosoftware.galaxyforce.sprites.game.missiles.aliens;

import com.danosoftware.galaxyforce.sprites.game.bases.IBasePrimary;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenAnySide;

/**
 * Alien missile that targets the supplied base.
 * <p>
 * The missile will calculate it's direction on construction and will continue
 * to move in that direction until off the screen.
 * <p>
 * This missile will not change direction once fired even if the targeted base
 * moves.
 *
 * @author Danny
 */
public class AlienMissileRotated extends AbstractAlienMissile {

    // how much energy will be lost by base when this missile hits it
    private static final int HIT_ENERGY = 2;

    // distance missile can move each cycle in pixels each second
    private static final int ALIEN_MISSILE_MOVE_PIXELS = 5 * 60;

    // Sprite id
    private static final ISpriteIdentifier SPRITE = GameSpriteIdentifier.LASER_ALIEN;

    // offset applied to x and y every move
    private final int xDelta;
    private final int yDelta;

    public AlienMissileRotated(int xStart, int yStart, IBasePrimary base) {
        super(SPRITE, xStart, yStart, HIT_ENERGY);

        // calculate angle from missile position to base
        final float angle;
        if (base != null) {
            angle = (float) Math.atan2(
                    base.y() - yStart,
                    base.x() - xStart);
        } else {
            // if base is null fire downwards
            angle = (float) Math.atan2(-1, 0);
        }

        // convert angle to degrees for sprite rotation.
        // needs to be adjusted by 90 deg for correct rotation.
        rotate((int) ((angle - Math.PI / 2f) * (180f / Math.PI)));

        // calculate the deltas to be applied each move
        this.xDelta = (int) (ALIEN_MISSILE_MOVE_PIXELS * (float) Math.cos(angle));
        this.yDelta = (int) (ALIEN_MISSILE_MOVE_PIXELS * (float) Math.sin(angle));
    }

    @Override
    public void animate(float deltaTime) {

        // move missile by calculated deltas
        move(
                x() + (int) (xDelta * deltaTime),
                y() + (int) (yDelta * deltaTime));

        // if missile is now off screen then destroy it
        if (offScreenAnySide(this)) {
            destroy();
        }

    }
}
