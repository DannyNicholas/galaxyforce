package com.danosoftware.galaxyforce.buttons.sprite_text_button;

import com.danosoftware.galaxyforce.buttons.toggle_group.ToggleButtonGroup;
import com.danosoftware.galaxyforce.options.Option;
import com.danosoftware.galaxyforce.sprites.buttons.ButtonSprite;
import com.danosoftware.galaxyforce.sprites.buttons.IButtonSprite;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.utilities.Rectangle;

/**
 * Represents a level selector. Level selector has a border, level number and position.
 */
public class OptionButton implements SpriteTextButton {

  // reference to Text representing level number
  private final Text text;

  // reference to level selector button sprite
  private final IButtonSprite levelSprite;

  // sprites to be used for when button is up (not pressed) or down (pressed)
  private final SpriteDetails spriteButtonUp;
  private final SpriteDetails spriteButtonDown;

  // parent toggle button group
  private final ToggleButtonGroup toggleButtonGroup;

  // option type associated with button
  private final Option optionType;

  public OptionButton(
      int xPos,
      int yPos,
      Option optionType,
      SpriteDetails spriteButtonUp,
      SpriteDetails spriteButtonDown,
      ToggleButtonGroup toggleButtonGroup) {
    this.levelSprite = new ButtonSprite(spriteButtonUp, xPos, yPos);
    this.spriteButtonUp = spriteButtonUp;
    this.spriteButtonDown = spriteButtonDown;
    this.text = Text.newTextAbsolutePosition(optionType.getText(), xPos, yPos);
    this.toggleButtonGroup = toggleButtonGroup;
    this.optionType = optionType;
  }

  @Override
  public Rectangle getBounds() {
    return levelSprite.getBounds();
  }

  @Override
  public void buttonUp() {
    toggleButtonGroup.optionSelected(this, optionType);
  }

  @Override
  public void buttonDown() {
    levelSprite.changeType(spriteButtonDown);
  }

  @Override
  public void buttonReleased() {
    // only release button if current button is not the current option
    // selected. Stops the currently selected controller from being
    // accidentally
    // unselected leaving nothing selected.
    if (optionType != toggleButtonGroup.getSelectedOption()) {
      levelSprite.changeType(spriteButtonUp);
    }
  }

  @Override
  public IButtonSprite getSprite() {
    return levelSprite;
  }

  @Override
  public Text getText() {
    return text;
  }
}
