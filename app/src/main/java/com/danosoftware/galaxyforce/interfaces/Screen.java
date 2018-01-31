package com.danosoftware.galaxyforce.interfaces;

/* Screen interface lists methods needed for screens to draw, update and manage the different types of screen in the game.*/
public interface Screen
{

    /**
     * draw components of current screen
     */
    public void draw(float deltaTime);

    /** update model associated with current screen */
    public void update(float deltaTime);

    /** pause the components of current screen */
    public void pause();

    /** resume the components of current screen */
    public void resume();

    /** dispose of current screen */
    public void dispose();

    /**
     * return true if back button is handled internally and we don't want
     * application to exit.
     * 
     * @return true if back button handled internally
     */
    public boolean handleBackButton();

}
