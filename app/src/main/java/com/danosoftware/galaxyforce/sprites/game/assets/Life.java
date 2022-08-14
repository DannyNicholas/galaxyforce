package com.danosoftware.galaxyforce.sprites.game.assets;

import com.danosoftware.galaxyforce.sprites.common.AbstractSprite;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import java.util.ArrayList;
import java.util.List;

public class Life extends AbstractSprite {

  // live height and width
  private static final int LIVES_HEIGHT = 28;
  private static final int LIVES_WIDTH = 28;

  // start x,y position of lives
  private static final int LIVES_START_X = (LIVES_WIDTH / 2);
  private static final int LIVES_START_Y = (LIVES_HEIGHT / 2) + 10;

  private Life(int x, int y, SpriteDetails spriteId) {
    super(spriteId, x, y);
  }

  /**
   * Creates a list of lives.
   */
  public static List<Life> getLives(int lives) {
    List<Life> livesList = new ArrayList<>();

    int flagXPosition = LIVES_START_X;

    // add the number of energy bars needed to the list
    for (int i = 0; i < lives; i++) {
      livesList.add(new Life(flagXPosition, LIVES_START_Y, SpriteDetails.LIVES));
      flagXPosition += LIVES_WIDTH;
    }

    return livesList;
  }
}
