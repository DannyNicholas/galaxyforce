package com.danosoftware.galaxyforce.buttons.interfaces;

import com.danosoftware.galaxyforce.text.Text;

/**
 * Button that has a text label.
 */
public interface SpriteTextButton extends SpriteButton {

    /**
     * Get the button's text label (if any).
     *
     * @return the text object of the button's label
     */
    Text getText();

}
