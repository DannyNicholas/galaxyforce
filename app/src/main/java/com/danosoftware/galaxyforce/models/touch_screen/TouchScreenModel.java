package com.danosoftware.galaxyforce.models.touch_screen;


import com.danosoftware.galaxyforce.models.Model;

public interface TouchScreenModel extends Model {

    /**
     * Called when screen touched by user.
     */
    void screenTouched();
}
