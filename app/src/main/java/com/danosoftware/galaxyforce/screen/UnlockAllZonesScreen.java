package com.danosoftware.galaxyforce.screen;

import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.models.Model;
import com.danosoftware.galaxyforce.textures.TextureMap;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.SpriteBatcher;

public class UnlockAllZonesScreen extends AbstractScreen {

    public UnlockAllZonesScreen(
            Model model,
            Controller controller,
            TextureMap textureMap,
            GLGraphics glGraphics,
            Camera2D camera,
            SpriteBatcher batcher) {

        super(model, controller, textureMap, glGraphics, camera, batcher);
    }
}
