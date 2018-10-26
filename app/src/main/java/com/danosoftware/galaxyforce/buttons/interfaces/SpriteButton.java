package com.danosoftware.galaxyforce.buttons.interfaces;

import com.danosoftware.galaxyforce.sprites.refactor.IButtonSprite;

/**
 * Button that can be selected by a user.
 */
public interface SpriteButton extends Button
{
    /**
     * Get the button's sprite.
     * 
     * @return button's sprite
     */
    IButtonSprite getSprite();
}
