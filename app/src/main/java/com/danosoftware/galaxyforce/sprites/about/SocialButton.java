package com.danosoftware.galaxyforce.sprites.about;

import com.danosoftware.galaxyforce.buttons.sprite_button.SpriteButton;
import com.danosoftware.galaxyforce.models.buttons.ButtonModel;
import com.danosoftware.galaxyforce.models.buttons.ButtonType;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ButtonSprite;
import com.danosoftware.galaxyforce.sprites.refactor.IButtonSprite;
import com.danosoftware.galaxyforce.utilities.Rectangle;

/**
 * Represents a level selector. Level selector has a border, level number and
 * position.
 */
public class SocialButton implements SpriteButton {

    // reference to button's parent model
    private final ButtonModel model;

    // reference to level selector button sprite
    private final IButtonSprite buttonSprite;

    // sprites to be used for when button is up (not pressed) or down (pressed)
    private final ISpriteIdentifier spriteButtonUp;
    private final ISpriteIdentifier spriteButtonDown;

    // this button's type
    private final ButtonType buttonType;

    public SocialButton(ButtonModel model, int xPos, int yPos, ButtonType buttonType,
                        ISpriteIdentifier spriteButtonUp, ISpriteIdentifier spriteButtonDown) {
        this.model = model;
        this.buttonSprite = new ButtonSprite(spriteButtonUp, xPos, yPos);
        this.buttonType = buttonType;
        this.spriteButtonUp = spriteButtonUp;
        this.spriteButtonDown = spriteButtonDown;
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
}
