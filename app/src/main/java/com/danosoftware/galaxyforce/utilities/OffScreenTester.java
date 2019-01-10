package com.danosoftware.galaxyforce.utilities;

import com.danosoftware.galaxyforce.sprites.common.ISprite;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;

/**
 * Utility methods used to test if a sprite is currently off-screen
 */
public class OffScreenTester {

    /**
     * Tests if the sprite is completely off-screen by testing it's
     * position/dimensions against all screen edges.
     */
    public static boolean offScreenAnySide(ISprite sprite) {
        return (sprite.y() >= GAME_HEIGHT + sprite.halfHeight()
                || sprite.y() <= 0 - sprite.halfHeight()
                || sprite.x() >= GAME_WIDTH + sprite.halfWidth()
                || sprite.x() <= 0 - sprite.halfWidth());
    }

    /**
     * Tests if the sprite is off top of screen by testing it's
     * position/dimensions against the top edge.
     */
    public static boolean offScreenTop(ISprite sprite) {
        return (sprite.y() >= (GAME_HEIGHT + sprite.halfHeight()));
    }

    /**
     * Tests if the sprite is off bottom of screen by testing it's
     * position/dimensions against the bottom edge.
     */
    public static boolean offScreenBottom(ISprite sprite) {
        return (sprite.y() <= 0 - sprite.halfHeight());
    }
}
