package com.danosoftware.galaxyforce.sprites.game.assets;

import android.util.Log;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.sprites.common.AbstractSprite;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.utilities.Reversed;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Flag extends AbstractSprite {

  /* logger tag */
  private static final String TAG = "Flag";

  // flags height and width
  private static final int FLAGS_HEIGHT = 32;
  private static final int FLAGS_WIDTH = 32;

  // start x,y position of flags
  private static final int FLAGS_START_X = (FLAGS_WIDTH / 2);
  private static final int FLAGS_START_Y = GameConstants.GAME_HEIGHT - (FLAGS_HEIGHT / 2);

  // constants for each supported flag
  private static final SpriteDetails FLAG_50 = SpriteDetails.FLAG_50;
  private static final SpriteDetails FLAG_10 = SpriteDetails.FLAG_10;
  private static final SpriteDetails FLAG_5 = SpriteDetails.FLAG_5;
  private static final SpriteDetails FLAG_1 = SpriteDetails.FLAG_1;

  // static map of level values to level flags
  private static final Map<Integer, SpriteDetails> flagMap = new HashMap<>();

  static {
    flagMap.put(50, Flag.FLAG_50);
    flagMap.put(10, Flag.FLAG_10);
    flagMap.put(5, Flag.FLAG_5);
    flagMap.put(1, Flag.FLAG_1);
  }

  private Flag(SpriteDetails spriteId, int x, int y) {
    super(spriteId, x, y);
  }

  /**
   * Creates a map of used to illustrate the level using a set of flags. The map key is the flag
   * number and the map value contains the number of flags needed of this type.
   * <p>
   * Flags used are 100, 50, 10, 5 and 1.
   * <p>
   * Example: Level 276 would be represented by 2 x 100 flags, 1 x 50 flag, 2 x 10 flags, 1 x 5 flag
   * and 1 x 1 flag.
   */
  public static List<Flag> getFlagList(int levelNumber) {
    List<Flag> flags = new ArrayList<>();

    int remainder = levelNumber;
    int flagXPosition = FLAGS_START_X;

    // get keys as sorted list so it can be iterated through in reverse
    // order
    List<Integer> flagKeysAsList = new ArrayList<>(flagMap.keySet());
    Collections.sort(flagKeysAsList);

    // calculate how many flags of each flag type are needed
    for (int flag : Reversed.reversed(flagKeysAsList)) {
      int numberOfFlags = remainder / flag;
      remainder = remainder - (numberOfFlags * flag);

      Log.d(TAG, "Flag: " + flag + ". Number Of: " + numberOfFlags);

      SpriteDetails flagSprite = flagMap.get(flag);

      // add the number of flags needed to the list
      for (int i = 0; i < numberOfFlags; i++) {
        flags.add(new Flag(flagSprite, flagXPosition, FLAGS_START_Y));
        flagXPosition += FLAGS_WIDTH;
      }
    }

    return flags;
  }
}
