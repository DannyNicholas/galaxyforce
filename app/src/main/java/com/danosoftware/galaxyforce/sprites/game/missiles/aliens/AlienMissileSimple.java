package com.danosoftware.galaxyforce.sprites.game.missiles.aliens;

import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;

import static com.danosoftware.galaxyforce.utilities.OffScreenTester.offScreenBottom;

public class AlienMissileSimple extends AbstractAlienMissile {

    // distance missile can move each cycle in pixels each second
    private static final int ALIEN_MISSILE_MOVE_PIXELS = -5 * 60;

    // Sprite id
    private static final ISpriteIdentifier SPRITE = GameSpriteIdentifier.LASER_ALIEN;


    public AlienMissileSimple(int xStart, int yStart) {
        super(SPRITE, xStart, yStart);
    }

    @Override
    public void animate(float deltaTime) {

        moveYByDelta((int) (ALIEN_MISSILE_MOVE_PIXELS * deltaTime));

        // if missile is now off screen then destroy it
        if (offScreenBottom(this)) {
            destroy();
        }
    }
}
