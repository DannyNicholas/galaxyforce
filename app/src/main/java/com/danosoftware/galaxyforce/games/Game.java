package com.danosoftware.galaxyforce.games;

import com.danosoftware.galaxyforce.models.screens.background.RgbColour;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.sprites.common.ISprite;

import java.util.List;

public interface Game {
    void start();

    void resume();

    void pause();

    void dispose();

    void draw(float deltaTime);

    void update(float deltaTime);

    void changeToScreen(ScreenType gameScreen);

    void changeToGameScreen(int wave);

    void changeToGamePausedScreen(List<ISprite> pausedSprites, RgbColour backgroundColour);

    void changeToGameOverScreen(int previousWave);

    /**
     * Change to a screen that will eventually return back to this screen. A
     * subsequent call to screenReturn() will return back to the the previous
     * screen.
     */
    void changeToReturningScreen(ScreenType gameScreen);

    /**
     * Change back to previous screen. That is the screen that we were on when
     * changeToReturningScreen() was called.
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