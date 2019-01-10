package com.danosoftware.galaxyforce.buttons.sprite_button;

import com.danosoftware.galaxyforce.buttons.button.Button;
import com.danosoftware.galaxyforce.sprites.buttons.IButtonSprite;

/**
 * Button that can be selected by a user.
 */
public interface SpriteButton extends Button {
    /**
     * Get the button's sprite.
     *
     * @return button's sprite
     */
    IButtonSprite getSprite();
}
