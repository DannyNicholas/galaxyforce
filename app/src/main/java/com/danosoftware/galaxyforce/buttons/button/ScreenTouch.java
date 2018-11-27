package com.danosoftware.galaxyforce.buttons.button;

import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.models.touch_screen.TouchScreenModel;
import com.danosoftware.galaxyforce.utilities.Rectangle;

/**
 * Represents a screen touch. Registers a new button that covers the whole
 * screen. No sprite associated with screen - whole screen acts as button.
 */
public class ScreenTouch implements Button {

    // reference to button's parent model
    private final TouchScreenModel model;

    // reference to button bounds that covers the entire screen
    private final Rectangle bounds;

    public ScreenTouch(TouchScreenModel model) {
        this.model = model;
        this.bounds = new Rectangle(0, 0, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void buttonUp() {
        // call screen touched when screen button pressed
        model.screenTouched();
    }

    @Override
    public void buttonDown() {
        // no action when screen pressed
    }

    @Override
    public void buttonReleased() {
        // no action when screen released (not registered as button press)
    }
}
