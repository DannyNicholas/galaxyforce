package com.danosoftware.galaxyforce.utilities;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_HEIGHT;
import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;

import com.danosoftware.galaxyforce.sprites.common.ISprite;

/**
 * Utility methods used to test if a sprite is currently off-screen
 */
public class OffScreenTester {

  /**
   * Tests if the sprite is completely off-screen by testing it's position/dimensions against all
   * screen edges.
   */
  public static boolean offScreenAnySide(ISprite sprite) {
    return (offScreenTop(sprite)
        || offScreenBottom(sprite)
        || offScreenLeft(sprite)
        || offScreenRight(sprite));
  }

  /**
   * Tests if the sprite is off top of screen by testing it's position/dimensions against the top
   * edge.
   */
  public static boolean offScreenTop(ISprite sprite) {
    return (sprite.y() >= (GAME_HEIGHT + sprite.halfHeight()));
  }

  /**
   * Tests if the sprite is off bottom of screen by testing it's position/dimensions against the
   * bottom edge.
   */
  public static boolean offScreenBottom(ISprite sprite) {
    return (sprite.y() <= -sprite.halfHeight());
  }

  /**
   * Tests if the sprite is off left of screen by testing it's position/dimensions against the left
   * edge.
   */
  public static boolean offScreenLeft(ISprite sprite) {
    return (sprite.x() <= -sprite.halfWidth());
  }

  /**
   * Tests if the sprite is off right of screen by testing it's position/dimensions against the
   * right edge.
   */
  public static boolean offScreenRight(ISprite sprite) {
    return (sprite.x() >= (GAME_WIDTH + sprite.halfWidth()));
  }

  /**
   * Is alien off-screen and continuing to move off-screen? Will return false if alien is off-screen
   * but travelling in a direction that will bring it on-screen soon.
   */
  public static boolean isTravellingOffScreen(ISprite sprite, float xDelta, float yDelta) {
    return
        (offScreenLeft(sprite) && xDelta < 0) ||
            (offScreenRight(sprite) && xDelta > 0) ||
            (offScreenBottom(sprite) && yDelta < 0) ||
            (offScreenTop(sprite) && yDelta > 0);
  }
}
