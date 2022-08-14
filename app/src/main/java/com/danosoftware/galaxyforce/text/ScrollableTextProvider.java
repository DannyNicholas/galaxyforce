package com.danosoftware.galaxyforce.text;

import static com.danosoftware.galaxyforce.constants.GameConstants.GAME_WIDTH;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A text provider that only provides on-screen text when the screen can be scrolled left-or-right.
 */
public class ScrollableTextProvider extends TextProvider {

  private final static int HALF_FONT_GLYPHS_WIDTH = 30 / 2;

  // on-screen characters to draw
  private final List<Character> onScreenCharacters;
  private int onScreenCharactersCount;
  private boolean refreshOnScreenCharacters;
  private float scrollPosition;

  public ScrollableTextProvider() {
    super();
    this.onScreenCharacters = new ArrayList<>();
    this.onScreenCharactersCount = 0;
    this.refreshOnScreenCharacters = false;
    this.scrollPosition = -1;
  }

  @Override
  public void clear() {
    super.clear();
    onScreenCharacters.clear();
    refreshOnScreenCharacters = true;
  }

  public void updateScrollPosition(float newScrollPosition) {
    if (scrollPosition != newScrollPosition) {
      scrollPosition = newScrollPosition;
      refreshOnScreenCharacters = true;
    }
  }

  // how many characters currently on-screen?
  @Override
  public int count() {
    if (refreshOnScreenCharacters) {
      refreshOnScreenCharacters(scrollPosition);
    }
    return onScreenCharactersCount;
  }

  // list of characters currently on-screen
  @Override
  public List<Character> characters() {
    if (refreshOnScreenCharacters) {
      refreshOnScreenCharacters(scrollPosition);
    }
    return onScreenCharacters;
  }

  @Override
  public void add(Text addText) {
    super.add(addText);
    refreshOnScreenCharacters = true;
  }

  @Override
  public void addAll(Collection<Text> addTexts) {
    super.addAll(addTexts);
    refreshOnScreenCharacters = true;
  }

  // refresh the on-screen characters based on the current scroll position
  private void refreshOnScreenCharacters(float minX) {
    onScreenCharacters.clear();

    // calculate min and max X bounds for current scroll-position.
    // a character will be displayed if it falls within these bounds.
    float maxX = minX + GAME_WIDTH;

    // find all characters to show on-screen
    for (Character character : super.characters()) {
      float leftX = character.getX() - HALF_FONT_GLYPHS_WIDTH;
      float rightX = character.getX() + HALF_FONT_GLYPHS_WIDTH;
      // add to on-screen characters if any part of character is within bounds
      if ((leftX >= minX && leftX <= maxX) || (rightX >= minX && rightX <= maxX)) {
        onScreenCharacters.add(character);
      }
    }
    onScreenCharactersCount = onScreenCharacters.size();
    refreshOnScreenCharacters = false;
  }
}
