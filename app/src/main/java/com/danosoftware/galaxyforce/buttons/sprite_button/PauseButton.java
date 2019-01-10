package com.danosoftware.galaxyforce.buttons.sprite_button;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.models.screens.game.GameModel;
import com.danosoftware.galaxyforce.sprites.buttons.ButtonSprite;
import com.danosoftware.galaxyforce.sprites.buttons.IButtonSprite;
import com.danosoftware.galaxyforce.utilities.Rectangle;

import static com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier.PAUSE_BUTTON_DOWN;
import static com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier.PAUSE_BUTTON_UP;

/**
 * Represents a pause button. Calls game model when pressed.
 */
public class PauseButton implements SpriteButton {

    /* Constants for the pause button's position */
    private static final int PAUSE_BUTTON_X = GameConstants.GAME_WIDTH - 50 - 10;
    private static final int PAUSE_BUTTON_Y = GameConstants.GAME_HEIGHT - 50 - 10;

    // reference to button's parent model
    private final GameModel model;

    // reference to button sprite
    private final IButtonSprite buttonSprite;

    public PauseButton(
            GameModel model) {

        this.model = model;

        // create button with a 10px border
        this.buttonSprite = new ButtonSprite(
                PAUSE_BUTTON_UP,
                PAUSE_BUTTON_X,
                PAUSE_BUTTON_Y,
                10);
    }

    @Override
    public Rectangle getBounds() {
        return buttonSprite.getBounds();
    }

    @Override
    public void buttonUp() {
        buttonSprite.changeType(PAUSE_BUTTON_UP);
        model.pause();
    }

    @Override
    public void buttonDown() {
        buttonSprite.changeType(PAUSE_BUTTON_DOWN);
    }

    @Override
    public void buttonReleased() {
        buttonSprite.changeType(PAUSE_BUTTON_UP);
    }

    @Override
    public IButtonSprite getSprite() {
        return buttonSprite;
    }
}
