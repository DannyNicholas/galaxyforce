package com.danosoftware.galaxyforce.buttons.impl;

import com.danosoftware.galaxyforce.buttons.interfaces.Button;
import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.controller.utilities.DetectButtonTouch;
import com.danosoftware.galaxyforce.interfaces.TouchScreenModel;
import com.danosoftware.galaxyforce.utilities.Rectangle;

/**
 * Represents a screen touch. Registers a new button that covers the whole
 * screen. No sprite associated with screen - whole screen acts as button.
 */
public class ScreenTouch implements Button {

    // reference to button's parent model
    private TouchScreenModel model;

    // reference to button bounds
    private Rectangle bounds;

    public ScreenTouch(TouchScreenModel model, Controller controller, int topLeftX, int topLeftY, int width, int height) {
        this.model = model;

        // create bounds using the supplied co-ordinates, width and height
        this.bounds = new Rectangle(topLeftX, topLeftY, width, height);

        // add a new menu button to controller's list of touch controllers
        controller.addTouchController(new DetectButtonTouch(this));
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
