package com.danosoftware.galaxyforce.sprites.game.interfaces;

/**
 * Implementations represent a flashing text instance that can periodically show
 * and hide text.
 */
public interface FlashingText
{

    /**
     * Update the state of the flashing text instance. When the state changes,
     * the flashing text instance should callback the model using the
     * flashText() method.
     * 
     * @param deltaTime
     */
    void update(float deltaTime);

}