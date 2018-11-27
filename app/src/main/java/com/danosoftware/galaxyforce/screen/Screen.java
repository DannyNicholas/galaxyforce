package com.danosoftware.galaxyforce.screen;

/* Screen interface lists methods needed for screens to draw, update and manage the different types of screen in the game.*/
public interface Screen {

    /**
     * draw components of current screen
     */
    void draw(float deltaTime);

    /**
     * update model associated with current screen
     */
    void update(float deltaTime);

    /**
     * pause the components of current screen
     */
    void pause();

    /**
     * resume the components of current screen
     */
    void resume();

    /**
     * dispose of current screen
     */
    void dispose();

    /**
     * return true if back button is handled internally and we don't want
     * application to exit.
     *
     * @return true if back button handled internally
     */
    boolean handleBackButton();

}
