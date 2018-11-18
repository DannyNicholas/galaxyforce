package com.danosoftware.galaxyforce.sprites.mainmenu;

import com.danosoftware.galaxyforce.buttons.interfaces.SpriteTextButton;
import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.controller.utilities.DetectButtonTouch;
import com.danosoftware.galaxyforce.interfaces.LevelModel;
import com.danosoftware.galaxyforce.model.screens.ButtonType;
import com.danosoftware.galaxyforce.model.screens.MenuButtonModel;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ButtonSprite;
import com.danosoftware.galaxyforce.sprites.refactor.IButtonSprite;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.utilities.Rectangle;

/**
 * Represents a menu button that responds to user clicks on the select level
 * screen even if the user is swiping. This is achieved by shifting the button's
 * getBounds() by the current swipe offset.
 */
public class SwipeMenuButton implements SpriteTextButton {
    // reference to Text representing level number
    private final Text text;

    // reference to button's parent model
    private final MenuButtonModel model;

    // reference to model that allows us to determine screen swipe offset
    private final LevelModel swipeModel;

    // reference to level selector button sprite
    private final IButtonSprite buttonSprite;

    // sprites to be used for when button is up (not pressed) or down (pressed)
    private final ISpriteIdentifier spriteButtonUp;
    private final ISpriteIdentifier spriteButtonDown;

    // this button's type
    ButtonType buttonType = null;

    public SwipeMenuButton(MenuButtonModel model, LevelModel swipeModel, Controller controller, int xPos, int yPos, String text,
                           ButtonType buttonType, ISpriteIdentifier spriteButtonUp, ISpriteIdentifier spriteButtonDown) {
        this.model = model;
        this.swipeModel = swipeModel;
        this.buttonSprite = new ButtonSprite(spriteButtonUp, xPos, yPos);
        this.buttonType = buttonType;
        this.spriteButtonUp = spriteButtonUp;
        this.spriteButtonDown = spriteButtonDown;
        this.text = Text.newTextAbsolutePosition(text, xPos, yPos);

        // add a new menu button to controller's list of touch controllers
        controller.addTouchController(new DetectButtonTouch(this));
    }

    @Override
    public Rectangle getBounds() {
        // get the current swipe offset
        float swipeOffset = swipeModel.getScrollPosition();

        /*
         * Return a new rectangle that represents the button which is offset by
         * the swipe offset. We don't want to create a new button as we'll lose
         * the existing button's state.
         */
        Rectangle swipedBounds = new Rectangle(
                swipeOffset + buttonSprite.x() - buttonSprite.width() / 2,
                buttonSprite.y() - buttonSprite.height() / 2,
                buttonSprite.width(),
                buttonSprite.height());

        return swipedBounds;
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
