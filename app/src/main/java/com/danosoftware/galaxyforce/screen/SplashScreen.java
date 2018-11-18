package com.danosoftware.galaxyforce.screen;

import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.interfaces.Model;
import com.danosoftware.galaxyforce.textures.TextureMap;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.SpriteBatcher;

public class SplashScreen extends AbstractScreen {
    public SplashScreen(Model model, Controller controller, TextureMap textureMap, GLGraphics glGraphics, Camera2D camera,
                        SpriteBatcher batcher) {
        /* use superclass constructor to create screen */
        super(model, controller, textureMap, glGraphics, camera, batcher);
    }

    @Override
    public boolean handleBackButton() {
        // Don't handle back button and so allow application to exit
        return false;
    }
}
