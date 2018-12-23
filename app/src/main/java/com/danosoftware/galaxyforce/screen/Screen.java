package com.danosoftware.galaxyforce.screen;

import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.interfaces.FileIO;
import com.danosoftware.galaxyforce.models.screens.Model;
import com.danosoftware.galaxyforce.textures.TextureMap;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.SpriteBatcher;

public class Screen extends AbstractScreen {

    public Screen(
            Model model,
            Controller controller,
            TextureMap textureMap,
            GLGraphics glGraphics,
            FileIO fileIO,
            Camera2D camera,
            SpriteBatcher batcher) {

        super(model, controller, textureMap, glGraphics, fileIO, camera, batcher);
    }
}
