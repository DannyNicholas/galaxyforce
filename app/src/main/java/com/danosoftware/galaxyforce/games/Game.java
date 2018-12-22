package com.danosoftware.galaxyforce.games;

import com.danosoftware.galaxyforce.screen.enums.ScreenType;

public interface Game {
    void start();

    void resume();

    void pause();

    void dispose();

    void draw(float deltaTime);

    void update(float deltaTime);

    void setScreen(ScreenType gameScreen);

    void setGameScreen(int wave);

    /**
     * Change to a screen that will eventually return back to this screen. A
     * subsequent call to screenReturn() will return back to the the previous
     * screen.
     */
    void setReturningScreen(ScreenType gameScreen);

    /**
     * Change back to previous screen. That is the screen that we were on when
     * setReturningScreen() was called.
     */
    void screenReturn();

    /**
     * return true if back button is handled internally and we don't want
     * application to exit.
     *
     * @return true if back button handled internally
     */
    boolean handleBackButton();
}