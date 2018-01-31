package com.danosoftware.galaxyforce.sprites.mainmenu;

import com.danosoftware.galaxyforce.buttons.interfaces.SpriteTextButton;
import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.controller.utilities.DetectButtonTouch;
import com.danosoftware.galaxyforce.model.screens.ButtonType;
import com.danosoftware.galaxyforce.model.screens.MenuButtonModel;
import com.danosoftware.galaxyforce.sprites.game.interfaces.ButtonRectangle;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.utilities.Rectangle;

/**
 * Represents a level selector. Level selector has a border, level number and
 * position.
 */
public class MenuButton implements SpriteTextButton
{
    // reference to Text representing level number
    private final Text text;

    // reference to button's parent model
    private final MenuButtonModel model;

    // reference to level selector button sprite
    private final ButtonRectangle buttonSprite;

    // sprites to be used for when button is up (not pressed) or down (pressed)
    private final ISpriteIdentifier spriteButtonUp;
    private final ISpriteIdentifier spriteButtonDown;

    // this button's type
    ButtonType buttonType = null;

    public MenuButton(MenuButtonModel model, Controller controller, int xPos, int yPos, String text, ButtonType buttonType,
            ISpriteIdentifier spriteButtonUp, ISpriteIdentifier spriteButtonDown)
    {
        this.model = model;
        this.buttonSprite = new ButtonRectangle(xPos, yPos, spriteButtonUp);
        this.buttonType = buttonType;
        this.spriteButtonUp = spriteButtonUp;
        this.spriteButtonDown = spriteButtonDown;
        this.text = Text.newTextAbsolutePosition(text, xPos, yPos);

        // add a new menu button to controller's list of touch controllers
        controller.addTouchController(new DetectButtonTouch(this));
    }

    @Override
    public Rectangle getBounds()
    {
        return buttonSprite.getBounds();
    }

    @Override
    public void buttonUp()
    {
        buttonSprite.setSpriteIdentifier(spriteButtonUp);

        model.processButton(buttonType);
    }

    @Override
    public void buttonDown()
    {
        buttonSprite.setSpriteIdentifier(spriteButtonDown);
    }

    @Override
    public void buttonReleased()
    {
        buttonSprite.setSpriteIdentifier(spriteButtonUp);
    }

    @Override
    public ButtonRectangle getSprite()
    {
        return buttonSprite;
    }

    @Override
    public Text getText()
    {
        return text;
    }
}
