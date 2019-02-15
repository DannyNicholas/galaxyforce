package com.danosoftware.galaxyforce.screen;

import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.models.screens.Model;
import com.danosoftware.galaxyforce.textures.TextureMap;
import com.danosoftware.galaxyforce.textures.TextureService;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.SpriteBatcher;

public class ExitingScreen extends AbstractScreen {

    public ExitingScreen(
            Model model,
            Controller controller,
            TextureService textureService,
            TextureMap textureMap,
            GLGraphics glGraphics,
            Camera2D camera,
            SpriteBatcher batcher) {

        super(model, controller, textureService, textureMap, glGraphics, camera, batcher);
    }

    @Override
    public boolean handleBackButton() {
        // Don't handle back button and so allow application to exit
        return false;
    }
}
