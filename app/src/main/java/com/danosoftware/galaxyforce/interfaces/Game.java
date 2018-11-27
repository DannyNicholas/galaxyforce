package com.danosoftware.galaxyforce.interfaces;

import android.content.Context;

import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.screen.Screen;
import com.danosoftware.galaxyforce.view.GLGraphics;

public interface Game {
    void start();

    void resume();

    void pause();

    void dispose();

    void draw(float deltaTime);

    void update(float deltaTime);

    GLGraphics getGlGraphics();

    Screen getScreen();

    FileIO getFileIO();

    void setScreen(Screen gameScreen);

    /**
     * Change to a screen that will eventually return back to this screen. A
     * subsequent call to screenReturn() will return back to the the previous
     * screen.
     */
    void setReturningScreen(Screen gameScreen);

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

    Audio getAudio();

    Context getContext();

    IBillingService getBillingService();
}