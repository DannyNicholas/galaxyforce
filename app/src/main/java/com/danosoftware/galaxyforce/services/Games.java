package com.danosoftware.galaxyforce.services;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.danosoftware.galaxyforce.billing.service.IBillingService;
import com.danosoftware.galaxyforce.games.GameImpl;
import com.danosoftware.galaxyforce.interfaces.Game;
import com.danosoftware.galaxyforce.view.GLGraphics;

public class Games {

    // private constructor
    private Games() {
    }

    // static reference to game
    private static Game game = null;

    // return the map implementation
    public static Game getGame() {
        return game;
    }

    // static factory to create new game instance
    public static void newGame(Context context, GLGraphics glGraphics, GLSurfaceView glView, IBillingService billingService) {
        if (context == null) {
            throw new IllegalArgumentException("Supplied Context object can not be null.");
        }
        if (glGraphics == null) {
            throw new IllegalArgumentException("Supplied GLGraphics object can not be null.");
        }
        if (glView == null) {
            throw new IllegalArgumentException("Supplied GLSurfaceView object can not be null.");
        }
        if (billingService == null) {
            throw new IllegalArgumentException("Supplied IBillingService object can not be null.");
        }

        game = new GameImpl(context, glGraphics, glView, billingService);
    }

}
