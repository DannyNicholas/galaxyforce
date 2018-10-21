package com.danosoftware.galaxyforce.sprites.game.interfaces;

import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.AbstractSprite;

/*
 * Sprite model for the joystick point.
 */
public class JoystickPoint extends AbstractSprite {

    public JoystickPoint(
            int x,
            int y,
            ISpriteIdentifier spriteId) {

        super(
                spriteId,
                x,
                y);
    }

}
