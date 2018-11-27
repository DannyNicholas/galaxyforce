package com.danosoftware.galaxyforce.models.about;

import com.danosoftware.galaxyforce.models.Model;
import com.danosoftware.galaxyforce.models.screens.ButtonType;

public interface AboutModel extends Model {

    /**
     * Process a selected menu option using the supplied button type.
     */
    void processButton(ButtonType buttonType);
}
