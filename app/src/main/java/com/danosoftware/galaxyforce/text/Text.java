package com.danosoftware.galaxyforce.text;

import com.danosoftware.galaxyforce.constants.GameConstants;

/* used to store any game text. contains a string of the actual text and the x, y position */
public class Text {

  /*
   * instance variables for text's x,y position - can be null if text position
   * enums are used
   */
  private final int x;
  private final int y;

  /* reference to X enum position (e.g. LEFT) */
  private final TextPositionX textPositionX;

  /* reference to Y enum position (e.g. BOTTOM) */
  private final TextPositionY textPositionY;

  /* reference to text String */
  private final String text;

  // private constructor as static factories are used
  private Text(
      String text,
      int x,
      int y,
      TextPositionX textPositionX,
      TextPositionY textPositionY,
      boolean trustedText) {
    this.text = trustedText ? text : removeInvalidCharacters(text);
    this.x = x;
    this.y = y;
    this.textPositionX = textPositionX;
    this.textPositionY = textPositionY;
  }

  // static factory for text in absolute position
  public static Text newTextAbsolutePosition(
      String text,
      int x,
      int y) {
    return new Text(text, x, y, null, null, true);
  }

  // static factory for text using relative position enum
  public static Text newTextRelativePositionBoth(
      String text,
      TextPositionX textPositionX,
      TextPositionY textPositionY) {
    return new Text(text, 0, 0, textPositionX, textPositionY, true);
  }

  // static factory for text using relative position enum
  public static Text newTextRelativePositionX(
      String text,
      TextPositionX textPositionX,
      int y) {
    return new Text(text, 0, y, textPositionX, null, true);
  }

  // static factory for untrusted text using relative position enum.
  // untrusted text is normally used when we don't know the text content at compile time.
  // e.g. price of upgrade or game version number.
  // in these cases, we want to remove any unsupported characters from the text.
  public static Text newUntrustedTextRelativePositionX(
      String text,
      TextPositionX textPositionX,
      int y) {
    return new Text(text, 0, y, textPositionX, null, false);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public String getText() {
    return text;
  }

  public TextPositionX getTextPositionX() {
    return textPositionX;
  }

  public TextPositionY getTextPositionY() {
    return textPositionY;
  }

  /**
   * Takes the original supplied text string and removes any invalid characters. In this case, this
   * will be any characters that don't exist in the font character map.
   */
  private String removeInvalidCharacters(String text) {

    final StringBuilder validStr = new StringBuilder();
    final String validCharacters = GameConstants.FONT_CHARACTER_MAP;

    final int strLength = text.length();
    for (int i = 0; i < strLength; i++) {
      char c = text.charAt(i);
      if (validCharacters.indexOf(c) != -1) {
        validStr.append(c);
      }
    }

    return validStr.toString();
  }
}
