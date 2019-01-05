package com.danosoftware.galaxyforce.sprites.game.interfaces;

import com.danosoftware.galaxyforce.text.Text;

import java.util.List;

/**
 * Implementations represent a flashing text instance that can periodically show
 * and hide text.
 */
public interface FlashingText {

    /**
     * Update the state of the flashing text.
     */
    void update(float deltaTime);

    /**
     * Return the currently visible text.
     * Can return an empty list if all text is hidden.
     */
    List<Text> text();
}