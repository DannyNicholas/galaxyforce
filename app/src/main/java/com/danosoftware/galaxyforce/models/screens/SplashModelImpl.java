package com.danosoftware.galaxyforce.models.screens;

import com.danosoftware.galaxyforce.buttons.button.Button;
import com.danosoftware.galaxyforce.buttons.button.ScreenTouch;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controllers.common.Controller;
import com.danosoftware.galaxyforce.controllers.touch.DetectButtonTouch;
import com.danosoftware.galaxyforce.games.Game;
import com.danosoftware.galaxyforce.models.buttons.TouchScreenModel;
import com.danosoftware.galaxyforce.screen.enums.ScreenType;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SplashSprite;
import com.danosoftware.galaxyforce.sprites.properties.SplashSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SplashModelImpl implements Model, TouchScreenModel {

    private final Game game;

    private final List<Text> text;
    private final List<ISprite> sprites;

    // how long splash screen has been displayed for so far (in seconds)
    private float splashScreenTime;

    // how long splash screen should be displayed for (in seconds)
    private static final float SPLASH_SCREEN_WAIT = 4f;

    public SplashModelImpl(Game game, Controller controller) {

        this.game = game;
        this.sprites = new ArrayList<>();
        this.text = new ArrayList<>();
        this.splashScreenTime = 0f;

        sprites.add(new SplashSprite(
                GameConstants.SCREEN_MID_X,
                GameConstants.SCREEN_MID_Y,
                SplashSpriteIdentifier.SPLASH_SCREEN));

        // add button that covers the entire screen
        Button screenTouch = new ScreenTouch(this);
        controller.addTouchController(new DetectButtonTouch(screenTouch));
    }

    @Override
    public List<ISprite> getSprites() {
        return sprites;
    }

    @Override
    public List<Text> getText() {
        return text;
    }

    @Override
    public void update(float deltaTime) {
        // increment splash screen time count by deltaTime
        splashScreenTime = splashScreenTime + deltaTime;

        // if splash screen has been shown for required time, move to main menu
        if (splashScreenTime >= SPLASH_SCREEN_WAIT) {
            game.changeToScreen(ScreenType.MAIN_MENU);
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void screenTouched() {
        // if screen pressed, then go to main menu
        game.changeToScreen(ScreenType.MAIN_MENU);
    }

    @Override
    public void goBack() {
        // No action. Splash screen does not change back button behaviour.
    }

    @Override
    public void resume() {
        // no action for this model
    }

    @Override
    public void pause() {
        // no action for this model
    }
}
