package com.danosoftware.galaxyforce.screen;

import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.models.screens.Model;
import com.danosoftware.galaxyforce.services.file.FileIO;
import com.danosoftware.galaxyforce.textures.TextureMap;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.SpriteBatcher;

public class ExitingScreen extends AbstractScreen {

    public ExitingScreen(
            Model model,
            Controller controller,
            TextureMap textureMap,
            GLGraphics glGraphics,
            FileIO fileIO,
            Camera2D camera,
            SpriteBatcher batcher) {

        super(model, controller, textureMap, glGraphics, fileIO, camera, batcher);
    }

    @Override
    public boolean handleBackButton() {
        // Don't handle back button and so allow application to exit
        return false;
    }
}
