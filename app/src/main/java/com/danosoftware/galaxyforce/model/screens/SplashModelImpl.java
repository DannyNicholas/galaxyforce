package com.danosoftware.galaxyforce.model.screens;

import com.danosoftware.galaxyforce.buttons.impl.ScreenTouch;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.controller.interfaces.Controller;
import com.danosoftware.galaxyforce.enumerations.ModelState;
import com.danosoftware.galaxyforce.interfaces.Screen;
import com.danosoftware.galaxyforce.interfaces.TouchScreenModel;
import com.danosoftware.galaxyforce.screen.ScreenFactory;
import com.danosoftware.galaxyforce.screen.ScreenFactory.ScreenType;
import com.danosoftware.galaxyforce.services.Games;
import com.danosoftware.galaxyforce.sprites.game.interfaces.SplashSprite;
import com.danosoftware.galaxyforce.sprites.properties.SplashSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.refactor.ISprite;
import com.danosoftware.galaxyforce.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SplashModelImpl implements TouchScreenModel
{

    private List<Text> textList;

    // list of text items to display
    Text title = null;
    Text company = null;
    Text copyright = null;

    // list of sprites
    private List<ISprite> allSprites = null;

    /* reference to controller */
    private Controller controller = null;

    private ScreenTouch screenTouch = null;

    private ModelState modelState = null;

    // how long splash screen has been displayed for so far (in seconds)
    float splashScreenTime = 0f;

    // how long splash screen should be displayed for (in seconds)
    private static final float SPLASH_SCREEN_WAIT = 4f;

    public SplashModelImpl(Controller controller)
    {

        this.allSprites = new ArrayList<>();
        this.textList = new ArrayList<>();

        this.controller = controller;

        // add screen touch to trigger screenTouch method when user touches
        // screen
        this.screenTouch = new ScreenTouch(this, controller, 0, 0, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
    }

    @Override
    public void initialise()
    {
        // title = Text.newTextRelativePositionBoth("GALAXY FORCE",
        // TextPositionX.CENTRE, TextPositionY.TOP);
        // company = Text.newTextRelativePositionBoth("DANO SOFTWARE",
        // TextPositionX.LEFT, TextPositionY.CENTRE);
        // copyright = Text.newTextRelativePositionBoth("COPYRIGHT 2013-2014",
        // TextPositionX.RIGHT, TextPositionY.BOTTOM);

        // textList = new ArrayList<Text>();
        // textList.add(title);
        // textList.add(company);
        // textList.add(copyright);

        allSprites.add(new SplashSprite(GameConstants.SCREEN_MID_X, GameConstants.SCREEN_MID_Y, SplashSpriteIdentifier.SPLASH_SCREEN));

        modelState = ModelState.INITIALISED;

        splashScreenTime = 0f;
    }

    @Override
    public List<ISprite> getSprites()
    {
        return allSprites;
    }

    @Override
    public List<Text> getText()
    {
        return textList;
    }

    @Override
    public void update(float deltaTime)
    {
        // increment splash screen time count by deltaTime
        splashScreenTime = splashScreenTime + deltaTime;

        // check if splash screen has been shown for required time
        if (splashScreenTime >= SPLASH_SCREEN_WAIT)
        {
            setState(ModelState.EXPIRED);
        }

        // if timer expired or screen pressed go to main menu
        if (getState() == ModelState.EXPIRED)
        {
            Screen screen = ScreenFactory.newScreen(ScreenType.MAIN_MENU);
            Games.getGame().setScreen(screen);
        }

    }

    @Override
    public void dispose()
    {
        this.textList = null;
        this.title = null;
        this.company = null;
        this.copyright = null;
        this.modelState = null;
    }

    @Override
    public void screenTouched()
    {
        setState(ModelState.EXPIRED);
    }

    @Override
    public void goBack()
    {
        // No action. Splash screen does not change back button behaviour.
    }

    @Override
    public void resume()
    {
        // no action for this model
    }

    @Override
    public void pause()
    {
        // no action for this model
    }

    private void setState(ModelState modelState)
    {
        this.modelState = modelState;
    }

    private ModelState getState()
    {
        return this.modelState;
    }

}
