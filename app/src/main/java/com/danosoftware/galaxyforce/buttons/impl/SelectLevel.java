package com.danosoftware.galaxyforce.buttons.impl;

import com.danosoftware.galaxyforce.buttons.interfaces.SpriteTextButton;
import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.controller.utilities.DetectButtonTouch;
import com.danosoftware.galaxyforce.model.screens.SelectLevelModel;
import com.danosoftware.galaxyforce.sprites.properties.ISpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ButtonSprite;
import com.danosoftware.galaxyforce.sprites.refactor.IButtonSprite;
import com.danosoftware.galaxyforce.text.Text;
import com.danosoftware.galaxyforce.utilities.Rectangle;

/**
 * Represents a level selector. Level selector has a border, level number and
 * position.
 */
public class SelectLevel implements SpriteTextButton
{
    // enum used to determine if button is locked
    public enum LockStatus
    {
        UNLOCKED, LOCKED
    }

    // possible button sprites
    private static ISpriteIdentifier levelButton = MenuSpriteIdentifier.LEVEL_FRAME;
    private static ISpriteIdentifier levelButtonPressed = MenuSpriteIdentifier.LEVEL_FRAME_PRESSED;
    private static ISpriteIdentifier lockedButton = MenuSpriteIdentifier.LEVEL_FRAME_LOCKED;
    private static ISpriteIdentifier lockedButtonPressed = MenuSpriteIdentifier.LEVEL_FRAME_LOCKED_PRESSED;

    // reference to Text representing level number
    private final Text text;

    // reference to button's parent model
    private final SelectLevelModel model;

    // reference to level selector button sprite
    private final IButtonSprite levelSprite;

    // reference to button's level number
    private final static int NO_LEVEL = 0;
    private final int levelNumber;

    // lock status of this button
    private final LockStatus lockStatus;

    // sprites to be used for when button is up (not pressed) or down (pressed)
    private final ISpriteIdentifier spriteButtonUp;
    private final ISpriteIdentifier spriteButtonDown;

    public SelectLevel(SelectLevelModel model, Controller controller, int xPos, int yPos, int levelInt, LockStatus lockStatus)
    {
        this.model = model;
        this.lockStatus = lockStatus;

        if (lockStatus == LockStatus.UNLOCKED)
        {
            // unlocked button
            this.spriteButtonUp = levelButton;
            this.spriteButtonDown = levelButtonPressed;
            this.levelSprite = new ButtonSprite(spriteButtonUp, xPos, yPos);
            this.text = Text.newTextAbsolutePosition(Integer.toString(levelInt), xPos, yPos);
            this.levelNumber = levelInt;
        }
        else
        {
            // locked button
            this.spriteButtonUp = lockedButton;
            this.spriteButtonDown = lockedButtonPressed;
            this.levelSprite = new ButtonSprite(spriteButtonUp, xPos, yPos);
            this.text = Text.newTextAbsolutePosition("", xPos, yPos);
            this.levelNumber = NO_LEVEL;
        }

        // add a new menu button to controller's list of touch controllers
        controller.addTouchController(new DetectButtonTouch(this));

    }

    @Override
    public Rectangle getBounds()
    {
        return levelSprite.getBounds();
    }

    @Override
    public void buttonUp()
    {
        levelSprite.changeType(spriteButtonUp);

        if (lockStatus == LockStatus.UNLOCKED)
        {
            model.setLevel(levelNumber);
        }
    }

    @Override
    public void buttonDown()
    {

        levelSprite.changeType(spriteButtonDown);
    }

    @Override
    public void buttonReleased()
    {
        levelSprite.changeType(spriteButtonUp);
    }

    @Override
    public IButtonSprite getSprite()
    {
        return levelSprite;
    }

    @Override
    public Text getText()
    {
        return text;
    }
}
