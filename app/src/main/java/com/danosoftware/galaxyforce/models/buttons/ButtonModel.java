package com.danosoftware.galaxyforce.models.buttons;

public interface ButtonModel {

    /**
     * Called when button is touched by user.
     */
    void processButton(ButtonType buttonType);
}
