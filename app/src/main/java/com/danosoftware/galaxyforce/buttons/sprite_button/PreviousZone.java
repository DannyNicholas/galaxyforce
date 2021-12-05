package com.danosoftware.galaxyforce.buttons.sprite_button;

import com.danosoftware.galaxyforce.models.screens.level.SelectLevelModel;
import com.danosoftware.galaxyforce.sprites.buttons.ButtonSprite;
import com.danosoftware.galaxyforce.sprites.buttons.IButtonSprite;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.utilities.Rectangle;

/**
 * Represents a previous zone button. Has a sprite and position.
 */
public class PreviousZone implements SpriteButton {

  // wanted buffer around the button
  private static final int BUFFER = 32;

  // reference to button's parent model
  private final SelectLevelModel model;

  // reference to button's parent zone
  private final int zone;

  // reference to level selector button sprite
  private final IButtonSprite levelSprite;

  // sprites to be used for when button is up (not pressed) or down (pressed)
  private final ISpriteIdentifier spriteButtonUp;
  private final ISpriteIdentifier spriteButtonDown;

  public PreviousZone(
      SelectLevelModel model,
      int xPos,
      int yPos,
      int zone,
      ISpriteIdentifier spriteButtonUp,
      ISpriteIdentifier spriteButtonDown) {
    this.model = model;
    this.levelSprite = new ButtonSprite(spriteButtonUp, xPos, yPos, BUFFER);
    this.spriteButtonUp = spriteButtonUp;
    this.spriteButtonDown = spriteButtonDown;
    this.zone = zone;
  }

  @Override
  public Rectangle getBounds() {
    return levelSprite.getBounds();
  }

  @Override
  public void buttonUp() {
    levelSprite.changeType(spriteButtonUp);

    model.changeZone(zone - 1);
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
}
