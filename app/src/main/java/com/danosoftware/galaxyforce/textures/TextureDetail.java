package com.danosoftware.galaxyforce.textures;

import android.util.Log;
import com.danosoftware.galaxyforce.constants.GameConstants;

/**
 * A TextureDetail property describes a texture region (representing a sprite) within a texture
 * map.
 * <p>
 * The properties describe the name of the texture region, position within the texture map and
 * dimensions.
 */
public class TextureDetail {

  private final String name;
  private final int xPos;
  private final int yPos;
  private final int width;
  private final int height;

  public TextureDetail(String name, String xPos, String yPos, String width, String height) {
    this.name = name;
    this.xPos = convertNumeric(xPos);
    this.yPos = convertNumeric(yPos);
    this.width = convertNumeric(width);
    this.height = convertNumeric(height);
  }

  // converts string to int
  // returns 0 if NumberFormatException is thrown
  private static int convertNumeric(String str) {
    int num;

    try {
      num = Integer.parseInt(str);
    } catch (NumberFormatException nfe) {
      Log.e(GameConstants.LOG_TAG,
          "Unable to convert Texture Region value '" + str + "' to a numeric value.", nfe);
      return 0;
    }

    return num;
  }

  public String getName() {
    return name;
  }

  public int getXPos() {
    return xPos;
  }

  public int getYPos() {
    return yPos;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
