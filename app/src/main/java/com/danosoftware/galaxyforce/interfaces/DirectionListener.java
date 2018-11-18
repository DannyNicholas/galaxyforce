package com.danosoftware.galaxyforce.interfaces;


/**
 * Interface to be implemented by all classes that listen for direction changes.
 * This is is so they can be notified following the direction change to allow
 * any required updates.
 */
public interface DirectionListener {

    /**
     * Used to update any listeners of a direction change.
     *
     * @param direction new direction to be updated
     */
    void completeDirectionChange();

    /**
     * Used to update any listeners that a direction change is starting. Some
     * listeners may choose not to take any action.
     */
    void startDirectionChange();

}
