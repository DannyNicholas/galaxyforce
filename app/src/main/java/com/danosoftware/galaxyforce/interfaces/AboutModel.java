package com.danosoftware.galaxyforce.interfaces;

import com.danosoftware.galaxyforce.model.screens.ButtonType;

public interface AboutModel extends Model {

    /**
     * Process a selected menu option using the supplied button type.
     */
    void processButton(ButtonType buttonType);
}
