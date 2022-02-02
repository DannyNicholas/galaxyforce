package com.danosoftware.galaxyforce.buttons.sprite_text_button;

import com.danosoftware.galaxyforce.models.screens.level.SelectLevelModel;
import com.danosoftware.galaxyforce.sprites.buttons.ButtonSprite;
import com.danosoftware.galaxyforce.sprites.buttons.IButtonSprite;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.utilities.Rectangle;

/**
 * Represents a level selector. Level selector has a border, level number and position.
 */
public class SelectLevel implements SpriteTextButton {

  // possible button sprites
  private static final SpriteDetails levelButton = SpriteDetails.LEVEL_FRAME;
  private static final SpriteDetails levelButtonPressed = SpriteDetails.LEVEL_FRAME_PRESSED;
  private static final SpriteDetails lockedButton = SpriteDetails.LEVEL_FRAME_LOCKED;
  private static final SpriteDetails lockedButtonPressed = SpriteDetails.LEVEL_FRAME_LOCKED_PRESSED;
  // reference to button's level number
  private final static int NO_LEVEL = 0;
  // reference to Text representing level number
  private final Text text;

  // reference to button's parent model
  private final SelectLevelModel model;

  // reference to level selector button sprite
  private final IButtonSprite levelSprite;
  private final int levelNumber;
  // lock status of this button
  private final LockStatus lockStatus;
  // sprites to be used for when button is up (not pressed) or down (pressed)
  private final SpriteDetails spriteButtonUp;
  private final SpriteDetails spriteButtonDown;

  public SelectLevel(SelectLevelModel model, int xPos, int yPos, int levelInt,
      LockStatus lockStatus) {
    this.model = model;
    this.lockStatus = lockStatus;

    if (lockStatus == LockStatus.UNLOCKED) {
      // unlocked button
      this.spriteButtonUp = levelButton;
      this.spriteButtonDown = levelButtonPressed;
      this.levelSprite = new ButtonSprite(spriteButtonUp, xPos, yPos);
      this.text = Text.newTextAbsolutePosition(Integer.toString(levelInt), xPos, yPos);
      this.levelNumber = levelInt;
    } else {
      // locked button
      this.spriteButtonUp = lockedButton;
      this.spriteButtonDown = lockedButtonPressed;
      this.levelSprite = new ButtonSprite(spriteButtonUp, xPos, yPos);
      this.text = Text.newTextAbsolutePosition("", xPos, yPos);
      this.levelNumber = NO_LEVEL;
    }
  }

  @Override
  public Rectangle getBounds() {
    return levelSprite.getBounds();
  }

  @Override
  public void buttonUp() {
    levelSprite.changeType(spriteButtonUp);

    if (lockStatus == LockStatus.UNLOCKED) {
      model.setLevel(levelNumber);
    }
  }

  @Override
  public void buttonDown() {

    levelSprite.changeType(spriteButtonDown);
  }

  @Override
  public void buttonReleased() {
    levelSprite.changeType(spriteButtonUp);
  }

  @Override
  public IButtonSprite getSprite() {
    return levelSprite;
  }

  @Override
  public Text getText() {
    return text;
  }

  // enum used to determine if button is locked
  public enum LockStatus {
    UNLOCKED, LOCKED
  }
}
