package com.danosoftware.galaxyforce.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Provides text to be drawn to the screen.
 * <p>
 * Initially starts as empty. Text to be drawn can be added.
 * <p>
 * Reports total characters cached and any changes since text was last retrieved.
 */
public class TextProvider {

  private final List<Text> text;
  // cached characters to draw
  private final List<Character> characters;
  // font used to create characters - may be null initially
  private Font font;
  private int charactersCount;
  private boolean updatedSinceLastRetrieve;

  public TextProvider() {
    this.text = new ArrayList<>();
    this.characters = new ArrayList<>();
    this.charactersCount = 0;
    this.updatedSinceLastRetrieve = false;
  }

  public void updateFont(Font font) {
    this.font = font;
  }

  // empties the contents of any cached text
  public void clear() {
    text.clear();
    characters.clear();
    charactersCount = 0;
    updatedSinceLastRetrieve = true;
  }

  // count all stored characters in the cached text
  public int count() {
    // do we need to re-build the characters list?
    if (updatedSinceLastRetrieve) {
      rebuildCharacters();
    }
    return charactersCount;
  }

  // return all cached text
  public List<Text> text() {
    return text;
  }

  // return all stored characters in the cached text
  public List<Character> characters() {
    // do we need to re-build the characters list?
    if (updatedSinceLastRetrieve) {
      rebuildCharacters();
    }
    return characters;
  }

  // add a single text item
  // synchronised since can be called from task thread and main thread
  public synchronized void add(Text addText) {
    text.add(addText);
    this.updatedSinceLastRetrieve = true;
  }

  // add a collection of text
  public synchronized void addAll(Collection<Text> addTexts) {
    for (Text text : addTexts) {
      add(text);
    }
  }

  private void rebuildCharacters() {
    characters.clear();
    charactersCount = 0;

    if (font == null) {
      return;
    }

    for (Text eachText : text) {
      font.updateCharacters(
          characters,
          eachText.getText(),
          eachText.getX(),
          eachText.getY(),
          eachText.getTextPositionX(),
          eachText.getTextPositionY());
    }
    charactersCount = characters.size();
    this.updatedSinceLastRetrieve = false;
  }
}
