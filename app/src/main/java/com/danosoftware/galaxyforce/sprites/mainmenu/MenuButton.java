package com.danosoftware.galaxyforce.sprites.mainmenu;

import com.danosoftware.galaxyforce.buttons.sprite_text_button.SpriteTextButton;
import com.danosoftware.galaxyforce.models.buttons.ButtonModel;
import com.danosoftware.galaxyforce.models.buttons.ButtonType;
import com.danosoftware.galaxyforce.sprites.buttons.ButtonSprite;
import com.danosoftware.galaxyforce.sprites.buttons.IButtonSprite;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.utilities.Rectangle;

/**
 * Represents a menu button with text.
 */
public class MenuButton implements SpriteTextButton {

  // reference to Text representing level number
  private final Text text;

  // reference to button's parent model
  private final ButtonModel model;

  // reference to level selector button sprite
  private final IButtonSprite buttonSprite;

  // sprites to be used for when button is up (not pressed) or down (pressed)
  private final SpriteDetails spriteButtonUp;
  private final SpriteDetails spriteButtonDown;

  // this button's type
  private final ButtonType buttonType;

  public MenuButton(
      ButtonModel model,
      int xPos,
      int yPos,
      String text,
      ButtonType buttonType,
      SpriteDetails spriteButtonUp,
      SpriteDetails spriteButtonDown) {
    this.model = model;
    this.buttonSprite = new ButtonSprite(spriteButtonUp, xPos, yPos);
    this.buttonType = buttonType;
    this.spriteButtonUp = spriteButtonUp;
    this.spriteButtonDown = spriteButtonDown;
    this.text = Text.newTextAbsolutePosition(text, xPos, yPos);
  }

  @Override
  public Rectangle getBounds() {
    return buttonSprite.getBounds();
  }

  @Override
  public void buttonUp() {
    buttonSprite.changeType(spriteButtonUp);
    model.processButton(buttonType);
  }

  @Override
  public void buttonDown() {
    buttonSprite.changeType(spriteButtonDown);
  }

  @Override
  public void buttonReleased() {
    buttonSprite.changeType(spriteButtonUp);
  }

  @Override
  public IButtonSprite getSprite() {
    return buttonSprite;
  }

  @Override
  public Text getText() {
    return text;
  }
}
