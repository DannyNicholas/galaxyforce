package com.danosoftware.galaxyforce.screen;

import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.interfaces.Model;
import com.danosoftware.galaxyforce.textures.TextureMap;
import com.danosoftware.galaxyforce.view.Camera2D;
import com.danosoftware.galaxyforce.view.GLGraphics;
import com.danosoftware.galaxyforce.view.SpriteBatcher;

public class GameScreen extends AbstractScreen {
    // TODO - temp variables for optimisation - remove when not needed.
    // long controllerSum = 0;
    // long controllerSamples = 0;
    // long modelSum = 0;
    // long modelSamples = 0;

    public GameScreen(Model model, Controller controller, TextureMap textureMap, GLGraphics glGraphics, Camera2D camera,
                      SpriteBatcher batcher) {
        /* use superclass constructor to create screen */
        super(model, controller, textureMap, glGraphics, camera, batcher);
    }

    // @Override
    // public void update(float deltaTime)
    // {
    // /*
    // * TODO - remove temporary code below. Overrides super-class. Remove
    // * whole method.
    // */
    //
    // // long controllerStartTime = System.nanoTime();
    // // controller.update(deltaTime);
    // // long controllerDuration = System.nanoTime() - controllerStartTime;
    // // controllerSum = controllerSum + controllerDuration;
    // // controllerSamples = controllerSamples + 1;
    // // long controllerAverage = controllerSum / controllerSamples;
    // // // Log.d(TAG, "Controller average time:" + (controllerAverage / 1000)
    // // +
    // // // " micros.");
    // //
    // // long modelStartTime = System.nanoTime();
    // // model.update(deltaTime);
    // // long modelDuration = System.nanoTime() - modelStartTime;
    // // modelSum = modelSum + modelDuration;
    // // modelSamples = modelSamples + 1;
    // // long modelAverage = modelSum / modelSamples;
    // // Log.d(TAG, "Model average time:" + (modelAverage / 1000) +
    // // " micros.");
    //
    // // try
    // // {
    // // Thread.sleep(50);
    // // Log.d("SLEEP", "Sleep");
    // // }
    // // catch (InterruptedException e)
    // // {
    // // // TODO Auto-generated catch block
    // // e.printStackTrace();
    // // }
    // }
}
